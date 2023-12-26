package com.github.starnowski.posjsonhelper.text.hibernate6.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.*;
import java.util.stream.Stream;

import static com.github.starnowski.posjsonhelper.text.hibernate6.Application.CLEAR_DATABASE_SCRIPT_PATH;
import static com.github.starnowski.posjsonhelper.text.hibernate6.Application.TWEETS_SCRIPT_PATH;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, TWEETS_SCRIPT_PATH},
        config = @SqlConfig(transactionMode = ISOLATED),
        executionPhase = BEFORE_TEST_METHOD)
@Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
        config = @SqlConfig(transactionMode = ISOLATED),
        executionPhase = AFTER_TEST_METHOD)
public class SQLInjectionItTest extends AbstractItTest {

    private static final String PLAINTO_TSQUERY_STATEMENT_PATTERN = "SELECT t.id FROM tweet t WHERE to_tsvector(t.short_content) @@ plainto_tsquery(%s)";
    private static final String PHRASETO_TSQUERY_STATEMENT_PATTERN = "SELECT t.id FROM tweet t WHERE to_tsvector(t.short_content) @@ phraseto_tsquery(%s)";
    private static final String WEBSEARCH_TO_TSQUERY_STATEMENT_PATTERN = "SELECT t.id FROM tweet t WHERE to_tsvector(t.short_content) @@ websearch_to_tsquery(%s)";
    private static final String SETTING_CONFIGURATION_PROPERTY_PATTERN = "SELECT set_config('%s', '%s', false);";
    private static final String GETTING_CONFIGURATION_PROPERTY_PATTERN = "SELECT current_setting('%s');";

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    TweetDao tested;

    private static Stream<Arguments> provideShouldNotModifyCurrentConfigurationPropertyWithExpectedValue() {
        return Stream.of(
                Arguments.of("c.prop1", "some value", PLAINTO_TSQUERY_STATEMENT_PATTERN, "'cats'", asList(1, 3), 9),
                Arguments.of("prop.value", "this is a test", PLAINTO_TSQUERY_STATEMENT_PATTERN, "'rats'", asList(2, 3), 9),
                Arguments.of("c.prop1", "some value", WEBSEARCH_TO_TSQUERY_STATEMENT_PATTERN, "'cats'", asList(1, 3), 11),
                Arguments.of("prop.value", "this is a test", WEBSEARCH_TO_TSQUERY_STATEMENT_PATTERN, "'rats'", asList(2, 3), 11),
                Arguments.of("prop.value", "this is a test", PHRASETO_TSQUERY_STATEMENT_PATTERN, "'Rats and cats'", List.of(3), 9),
                Arguments.of("prop.value", "this is a test", PHRASETO_TSQUERY_STATEMENT_PATTERN, "'Rats cats'", new ArrayList<>(), 9)
        );
    }

    @DisplayName("should not modify current configuration property #property with expected value #value when executing correct statement without corrupted data")
    @ParameterizedTest
    @MethodSource("provideShouldNotModifyCurrentConfigurationPropertyWithExpectedValue")
    public void shouldNotModifyCurrentConfigurationPropertyWithExpectedValue(String property, String value, String sqlPattern, String textQuery, List<String> expectedQueryResults, int minimalMajorPostgresVersion) {
        assumeTrue(postgresVersion.getMajor() >= minimalMajorPostgresVersion, String.format("Test ignored because the minimal postgres version of Postgres is %d", minimalMajorPostgresVersion));

        //GIVEN
        jdbcTemplate.execute(String.format(SETTING_CONFIGURATION_PROPERTY_PATTERN, property, value));
        var query = String.format(sqlPattern, textQuery);
        System.out.println("Testing query : " + query);

        //WHEN
        var results = jdbcTemplate.queryForList(query, Integer.class);

        //THEN
        var currentValue = jdbcTemplate.queryForObject(String.format(GETTING_CONFIGURATION_PROPERTY_PATTERN, property), String.class);
        assertThat(currentValue).isEqualTo(value);
        assertThat(new HashSet<>(results)).isEqualTo(new HashSet<>(expectedQueryResults));
    }

    private static Stream<Arguments> provideShouldModifyCurrentConfigurationPropertyAndChangeCurrentValueWithExpected() {
        return Stream.of(
                Arguments.of("c.prop1", "some value", PLAINTO_TSQUERY_STATEMENT_PATTERN, "'cats'); SELECT set_config('c.prop1', 'SECURITY FAILED', false)--", "SECURITY FAILED", 9),
                Arguments.of("prop.value", "this is a test", PLAINTO_TSQUERY_STATEMENT_PATTERN, "'rats'); SELECT set_config('prop.value', 'WARNING', false)--", "WARNING", 9),
                Arguments.of("c.prop1", "some value", WEBSEARCH_TO_TSQUERY_STATEMENT_PATTERN, "'cats'); SELECT set_config('c.prop1', 'SECURITY FAILED', false)--", "SECURITY FAILED", 11),
                Arguments.of("prop.value", "this is a test", WEBSEARCH_TO_TSQUERY_STATEMENT_PATTERN, "'rats'); SELECT set_config('prop.value', 'WARNING', false)--", "WARNING", 11),
                Arguments.of("prop.value", "this is a test", PHRASETO_TSQUERY_STATEMENT_PATTERN, "'Rats and cats'); SELECT set_config('prop.value', 'SECURITY FAILED', false)--", "SECURITY FAILED", 9),
                Arguments.of("prop.value", "this is a test", PHRASETO_TSQUERY_STATEMENT_PATTERN, "'Rats cats'); SELECT set_config('prop.value', 'WARNING', false)--", "WARNING", 9)
        );
    }

