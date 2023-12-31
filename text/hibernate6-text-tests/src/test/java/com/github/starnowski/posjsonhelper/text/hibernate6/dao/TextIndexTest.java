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

import java.util.List;
import java.util.stream.Stream;

import static com.github.starnowski.posjsonhelper.text.hibernate6.Application.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
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


}
