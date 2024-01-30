package com.github.starnowski.posjsonhelper.text.hibernate6.dao;

import com.github.starnowski.posjsonhelper.text.hibernate6.model.TweetWithLocale;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.github.starnowski.posjsonhelper.text.hibernate6.Application.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, TEXT_INDEX_SCRIPT_PATH, TWEETS_WITH_LOCALE_SCRIPT_PATH},
        config = @SqlConfig(transactionMode = ISOLATED),
        executionPhase = BEFORE_TEST_METHOD)
@Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
        config = @SqlConfig(transactionMode = ISOLATED),
        executionPhase = AFTER_TEST_METHOD)
@EnabledIfSystemProperty(named = "run.custom.directory.test", matches = "true")
public class TextIndexTest extends AbstractItTest {

    @Autowired
    private TweetWithLocalDao tested;

    private static Stream<Arguments> provideShouldFindCorrectTweetsBySinglePlainQueryInDescription() {
        return Stream.of(
                Arguments.of("EV future", ENGLISH_CONFIGURATION, List.of(1L)),
                Arguments.of("Hydrogen", ENGLISH_CONFIGURATION, asList(2L, 3L)),
                Arguments.of("Hydrogen", POLISH_CONFIGURATION, emptyList()),
                Arguments.of("Wodór", POLISH_CONFIGURATION, List.of(4L)),
                Arguments.of("Zmywarka czarny", POLISH_CONFIGURATION, List.of(5L)),
                Arguments.of("Zmywarka biały", POLISH_CONFIGURATION, asList(6L, 7L)),
                Arguments.of("Zmywarka srebrny", POLISH_CONFIGURATION, List.of(8L)),
                Arguments.of("Electrolux", POLISH_CONFIGURATION, List.of(5L)),
                Arguments.of("Bosch", POLISH_CONFIGURATION, List.of(6L)),
                Arguments.of("Toshiba", POLISH_CONFIGURATION, List.of(7L)),
                Arguments.of("Siemens", POLISH_CONFIGURATION, List.of(8L))
        );
    }

    private static Stream<Arguments> provideShouldFindCorrectTweetsByWebSearchToTSQueryInDescription() {
        return Stream.of(
                Arguments.of("Postgres", ENGLISH_CONFIGURATION, asList(14L, 15L)),
                Arguments.of("Postgres Oracle", ENGLISH_CONFIGURATION, new ArrayList<>()),
                Arguments.of("Postgres or Oracle", ENGLISH_CONFIGURATION, asList(14L, 15L, 16L)),
                Arguments.of("database", ENGLISH_CONFIGURATION, asList(15L, 16L)),
                Arguments.of("database -Postgres", ENGLISH_CONFIGURATION, List.of(16L)),
                Arguments.of("\"already existed functions\"", ENGLISH_CONFIGURATION, List.of(14L)),
                Arguments.of("\"existed already functions\"", ENGLISH_CONFIGURATION, new ArrayList<>())
        );
    }

    private static Stream<Arguments> provideShouldFindCorrectTweetsBySinglePlainQueryInDescriptionWithNumWord() {
        return Stream.of(
                Arguments.of("ESF2400OK", List.of(5L)),
                Arguments.of("SKS51E32EU", List.of(6L))
        );
    }

    @DisplayName("should return all ids when searching by query for specific configuration' for plainto_tsquery function")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectTweetsBySinglePlainQueryInDescription")
    public void shouldFindCorrectTweetsBySinglePlainQueryInDescription(String phrase, String configuration, List<Long> expectedIds) {

        // when
        List<TweetWithLocale> results = tested.findBySinglePlainQueryInDescriptionForConfiguration(phrase, configuration);

        // then
        assertThat(results).hasSize(expectedIds.size());
        assertThat(results.stream().map(TweetWithLocale::getId).collect(toSet())).containsAll(expectedIds);
    }

    @DisplayName("should return all ids when searching by query for specific configuration' for websearch_to_tsquery function")
    @ParameterizedTest
    @MethodSource({"provideShouldFindCorrectTweetsBySinglePlainQueryInDescription", "provideShouldFindCorrectTweetsByWebSearchToTSQueryInDescription"})
    public void shouldFindCorrectTweetsByWebSearchToTSQueryInDescription(String phrase, String configuration, List<Long> expectedIds) {
        assumeTrue(postgresVersion.getMajor() >= 11, "Test ignored because the 'websearch_to_tsquery' function was added in version 10 of Postgres");

        // when
        List<TweetWithLocale> results = tested.findCorrectTweetsByWebSearchToTSQueryInDescription(phrase, configuration);

        // then
        assertThat(results).hasSize(expectedIds.size());
        assertThat(results.stream().map(TweetWithLocale::getId).collect(toSet())).containsAll(expectedIds);
    }

    @DisplayName("should return all ids when searching by query with word that has letters and digits for specific configuration' for plainto_tsquery function")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectTweetsBySinglePlainQueryInDescriptionWithNumWord")
    public void shouldFindCorrectTweetsBySinglePlainQueryInDescriptionWithNumWord(String phrase, List<Long> expectedIds) {

        // when
        List<TweetWithLocale> results = tested.findBySinglePlainQueryInDescriptionForConfiguration(phrase, POLISH_EXTENDED_CONFIGURATION);

        // then
        assertThat(results).hasSize(expectedIds.size());
        assertThat(results.stream().map(TweetWithLocale::getId).collect(toSet())).containsAll(expectedIds);
    }

    @DisplayName("should return empty ids list when searching by query with word that has letters and digits for specific configuration that do not handles the numword for plainto_tsquery function")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectTweetsBySinglePlainQueryInDescriptionWithNumWord")
    public void shouldNotFindCorrectTweetsBySinglePlainQueryInDescriptionWithNumWord(String phrase) {

        // when
        List<TweetWithLocale> results = tested.findBySinglePlainQueryInDescriptionForConfiguration(phrase, POLISH_CONFIGURATION);

        // then
        assertThat(results).isEmpty();
    }

}
