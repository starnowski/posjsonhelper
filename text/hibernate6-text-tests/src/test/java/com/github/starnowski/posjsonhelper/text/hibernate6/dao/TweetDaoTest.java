package com.github.starnowski.posjsonhelper.text.hibernate6.dao;

import com.github.starnowski.posjsonhelper.text.hibernate6.model.Tweet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.github.starnowski.posjsonhelper.text.hibernate6.Application.*;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
        config = @SqlConfig(transactionMode = ISOLATED),
        executionPhase = BEFORE_TEST_METHOD)
@Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
        config = @SqlConfig(transactionMode = ISOLATED),
        executionPhase = AFTER_TEST_METHOD)
public class TweetDaoTest {

    @Autowired
    private TweetDao tested;

    private static Stream<Arguments> provideShouldFindCorrectTweetsByPlainQueryInDescriptionForDefaultConfiguration() {
        return Stream.of(
                Arguments.of("cats", asList(1L, 3L)),
                Arguments.of("rats", asList(2L, 3L)),
                Arguments.of("rats cats", asList(3L)),
                Arguments.of("cats rats", asList(3L)),
                Arguments.of("cats rats cats", asList(3L))
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

    private static Stream<Arguments> provideShouldFindCorrectTweetsBySinglePlainQueryInDescription() {
        return Stream.of(
                Arguments.of("cats", asList(1L, 3L)),
                Arguments.of("cat", asList(1L, 3L)),
                Arguments.of("rats", asList(2L, 3L)),
                Arguments.of("rat", asList(2L, 3L)),
                Arguments.of("rats cats", asList(3L)),
                Arguments.of("cats rats", asList(3L)),
                Arguments.of("rat cat", asList(3L)),
                Arguments.of("cat rat", asList(3L))
        );
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

    private static Stream<Arguments> provideShouldFindCorrectTweetsBySinglePhraseInDescriptionForDefaultConfiguration() {
        return Stream.of(
                Arguments.of("Rats and cats", asList(3L)),
                Arguments.of("Rats cats", new ArrayList<>()),
                Arguments.of("cats Rats", new ArrayList<>())
        );
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

    private static Stream<Arguments> provideShouldFindCorrectTweetsBySinglePhraseInDescription() {
        return Stream.of(
                Arguments.of("rat and cats", asList(3L)),
                Arguments.of("rat and cat", asList(3L)),
                Arguments.of("rat cat", new ArrayList<>()),
                Arguments.of("cat Rats", new ArrayList<>())
        );
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


    private static Stream<Arguments> provideShouldFindCorrectTweetsByWebSearchToTSQueryInDescription() {
        return Stream.of(
                Arguments.of("Postgres", asList(4L,5L)),
                Arguments.of("Postgres Oracle", new ArrayList<>()),
                Arguments.of("Postgres or Oracle", asList(4L,5L,6L)),
                Arguments.of("database", asList(5L,6L)),
                Arguments.of("database -Postgres", asList(6L)),
                Arguments.of("\"already existed functions\"", asList(4L)),
                Arguments.of("\"existed already functions\"", new ArrayList<>())
        );
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, TWEETS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return all ids when searching by query for english configuration' for websearch_to_tsquery function")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectTweetsByWebSearchToTSQueryInDescription")
    public void shouldFindCorrectTweetsByWebSearchToTSQueryInDescription(String phrase, List<Long> expectedIds) {

        // when
        List<Tweet> results = tested.findCorrectTweetsByWebSearchToTSQueryInDescription(phrase, ENGLISH_CONFIGURATION);

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

        // when
        List<Tweet> results = tested.findCorrectTweetsByWebSearchToTSQueryInDescriptionAndRegconfigTypeCastOperatorFunctionObjectInstance(phrase, ENGLISH_CONFIGURATION);

        // then
        assertThat(results).hasSize(expectedIds.size());
        assertThat(results.stream().map(Tweet::getId).collect(toSet())).containsAll(expectedIds);
    }
}