    @DisplayName("should modify current configuration property #property and change current value #value with expected #expected when using statements with corrupted query")
    @ParameterizedTest
    @MethodSource("provideShouldModifyCurrentConfigurationPropertyAndChangeCurrentValueWithExpected")
    public void shouldModifyCurrentConfigurationPropertyAndChangeCurrentValueWithExpected(String property, String value, String sqlPattern, String textQuery, String expected, int minimalMajorPostgresVersion) {
        assumeTrue(postgresVersion.getMajor() >= minimalMajorPostgresVersion, String.format("Test ignored because the minimal postgres version of Postgres is %d", minimalMajorPostgresVersion));

        //GIVEN
        jdbcTemplate.execute(String.format(SETTING_CONFIGURATION_PROPERTY_PATTERN, property, value));
        var query = String.format(sqlPattern, textQuery);
        System.out.println("Testing query : " + query);

        //WHEN
        jdbcTemplate.execute(String.format(query, textQuery));

        //THEN
        var currentValue = jdbcTemplate.queryForObject(String.format(GETTING_CONFIGURATION_PROPERTY_PATTERN, property), String.class);
        assertThat(currentValue).isEqualTo(expected);

        // https://portswigger.net/web-security/sql-injection
    }

    private static Stream<Arguments> provideShouldNotModifyCurrentConfigurationPropertyAndChangeCurrentValueWhenUsingPredicateJsonbAllArrayStringsExistPredicateWithArrayElementsTagsAsPassedArguments() {
        return Stream.of(
                Arguments.of("prop.value"  ,   "this is a test" ,  "'some val']); SELECT set_config('prop.value', 'WARNING', false);--")
        );
    }

    private static Stream<Arguments> provideShouldNotModifyCurrentConfigurationPropertyAndChangeCurrentValueWithExpected() {
        return Stream.of(
                Arguments.of("c.prop1", "some value", "'cats'); SELECT set_config('c.prop1', 'SECURITY FAILED', false)--"),
                Arguments.of("prop.value", "this is a test",  "'rats'); SELECT set_config('prop.value', 'WARNING', false)--")
        );
    }

    @DisplayName("should not modify current configuration property #property and change current value #value when using predicate JsonbAllArrayStringsExistPredicate with array elements #tags as passed arguments")
    @ParameterizedTest
    @MethodSource("provideShouldNotModifyCurrentConfigurationPropertyAndChangeCurrentValueWithExpected")
    public void shouldNotModifyCurrentConfigurationPropertyAndChangeCurrentValueWhenUsingPlainQueryWihtCorruptedQuery(String property, String value, String query){
        //GIVEN:
        jdbcTemplate.execute(String.format(SETTING_CONFIGURATION_PROPERTY_PATTERN, property, value));

        //WHEN:
        var results = tested.findBySinglePlainQueryInDescriptionForDefaultConfiguration(query);

        //THEN:
        assertThat(results).isEmpty();
        var currentValue = jdbcTemplate.queryForObject(String.format(GETTING_CONFIGURATION_PROPERTY_PATTERN, property), String.class);
        assertThat(currentValue).isEqualTo(value);
        results.isEmpty();

        // https://portswigger.net/web-security/sql-injection
    }

    private static Stream<Arguments> provideShouldNotModifyCurrentConfigurationPropertyAndChangeCurrentValueWhenUsingPredicateJsonbAnyArrayStringsExistPredicateWithArrayElementsAsPassedArguments() {
        return Stream.of(
                Arguments.of("prop.value"   ,   "this is a test" ,  "'some val']); SELECT set_config('prop.value', 'WARNING', false);--")
        );
    }

//    @DisplayName("should not modify current configuration property #property and change current value #value when using predicate JsonbAnyArrayStringsExistPredicate with array elements #tags as passed arguments")
//    @ParameterizedTest
//    @MethodSource("provideShouldNotModifyCurrentConfigurationPropertyAndChangeCurrentValueWhenUsingPredicateJsonbAnyArrayStringsExistPredicateWithArrayElementsAsPassedArguments")
//    public void shouldNotModifyCurrentConfigurationPropertyAndChangeCurrentValueWhenUsingPredicateJsonbAnyArrayStringsExistPredicateWithArrayElementsAsPassedArguments(String property, String value, String tags){
//        //GIVEN:
//        jdbcTemplate.execute(String.format(SETTING_CONFIGURATION_PROPERTY_PATTERN, property, value));
//
//        //WHEN:
//        var results = tested.findAllByAnyMatchingTags(new HashSet<String>(Arrays.asList(tags)));
//
//        //THEN:
//        results.isEmpty();
//        var currentValue = jdbcTemplate.queryForObject(String.format(GETTING_CONFIGURATION_PROPERTY_PATTERN, property), String.class);
//        assertThat(currentValue).isEqualTo(value);
//        results.isEmpty();
//        // https://portswigger.net/web-security/sql-injection
//    }
}
