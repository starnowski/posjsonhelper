package com.github.starnowski.posjsonhelper.text.hibernate6.dao;

import com.github.starnowski.posjsonhelper.text.hibernate6.model.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.List;
import java.util.stream.Stream;

import static com.github.starnowski.posjsonhelper.text.hibernate6.Application.CLEAR_DATABASE_SCRIPT_PATH;
import static com.github.starnowski.posjsonhelper.text.hibernate6.Application.ITEMS_SCRIPT_PATH;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

class ItemDaoTest extends AbstractItTest {

    @Autowired
    private ItemDao itemDao;


    private static Stream<Arguments> provideShouldFindCorrectItemWithOrderBasedOnPhrase() {
        return Stream.of(
                Arguments.of("wing", true, asList(1L, 2L)),
                Arguments.of("wing", false, asList(2L, 1L))
        );
    }

    private static Stream<Arguments> provideShouldFindCorrectItemWithOrderBasedOnPhraseWithWeights() {
        return Stream.of(
                // Reserved weight values - D - 1.0 the top rank, A - 0.2 the lowest rank
                Arguments.of("wing", true, asList(2L, 1L), new double[]{1.0, 0.8, 0.4, 0.2}),
                // Reserved weight values - D - 1.0 the top rank, A - 0.2 the lowest rank
                Arguments.of("wing", false, asList(1L, 2L), new double[]{1.0, 0.8, 0.4, 0.2})
        );
    }


    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should find correct item with order based on phrase")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectItemWithOrderBasedOnPhrase")
    public void shouldFindCorrectItemWithOrderBasedOnPhraseRankedByTsRank(String phrase, boolean ascSort, List<Long> expectedIds) {
        assumeTrue(postgresVersion.getMajor() >= 11, "Test ignored because the 'websearch_to_tsquery' function was added in version 10 of Postgres");

        // WHEN
        List<Item> items = itemDao.findItemsByWebSearchToTSQuerySortedByTsRank(phrase, ascSort);

        // THEN
        assertEquals(expectedIds, items.stream().map(Item::getId).toList());
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should find correct item with order based on phrase")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectItemWithOrderBasedOnPhrase")
    public void shouldFindCorrectItemWithOrderBasedOnPhraseRankedByTsRankInHQL(String phrase, boolean ascSort, List<Long> expectedIds) {
        assumeTrue(postgresVersion.getMajor() >= 11, "Test ignored because the 'websearch_to_tsquery' function was added in version 10 of Postgres");

        // WHEN
        List<Item> items = itemDao.findItemsByWebSearchToTSQuerySortedByTsRankInHQL(phrase, ascSort);

        // THEN
        assertEquals(expectedIds, items.stream().map(Item::getId).toList());
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should find correct item with order based on phrase")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectItemWithOrderBasedOnPhrase")
    public void shouldFindCorrectItemWithOrderBasedOnPhraseRankedByTsRankCd(String phrase, boolean ascSort, List<Long> expectedIds) {
        assumeTrue(postgresVersion.getMajor() >= 11, "Test ignored because the 'websearch_to_tsquery' function was added in version 10 of Postgres");

        // WHEN
        List<Item> items = itemDao.findItemsByWebSearchToTSQuerySortedByTsRankCd(phrase, ascSort);

        // THEN
        assertEquals(expectedIds, items.stream().map(Item::getId).toList());
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should find correct item with order based on phrase")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectItemWithOrderBasedOnPhrase")
    public void shouldFindCorrectItemWithOrderBasedOnPhraseRankedByTsRankCdInHQL(String phrase, boolean ascSort, List<Long> expectedIds) {
        assumeTrue(postgresVersion.getMajor() >= 11, "Test ignored because the 'websearch_to_tsquery' function was added in version 10 of Postgres");

        // WHEN
        List<Item> items = itemDao.findItemsByWebSearchToTSQuerySortedByTsRankCdInHQL(phrase, ascSort);

        // THEN
        assertEquals(expectedIds, items.stream().map(Item::getId).toList());
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should find correct item with order based on phrase and custom weights")
    @ParameterizedTest
    @MethodSource("provideShouldFindCorrectItemWithOrderBasedOnPhraseWithWeights")
    public void shouldFindCorrectItemWithOrderBasedOnPhraseRankedByTsRankWithCustomWeights(String phrase, boolean ascSort, List<Long> expectedIds, double[] weights) {
        assumeTrue(postgresVersion.getMajor() >= 11, "Test ignored because the 'websearch_to_tsquery' function was added in version 10 of Postgres");

        // WHEN
        List<Item> items = itemDao.findItemsByWebSearchToTSQuerySortedByTsRankWithCustomW(phrase, ascSort, weights);

        // THEN
        assertEquals(expectedIds, items.stream().map(Item::getId).toList());
    }
}