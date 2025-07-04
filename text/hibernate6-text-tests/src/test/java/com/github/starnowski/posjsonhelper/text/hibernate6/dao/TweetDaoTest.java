package com.github.starnowski.posjsonhelper.text.hibernate6.dao;

import com.github.starnowski.posjsonhelper.text.hibernate6.model.Tweet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.github.starnowski.posjsonhelper.text.hibernate6.Application.*;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
        config = @SqlConfig(transactionMode = ISOLATED),
        executionPhase = BEFORE_TEST_METHOD)
@Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
        config = @SqlConfig(transactionMode = ISOLATED),
        executionPhase = AFTER_TEST_METHOD)
public class TweetDaoTest extends AbstractItTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TweetDao tested;

    private static Stream<Arguments> provideShouldFindCorrectTweetsByPlainQueryInDescriptionForDefaultConfiguration() {
        return Stream.of(
                Arguments.of("cats", asList(1L, 3L)),
                Arguments.of("rats", asList(2L, 3L)),
                Arguments.of("rats cats", List.of(3L)),
                Arguments.of("cats rats", List.of(3L)),
                Arguments.of("cats rats cats", List.of(3L))
        );
    }

    private static Stream<Arguments> provideShouldFindCorrectTweetsByToTSQueryFunctionInDescriptionForDefaultConfiguration() {
        return Stream.of(
                Arguments.of("cats", asList(1L, 3L)),
                Arguments.of("rats", asList(2L, 3L)),
                Arguments.of("rats & cats", List.of(3L)),
                Arguments.of("cats & rats", List.of(3L)),
                Arguments.of("cats & rats & cats", List.of(3L))
        );
    }


    private static Stream<Arguments> provideShouldFindCorrectTweetsBySinglePlainQueryInDescription() {
        return Stream.of(
                Arguments.of("cats", asList(1L, 3L)),
                Arguments.of("cat", asList(1L, 3L)),
                Arguments.of("rats", asList(2L, 3L)),
                Arguments.of("rat", asList(2L, 3L)),
                Arguments.of("rats cats", List.of(3L)),
                Arguments.of("cats rats", List.of(3L)),
                Arguments.of("rat cat", List.of(3L)),
                Arguments.of("cat rat", List.of(3L))
        );
    }

    private static Stream<Arguments> provideShouldFindCorrectTweetsBySingleToTSQueryFunctionInDescription() {
        return Stream.of(
                Arguments.of("cats & world", List.of(1L)),
                Arguments.of("cats <-> rule", List.of(1L)),
                Arguments.of("cat", asList(1L, 3L)),
                Arguments.of("cat | projects", asList(1L, 3L, 4L)),
                Arguments.of("(cat & !hate) | projects", asList(1L, 4L)),
                Arguments.of("rats", asList(2L, 3L)),
                Arguments.of("rat", asList(2L, 3L)),
                Arguments.of("rats & cats", List.of(3L)),
                Arguments.of("cats & rats", List.of(3L)),
                Arguments.of("rat & cat", List.of(3L)),
                Arguments.of("cat & rat", List.of(3L))
        );
    }

    private static Stream<Arguments> provideShouldFindCorrectTweetsBySinglePhraseInDescriptionForDefaultConfiguration() {
        return Stream.of(
                Arguments.of("Rats and cats", List.of(3L)),
                Arguments.of("Rats cats", new ArrayList<>()),
                Arguments.of("cats Rats", new ArrayList<>())
        );
    }

    private static Stream<Arguments> provideShouldFindCorrectTweetsBySinglePhraseInDescription() {
        return Stream.of(
                Arguments.of("rat and cats", List.of(3L)),
                Arguments.of("rat and cat", List.of(3L)),
                Arguments.of("rat cat", new ArrayList<>()),
                Arguments.of("cat Rats", new ArrayList<>())
        );
    }

    private static Stream<Arguments> provideShouldFindCorrectTweetsByWebSearchToTSQueryInDescription() {
        return Stream.of(
                Arguments.of("Postgres", asList(4L, 5L)),
                Arguments.of("Postgres Oracle", new ArrayList<>()),
                Arguments.of("Postgres or Oracle", asList(4L, 5L, 6L)),
                Arguments.of("database", asList(5L, 6L)),
                Arguments.of("database -Postgres", List.of(6L)),
                Arguments.of("\"already existed functions\"", List.of(4L)),
                Arguments.of("\"existed already functions\"", new ArrayList<>())
        );
    }

    private static Stream<Arguments> provideShouldFindCorrectTweetsByTitleAndShortContent() {
        return Stream.of(
                Arguments.of("feature existed", asList(4L))
        );
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, TWEETS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return all ids when searching by query for plainto_tsquery function")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectTweetsByPlainQueryInDescriptionForDefaultConfiguration")
    public void shouldFindCorrectTweetsByPlainQueryInDescriptionForDefaultConfiguration(String phrase, List<Long> expectedIds) {

        // when
        List<Tweet> results = tested.findBySinglePlainQueryInDescriptionForDefaultConfiguration(phrase);

        // then
        assertThat(results).hasSize(expectedIds.size());
        assertThat(results.stream().map(Tweet::getId).collect(toSet())).containsAll(expectedIds);
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, TWEETS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return all ids when searching by query for plainto_tsquery function with HQL")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectTweetsByPlainQueryInDescriptionForDefaultConfiguration")
    public void shouldFindCorrectTweetsByPlainQueryInDescriptionForDefaultConfigurationWithHQL(String phrase, List<Long> expectedIds) {

        // when
        List<Tweet> results = tested.findBySinglePlainQueryInDescriptionForDefaultConfigurationWithHQL(phrase);

        // then
        assertThat(results).hasSize(expectedIds.size());
        assertThat(results.stream().map(Tweet::getId).collect(toSet())).containsAll(expectedIds);
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, TWEETS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return all ids when searching by query for to_tsquery function with HQL")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectTweetsByToTSQueryFunctionInDescriptionForDefaultConfiguration")
    public void shouldFindCorrectTweetsByToTSQueryFunctionInDescriptionForDefaultConfigurationWithHQL(String phrase, List<Long> expectedIds) {

        // when
        List<Tweet> results = tested.findBySingleToTSQueryFunctionInDescriptionForDefaultConfigurationWithHQL(phrase);

        // then
        assertThat(results).hasSize(expectedIds.size());
        assertThat(results.stream().map(Tweet::getId).collect(toSet())).containsAll(expectedIds);
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, TWEETS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return all ids when searching by query for english configuration' for plainto_tsquery function")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectTweetsBySinglePlainQueryInDescription")
    public void shouldFindCorrectTweetsBySinglePlainQueryInDescription(String phrase, List<Long> expectedIds) {

        // when
        List<Tweet> results = tested.findBySinglePlainQueryInDescriptionForConfiguration(phrase, ENGLISH_CONFIGURATION);

        // then
        assertThat(results).hasSize(expectedIds.size());
        assertThat(results.stream().map(Tweet::getId).collect(toSet())).containsAll(expectedIds);
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, TWEETS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return all ids when searching by query for english configuration' for to_tsquery function")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectTweetsBySingleToTSQueryFunctionInDescription")
    public void shouldFindCorrectTweetsBySingleToTSQueryFunctionInDescription(String phrase, List<Long> expectedIds) {

        // when
        List<Tweet> results = tested.findBySingleToTSQueryFunctionInDescriptionForConfiguration(phrase, ENGLISH_CONFIGURATION);

        // then
        assertThat(results).hasSize(expectedIds.size());
        assertThat(results.stream().map(Tweet::getId).collect(toSet())).containsAll(expectedIds);
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, TWEETS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return all ids when searching by query for default configuration' for to_tsquery function")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectTweetsByToTSQueryFunctionInDescriptionForDefaultConfiguration")
    public void shouldFindCorrectTweetsBySingleToTSQueryFunctionInDescriptionForDefaultConfiguration(String phrase, List<Long> expectedIds) {

        // when
        List<Tweet> results = tested.findBySingleToTSQueryFunctionInDescriptionForDefaultConfiguration(phrase);

        // then
        assertThat(results).hasSize(expectedIds.size());
        assertThat(results.stream().map(Tweet::getId).collect(toSet())).containsAll(expectedIds);
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, TWEETS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return all ids when searching by query for english configuration' for plainto_tsquery function with HQL")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectTweetsBySinglePlainQueryInDescription")
    public void shouldFindCorrectTweetsBySinglePlainQueryInDescriptionWithHQL(String phrase, List<Long> expectedIds) {

        // when
        List<Tweet> results = tested.findBySinglePlainQueryInDescriptionForConfigurationWithHQL(phrase, ENGLISH_CONFIGURATION);

        // then
        assertThat(results).hasSize(expectedIds.size());
        assertThat(results.stream().map(Tweet::getId).collect(toSet())).containsAll(expectedIds);
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, TWEETS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return all ids when searching by query for english configuration' for plainto_tsquery function with RegconfigTypeCastOperatorFunction object as configuration")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectTweetsBySinglePlainQueryInDescription")
    public void shouldFindCorrectTweetsBySinglePlainQueryInDescriptionAndRegconfigTypeCastOperatorFunctionObjectInstance(String phrase, List<Long> expectedIds) {

        // when
        List<Tweet> results = tested.findBySinglePlainQueryInDescriptionForConfigurationAndRegconfigTypeCastOperatorFunctionObjectInstance(phrase, ENGLISH_CONFIGURATION);

        // then
        assertThat(results).hasSize(expectedIds.size());
        assertThat(results.stream().map(Tweet::getId).collect(toSet())).containsAll(expectedIds);
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, TWEETS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return all ids when searching by query for english configuration' for plainto_tsquery function with RegconfigTypeCastOperatorFunction object as configuration with HQL")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectTweetsBySinglePlainQueryInDescription")
    public void shouldFindCorrectTweetsBySinglePlainQueryInDescriptionAndRegconfigTypeCastOperatorFunctionObjectInstanceWithHQL(String phrase, List<Long> expectedIds) {

        // when
        List<Tweet> results = tested.findBySinglePlainQueryInDescriptionForConfigurationAndRegconfigTypeCastOperatorFunctionObjectInstanceWithHQL(phrase, ENGLISH_CONFIGURATION);

        // then
        assertThat(results).hasSize(expectedIds.size());
        assertThat(results.stream().map(Tweet::getId).collect(toSet())).containsAll(expectedIds);
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, TWEETS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return all ids when searching by query for phraseto_tsquery function")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectTweetsBySinglePhraseInDescriptionForDefaultConfiguration")
    public void shouldFindCorrectTweetsBySinglePhraseInDescriptionForDefaultConfiguration(String phrase, List<Long> expectedIds) {

        // when
        List<Tweet> results = tested.findBySinglePhraseInDescriptionForDefaultConfiguration(phrase);

        // then
        assertThat(results).hasSize(expectedIds.size());
        assertThat(results.stream().map(Tweet::getId).collect(toSet())).containsAll(expectedIds);
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, TWEETS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return all ids when searching by query for phraseto_tsquery function with HQL")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectTweetsBySinglePhraseInDescriptionForDefaultConfiguration")
    public void shouldFindCorrectTweetsBySinglePhraseInDescriptionForDefaultConfigurationWithHQL(String phrase, List<Long> expectedIds) {

        // when
        List<Tweet> results = tested.findBySinglePhraseInDescriptionForDefaultConfigurationWithHQL(phrase);

        // then
        assertThat(results).hasSize(expectedIds.size());
        assertThat(results.stream().map(Tweet::getId).collect(toSet())).containsAll(expectedIds);
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, TWEETS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return all ids when searching by query for english configuration' for phraseto_tsquery function")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectTweetsBySinglePhraseInDescription")
    public void shouldFindCorrectTweetsBySinglePhraseInDescription(String phrase, List<Long> expectedIds) {

        // when
        List<Tweet> results = tested.findBySinglePhraseInDescriptionForConfiguration(phrase, ENGLISH_CONFIGURATION);

        // then
        assertThat(results).hasSize(expectedIds.size());
        assertThat(results.stream().map(Tweet::getId).collect(toSet())).containsAll(expectedIds);
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, TWEETS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return all ids when searching by query for english configuration' for phraseto_tsquery function with HQL")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectTweetsBySinglePhraseInDescription")
    public void shouldFindCorrectTweetsBySinglePhraseInDescriptionWithHQL(String phrase, List<Long> expectedIds) {

        // when
        List<Tweet> results = tested.findBySinglePhraseInDescriptionForConfigurationWithHQL(phrase, ENGLISH_CONFIGURATION);

        // then
        assertThat(results).hasSize(expectedIds.size());
        assertThat(results.stream().map(Tweet::getId).collect(toSet())).containsAll(expectedIds);
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, TWEETS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return all ids when searching by query for english configuration' for phraseto_tsquery function with RegconfigTypeCastOperatorFunction object as configuration")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectTweetsBySinglePhraseInDescription")
    public void shouldFindCorrectTweetsBySinglePhraseInDescriptionAndRegconfigTypeCastOperatorFunctionObjectInstance(String phrase, List<Long> expectedIds) {

        // when
        List<Tweet> results = tested.findBySinglePhraseInDescriptionForConfigurationAndRegconfigTypeCastOperatorFunctionInstance(phrase, ENGLISH_CONFIGURATION);

        // then
        assertThat(results).hasSize(expectedIds.size());
        assertThat(results.stream().map(Tweet::getId).collect(toSet())).containsAll(expectedIds);
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, TWEETS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return all ids when searching by query for english configuration' for phraseto_tsquery function with RegconfigTypeCastOperatorFunction object as configuration with HQL")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectTweetsBySinglePhraseInDescription")
    public void shouldFindCorrectTweetsBySinglePhraseInDescriptionAndRegconfigTypeCastOperatorFunctionObjectInstanceWithHQL(String phrase, List<Long> expectedIds) {

        // when
        List<Tweet> results = tested.findBySinglePhraseInDescriptionForConfigurationAndRegconfigTypeCastOperatorFunctionInstanceWithHQL(phrase, ENGLISH_CONFIGURATION);

        // then
        assertThat(results).hasSize(expectedIds.size());
        assertThat(results.stream().map(Tweet::getId).collect(toSet())).containsAll(expectedIds);
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, TWEETS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return all ids when searching by query for english configuration' for websearch_to_tsquery function")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectTweetsByWebSearchToTSQueryInDescription")
    public void shouldFindCorrectTweetsByWebSearchToTSQueryInDescription(String phrase, List<Long> expectedIds) {
        assumeTrue(postgresVersion.getMajor() >= 11, "Test ignored because the 'websearch_to_tsquery' function was added in version 10 of Postgres");

        // when
        List<Tweet> results = tested.findCorrectTweetsByWebSearchToTSQueryInDescription(phrase, ENGLISH_CONFIGURATION);

        // then
        assertThat(results).hasSize(expectedIds.size());
        assertThat(results.stream().map(Tweet::getId).collect(toSet())).containsAll(expectedIds);
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, TWEETS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return all ids when searching by query for english configuration' for websearch_to_tsquery function with HQL")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectTweetsByWebSearchToTSQueryInDescription")
    public void shouldFindCorrectTweetsByWebSearchToTSQueryInDescriptionWithHQL(String phrase, List<Long> expectedIds) {
        assumeTrue(postgresVersion.getMajor() >= 11, "Test ignored because the 'websearch_to_tsquery' function was added in version 10 of Postgres");

        // when
        List<Tweet> results = tested.findCorrectTweetsByWebSearchToTSQueryInDescriptionWithHQL(phrase, ENGLISH_CONFIGURATION);

        // then
        assertThat(results).hasSize(expectedIds.size());
        assertThat(results.stream().map(Tweet::getId).collect(toSet())).containsAll(expectedIds);
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, TWEETS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return all ids when searching by query for WebSearchToTSQuery function with HQL")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectTweetsByPlainQueryInDescriptionForDefaultConfiguration")
    public void shouldFindCorrectTweetsByWebSearchToTSQueryInDescriptionForDefaultConfigurationWithHQL(String phrase, List<Long> expectedIds) {
        assumeTrue(postgresVersion.getMajor() >= 11, "Test ignored because the 'websearch_to_tsquery' function was added in version 10 of Postgres");

        // when
        List<Tweet> results = tested.findByWebSearchToTSQueryInDescriptionForDefaultConfiguration(phrase);

        // then
        assertThat(results).hasSize(expectedIds.size());
        assertThat(results.stream().map(Tweet::getId).collect(toSet())).containsAll(expectedIds);
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, TWEETS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return all ids when searching by query for english configuration' for websearch_to_tsquery function with RegconfigTypeCastOperatorFunction object as configuration")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectTweetsByWebSearchToTSQueryInDescription")
    public void shouldFindCorrectTweetsByWebSearchToTSQueryInDescriptionAndRegconfigTypeCastOperatorFunctionObjectInstance(String phrase, List<Long> expectedIds) {
        assumeTrue(postgresVersion.getMajor() >= 11, "Test ignored because the 'websearch_to_tsquery' function was added in version 10 of Postgres");

        // when
        List<Tweet> results = tested.findCorrectTweetsByWebSearchToTSQueryInDescriptionAndRegconfigTypeCastOperatorFunctionObjectInstance(phrase, ENGLISH_CONFIGURATION);

        // then
        assertThat(results).hasSize(expectedIds.size());
        assertThat(results.stream().map(Tweet::getId).collect(toSet())).containsAll(expectedIds);
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, TWEETS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return all ids when searching by query for english configuration' for websearch_to_tsquery function with RegconfigTypeCastOperatorFunction object as configuration with HQL")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectTweetsByWebSearchToTSQueryInDescription")
    public void shouldFindCorrectTweetsByWebSearchToTSQueryInDescriptionAndRegconfigTypeCastOperatorFunctionObjectInstanceWithHQL(String phrase, List<Long> expectedIds) {
        assumeTrue(postgresVersion.getMajor() >= 11, "Test ignored because the 'websearch_to_tsquery' function was added in version 10 of Postgres");

        // when
        List<Tweet> results = tested.findCorrectTweetsByWebSearchToTSQueryInDescriptionAndRegconfigTypeCastOperatorFunctionObjectInstanceWithHQL(phrase, ENGLISH_CONFIGURATION);

        // then
        assertThat(results).hasSize(expectedIds.size());
        assertThat(results.stream().map(Tweet::getId).collect(toSet())).containsAll(expectedIds);
    }

    // Native support

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, TWEETS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return all ids when searching by query for english configuration' for plainto_tsquery function with native SQL")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectTweetsBySinglePlainQueryInDescription")
    public void shouldFindCorrectTweetsBySinglePlainQueryInDescriptionWithNativeSQL(String phrase, List<Long> expectedIds) {

        // when
        List<Tweet> results = tested.findBySinglePlainQueryInDescriptionForConfigurationWithNativeSQL(phrase, ENGLISH_CONFIGURATION);

        // then
        assertThat(results).hasSize(expectedIds.size());
        assertThat(results.stream().map(Tweet::getId).collect(toSet())).containsAll(expectedIds);
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, TWEETS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return all ids when searching by query for english configuration' for websearch_to_tsquery function with native SQL")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectTweetsByWebSearchToTSQueryInDescription")
    public void shouldFindCorrectTweetsByWebSearchToTSQueryInDescriptionWithNativeSQL(String phrase, List<Long> expectedIds) {
        assumeTrue(postgresVersion.getMajor() >= 11, "Test ignored because the 'websearch_to_tsquery' function was added in version 10 of Postgres");

        // when
        List<Tweet> results = tested.findCorrectTweetsByWebSearchToTSQueryInDescriptionWithNativeSQL(phrase, ENGLISH_CONFIGURATION);

        // then
        assertThat(results).hasSize(expectedIds.size());
        assertThat(results.stream().map(Tweet::getId).collect(toSet())).containsAll(expectedIds);
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, TWEETS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return all ids when searching by query for default configuration for concatenated argument")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectTweetsByTitleAndShortContent")
    public void shouldFindCorrectTweetsByWebSearchToTSQueryInDescriptionWithConcatenatedOperator(String phrase, List<Long> expectedIds) {
        assumeTrue(postgresVersion.getMajor() >= 11, "Test ignored because the 'websearch_to_tsquery' function was added in version 10 of Postgres");

        // when
        List<Tweet> results = tested.findByWebSearchForConcatenatedFieldsForDefaultConfiguration(phrase);

        // then
        assertThat(results).hasSize(expectedIds.size());
        assertThat(results.stream().map(Tweet::getId).collect(toSet())).containsAll(expectedIds);
    }
}
