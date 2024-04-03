package com.github.starnowski.posjsonhelper.hibernate6.demo.dao;

import com.github.starnowski.posjsonhelper.hibernate6.demo.model.Item;
import com.github.starnowski.posjsonhelper.test.utils.NumericComparator;
import com.jayway.jsonpath.JsonPath;
import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.starnowski.posjsonhelper.hibernate6.demo.Application.CLEAR_DATABASE_SCRIPT_PATH;
import static com.github.starnowski.posjsonhelper.hibernate6.demo.Application.ITEMS_SCRIPT_PATH;
import static java.util.Arrays.asList;
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
public abstract class AbstractItemDaoTest {

    private static final Set<Long> ALL_ITEMS_IDS = new HashSet<>(asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L, 21L, 22L));

    private static Stream<Arguments> provideShouldReturnSingleCorrectIdExpectedIdWhenSearchingByAllMatchingTags() {
        return Stream.of(
                Arguments.of(asList("TAG1", "TAG2"), 1L),
                Arguments.of(List.of("TAG11"), 1L),
                Arguments.of(List.of("TAG12"), 1L)
        );
    }


    @Autowired
    private ItemDao tested;

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return single correct id #expectedId when searching by all matching tags [#tags]")
    @ParameterizedTest
    @MethodSource("provideShouldReturnSingleCorrectIdExpectedIdWhenSearchingByAllMatchingTags")
    public void shouldReturnSingleCorrectIdExpectedIdWhenSearchingByAllMatchingTags(List<String> tags, Long expectedId) {

        // when
        List<Item> results = tested.findAllByAllMatchingTags(new HashSet<>(tags));

        // then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getId()).isEqualTo(expectedId);
    }

    private static Stream<Arguments> provideShouldReturnCorrectIdExpectedIdsWhenSearchingByAllMatchingTags() {
        return Stream.of(
                Arguments.of(asList("TAG1", "TAG2"), new HashSet<>(asList(1L))),
                Arguments.of(asList("TAG3"), new HashSet<>(asList(3L, 2L))),
                Arguments.of(asList("TAG21", "TAG22"), new HashSet<>(asList(1L, 4L)))
        );
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return correct id #expectedIds when searching by all matching tags [#tags]")
    @ParameterizedTest
    @MethodSource("provideShouldReturnCorrectIdExpectedIdsWhenSearchingByAllMatchingTags")
    public void shouldReturnCorrectIdExpectedIdsWhenSearchingByAllMatchingTags(List<String> tags, Set<Long> expectedIds) {

        // when
        List<Item> results = tested.findAllByAllMatchingTags(new HashSet<>(tags));

        // then
        assertThat(results).hasSize(expectedIds.size());
        assertThat(results.stream().map(r -> r.getId()).collect(Collectors.toSet())).isEqualTo(expectedIds);
    }

    private static Stream<Arguments> provideShouldReturnCorrectIdExceptExpectedIdsWhenSearchingItemThatDoNotMatchByAllMatchingTags() {
        return Stream.of(
                Arguments.of(asList("TAG1", "TAG2"), new HashSet<>(asList(1L))),
                Arguments.of(asList("TAG3"), new HashSet<>(asList(3L, 2L))),
                Arguments.of(asList("TAG21", "TAG22"), new HashSet<>(asList(1L, 4L)))
        );
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return correct id except #expectedIds when searching item that do not match by all matching tags [#tags]")
    @ParameterizedTest
    @MethodSource("provideShouldReturnCorrectIdExceptExpectedIdsWhenSearchingItemThatDoNotMatchByAllMatchingTags")
    public void shouldReturnCorrectIdExceptExpectedIdsWhenSearchingItemThatDoNotMatchByAllMatchingTags(List<String> tags, Set<Long> nonIncludedIds) {

        // when
        List<Item> results = tested.findAllThatDoNotMatchByAllMatchingTags(new HashSet<>(tags));

        // then
        Set<Long> ids = results.stream().map(it -> it.getId()).collect(Collectors.toSet());
        Set<Long> expectedIds = ALL_ITEMS_IDS.stream().filter(id -> !nonIncludedIds.contains(id)).collect(Collectors.toSet());
        assertThat(ids).hasSize(expectedIds.size());
        assertThat(ids).isEqualTo(expectedIds);
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return correct id except #expectedIds when searching item that do not match by all matching tags [#tags] by using HQL query")
    @ParameterizedTest
    @MethodSource("provideShouldReturnCorrectIdExceptExpectedIdsWhenSearchingItemThatDoNotMatchByAllMatchingTags")
    public void shouldReturnCorrectIdExceptExpectedIdsWhenSearchingItemThatDoNotMatchByAllMatchingTagsWithHQLQuery(List<String> tags, Set<Long> nonIncludedIds) {

        // when
        List<Item> results = tested.findAllThatDoNotMatchByAllMatchingTagsWithHQLQuery(new HashSet<>(tags));

        // then
        Set<Long> ids = results.stream().map(it -> it.getId()).collect(Collectors.toSet());
        Set<Long> expectedIds = ALL_ITEMS_IDS.stream().filter(id -> !nonIncludedIds.contains(id)).collect(Collectors.toSet());
        assertThat(ids).hasSize(expectedIds.size());
        assertThat(ids).isEqualTo(expectedIds);
    }

    private static Stream<Arguments> provideShouldReturnCorrectIdExpectedIdsWhenSearchingByAnyMatchingTags() {
        return Stream.of(
                Arguments.of(asList("TAG1", "TAG2"), new HashSet<>(asList(1L, 3L))),
                Arguments.of(asList("TAG3"), new HashSet<>(asList(3L, 2L))),
                Arguments.of(asList("TAG1", "TAG32"), new HashSet<>(asList(1L, 3L, 5L)))
        );
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return correct id #expectedIds when searching by any matching tags [#tags]")
    @ParameterizedTest
    @MethodSource("provideShouldReturnCorrectIdExpectedIdsWhenSearchingByAnyMatchingTags")
    public void shouldReturnCorrectIdExpectedIdsWhenSearchingByAnyMatchingTags(List<String> tags, Set<Long> expectedIds) {

        // when
        List<Item> results = tested.findAllByAnyMatchingTags(new HashSet<>(tags));

        // then
        Set<Long> ids = results.stream().map(it -> it.getId()).collect(Collectors.toSet());
        assertThat(ids).hasSize(expectedIds.size());
        assertThat(ids).isEqualTo(expectedIds);
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return correct id #expectedIds when searching by any matching tags [#tags] with HQL query")
    @ParameterizedTest
    @MethodSource("provideShouldReturnCorrectIdExpectedIdsWhenSearchingByAnyMatchingTags")
    public void shouldReturnCorrectIdExpectedIdsWhenSearchingByAnyMatchingTagsWithHQL(List<String> tags, Set<Long> expectedIds) {

        // when
        List<Item> results = tested.findAllByAnyMatchingTagsWithHQL(new HashSet<>(tags));

        // then
        Set<Long> ids = results.stream().map(it -> it.getId()).collect(Collectors.toSet());
        assertThat(ids).hasSize(expectedIds.size());
        assertThat(ids).isEqualTo(expectedIds);
    }

    private static Stream<Arguments> provideShouldReturnCorrectIdExpectedIdsWhenSearchingByOperatorToCompareDoubleValue() {
        return Stream.of(
                Arguments.of(NumericComparator.EQ, -1137.98, new HashSet<>(asList(11L))),
                Arguments.of(NumericComparator.EQ, 353.01, new HashSet<>(asList(10L))),
                Arguments.of(NumericComparator.GE, -1137.98, new HashSet<>(asList(10L, 11L, 12L))),
                Arguments.of(NumericComparator.GT, -1137.98, new HashSet<>(asList(10L, 12L))),
                Arguments.of(NumericComparator.LE, -1137.98, new HashSet<>(asList(11L))),
                Arguments.of(NumericComparator.LT, -1137.98, new HashSet<>(asList())),
                Arguments.of(NumericComparator.LT, 20490.04, new HashSet<>(asList(10L, 11L)))
        );
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return correct id #expectedIds when searching by #operator operator to compare double value #value")
    @ParameterizedTest
    @MethodSource("provideShouldReturnCorrectIdExpectedIdsWhenSearchingByOperatorToCompareDoubleValue")
    public void shouldReturnCorrectIdExpectedIdsWhenSearchingByOperatorToCompareDoubleValue(NumericComparator operator, Double value, Set<Long> expectedIds) {

        // when
        List<Item> results = tested.findAllByNumericValue(BigDecimal.valueOf(value), operator);

        // then
        Set<Long> ids = results.stream().map(it -> it.getId()).collect(Collectors.toSet());
        assertThat(ids).hasSize(expectedIds.size());
        assertThat(ids).isEqualTo(expectedIds);
    }

    private static Stream<Arguments> provideShouldReturnCorrectIdExpectedIdsWhenSearchingByInOperatorToCompareEnumValue() {
        return Stream.of(
                Arguments.of(asList("SUPER", "USER"), new HashSet<>(asList(14L, 13L))),
                Arguments.of(asList("SUPER"), new HashSet<>(asList(13L))),
                Arguments.of(asList("ANONYMOUS", "SUPER"), new HashSet<>(asList(13L, 15L)))
        );
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return correct id #expectedIds when searching by IN operator to compare enum value #values")
    @ParameterizedTest
    @MethodSource("provideShouldReturnCorrectIdExpectedIdsWhenSearchingByInOperatorToCompareEnumValue")
    public void shouldReturnCorrectIdExpectedIdsWhenSearchingByInOperatorToCompareEnumValue(List<String> values, Set<Long> expectedIds) {

        // when
        List<Item> results = tested.findAllByStringThatMatchInValues(values);

        // then
        Set<Long> ids = results.stream().map(it -> it.getId()).collect(Collectors.toSet());
        assertThat(ids).hasSize(expectedIds.size());
        assertThat(ids).isEqualTo(expectedIds);
    }

    private static Stream<Arguments> provideShouldReturnCorrectIdExpectedIdsWhenSearchingByLIKEOperatorWithExpression() {
        return Stream.of(
                Arguments.of("this is full sentence", new HashSet<>(asList(16L))),
                Arguments.of("this is ", new HashSet<>(asList())),
                Arguments.of("this is %", new HashSet<>(asList(16L, 17L))),
                Arguments.of("end of", new HashSet<>(asList())),
                Arguments.of("end of%", new HashSet<>(asList())),
                Arguments.of("%end of%", new HashSet<>(asList(18L)))
        );
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return correct id #expectedIds when searching by LIKE operator with #expresion")
    @ParameterizedTest
    @MethodSource("provideShouldReturnCorrectIdExpectedIdsWhenSearchingByLIKEOperatorWithExpression")
    public void shouldReturnCorrectIdExpectedIdsWhenSearchingByLIKEOperatorWithExpression(String expression, Set<Long> expectedIds) {

        // when
        List<Item> results = tested.findAllByStringValueAndLikeOperator(expression);

        // then
        Set<Long> ids = results.stream().map(it -> it.getId()).collect(Collectors.toSet());
        assertThat(ids).hasSize(expectedIds.size());
        assertThat(ids).isEqualTo(expectedIds);
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return correct id #expectedIds when searching by LIKE operator with #expresion and with usage of HQL query")
    @ParameterizedTest
    @MethodSource("provideShouldReturnCorrectIdExpectedIdsWhenSearchingByLIKEOperatorWithExpression")
    public void shouldReturnCorrectIdExpectedIdsWhenSearchingByLIKEOperatorWithExpressionAndHQLQuery(String expression, Set<Long> expectedIds) {

        // when
        List<Item> results = tested.findAllByStringValueAndLikeOperatorWithHQLQuery(expression);

        // then
        Set<Long> ids = results.stream().map(it -> it.getId()).collect(Collectors.toSet());
        assertThat(ids).hasSize(expectedIds.size());
        assertThat(ids).isEqualTo(expectedIds);
    }

    private static Stream<Arguments> provideShouldReturnCorrectIdWhenSearchingByAnyMatchingTagsInInnerElements() {
        return Stream.of(
                Arguments.of(asList("dog"), new HashSet<>(asList(19L, 21L))),
                Arguments.of(asList("cat"), new HashSet<>(asList(20L, 21L))),
                Arguments.of(asList("hamster"), new HashSet<>(asList(22L))),
                Arguments.of(asList("hamster", "cat"), new HashSet<>(asList(20L, 21L, 22L)))
        );
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return correct id #expectedIds when searching by any matching tags [#tags] in inner elements")
    @ParameterizedTest
    @MethodSource("provideShouldReturnCorrectIdWhenSearchingByAnyMatchingTagsInInnerElements")
    public void shouldReturnCorrectIdWhenSearchingByAnyMatchingTagsInInnerElements(List<String> tags, Set<Long> expectedIds) {

        // when
        List<Item> results = tested.findAllByAnyMatchingTagsInInnerElement(new HashSet<String>(tags));

        // then
        Set<Long> ids = results.stream().map(it -> it.getId()).collect(Collectors.toSet());
        assertThat(ids).hasSize(expectedIds.size());
        assertThat(ids).isEqualTo(expectedIds);
    }

    private static Stream<Arguments> provideShouldAddJsonPropertyWithSpecificValueToInnerElement() {
        return Stream.of(
                Arguments.of(19L, "birthday", "1970-01-01")
        );
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should add json property with specific value to inner element")
    @ParameterizedTest
    @MethodSource("provideShouldAddJsonPropertyWithSpecificValueToInnerElement")
    public void shouldAddJsonPropertyWithSpecificValueToInnerElement(Long itemId, String property, String value) throws JSONException {
        // when
        tested.updateJsonPropertyForItem(itemId, property, value);

        // then
        Item item = tested.findById(itemId);
        assertThat((String)JsonPath.read(item.getJsonbContent(), "$.child." + property)).isEqualTo(value);
    }
}