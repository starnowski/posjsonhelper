package com.github.starnowski.posjsonhelper.hibernate6.demo.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.github.starnowski.posjsonhelper.hibernate6.demo.Application.CLEAR_DATABASE_SCRIPT_PATH;
import static com.github.starnowski.posjsonhelper.hibernate6.demo.Application.ITEMS_SCRIPT_PATH;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
        config = @SqlConfig(transactionMode = ISOLATED),
        executionPhase = BEFORE_TEST_METHOD)
@Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
        config = @SqlConfig(transactionMode = ISOLATED),
        executionPhase = AFTER_TEST_METHOD)
public abstract class AbstractSQLInjectionItTest {

    private static final String JSON_ALL_STATEMENT_PATTERN = "SELECT id FROM item WHERE jsonb_all_array_strings_exist(jsonb_extract_path(jsonb_content, %s), array[%s]) ";
    private static final String JSON_ANY_STATEMENT_PATTERN = "SELECT id FROM item WHERE jsonb_any_array_strings_exist(jsonb_extract_path(jsonb_content, %s), array[%s]) ";
    private static final String SETTING_CONFIGURATION_PROPERTY_PATTERN = "SELECT set_config('%s', '%s', false);";
    private static final String GETTING_CONFIGURATION_PROPERTY_PATTERN = "SELECT current_setting('%s');";

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    ItemDao tested;

    private static Stream<Arguments> provideShouldNotModifyCurrentConfigurationPropertyWithExpectedValue() {
        return Stream.of(
                Arguments.of("c.prop1"   ,   "some value"        , JSON_ALL_STATEMENT_PATTERN , "'top_element_with_set_of_values'" , "'TAG1', 'TAG2'" ,  new HashSet<>(Arrays.asList(1))),
                Arguments.of("prop.value"   ,   "this is a test" , JSON_ALL_STATEMENT_PATTERN , "'top_element_with_set_of_values'" , "'TAG1', 'TAG2'" ,  new HashSet<>(Arrays.asList(1))),
                Arguments.of("prop.value"   ,   "this is a test" , JSON_ANY_STATEMENT_PATTERN , "'top_element_with_set_of_values'" , "'TAG1', 'TAG2'" ,  new HashSet<>(Arrays.asList(1, 3)))
        );
    }

    @DisplayName("should not modify current configuration property #property with expected value #value when executing correct statement, sql pattern #sqlPattern, jsonPathInput #jsonPathInput, array input #arrayInput")
    @ParameterizedTest
    @MethodSource("provideShouldNotModifyCurrentConfigurationPropertyWithExpectedValue")
    public void shouldNotModifyCurrentConfigurationPropertyWithExpectedValue(String property, String value, String sqlPattern, String jsonPathInput, String arrayInput, Set<String> expectedQueryResults){
        //GIVEN
        jdbcTemplate.execute(String.format(SETTING_CONFIGURATION_PROPERTY_PATTERN, property, value));
        var query = String.format(sqlPattern, jsonPathInput, arrayInput);
        System.out.println("Testing query : " + query);

        //WHEN
        var results = jdbcTemplate.queryForList(query, Integer.class);

        //THEN
        var currentValue = jdbcTemplate.queryForObject(String.format(GETTING_CONFIGURATION_PROPERTY_PATTERN, property), String.class);
        assertThat(currentValue).isEqualTo(value);
        assertThat(new HashSet<>(results)).isEqualTo(expectedQueryResults);
    }
}
