package com.github.starnowski.posjsonhelper.hibernate6.demo.dao;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.Hibernate6JsonUpdateStatementBuilder;
import com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPath;
import com.github.starnowski.posjsonhelper.hibernate6.demo.model.Item;
import com.github.starnowski.posjsonhelper.hibernate6.functions.JsonbSetFunction;
import com.github.starnowski.posjsonhelper.hibernate6.functions.RemoveJsonValuesFromJsonArrayFunction;
import com.github.starnowski.posjsonhelper.hibernate6.operators.ConcatenateJsonbOperator;
import com.github.starnowski.posjsonhelper.hibernate6.operators.DeleteJsonbBySpecifiedPathOperator;
import com.github.starnowski.posjsonhelper.json.core.sql.JsonTextArrayBuilder;
import com.github.starnowski.posjsonhelper.json.core.sql.JsonUpdateStatementConfiguration;
import com.github.starnowski.posjsonhelper.test.utils.NumericComparator;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.tree.SqmTypedNode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import static org.json.JSONObject.quote;
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

    private static final Set<Long> ALL_ITEMS_IDS = new HashSet<>(asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L, 21L, 22L, 23L));
    @Autowired
    private ItemDao tested;
    @Autowired
    private HibernateContext hibernateContext;
    @Autowired
    private EntityManager entityManager;

    private static Stream<Arguments> provideShouldReturnSingleCorrectIdExpectedIdWhenSearchingByAllMatchingTags() {
        return Stream.of(
                Arguments.of(asList("TAG1", "TAG2"), 1L),
                Arguments.of(List.of("TAG11"), 1L),
                Arguments.of(List.of("TAG12"), 1L)
        );
    }

    private static Stream<Arguments> provideShouldReturnCorrectIdExpectedIdsWhenSearchingByAllMatchingTags() {
        return Stream.of(
                Arguments.of(asList("TAG1", "TAG2"), new HashSet<>(List.of(1L))),
                Arguments.of(List.of("TAG3"), new HashSet<>(asList(3L, 2L))),
                Arguments.of(asList("TAG21", "TAG22"), new HashSet<>(asList(1L, 4L)))
        );
    }

    private static Stream<Arguments> provideShouldReturnCorrectIdExceptExpectedIdsWhenSearchingItemThatDoNotMatchByAllMatchingTags() {
        return Stream.of(
                Arguments.of(asList("TAG1", "TAG2"), new HashSet<>(List.of(1L))),
                Arguments.of(List.of("TAG3"), new HashSet<>(asList(3L, 2L))),
                Arguments.of(asList("TAG21", "TAG22"), new HashSet<>(asList(1L, 4L)))
        );
    }

    private static Stream<Arguments> provideShouldReturnCorrectIdExpectedIdsWhenSearchingByAnyMatchingTags() {
        return Stream.of(
                Arguments.of(asList("TAG1", "TAG2"), new HashSet<>(asList(1L, 3L))),
                Arguments.of(List.of("TAG3"), new HashSet<>(asList(3L, 2L))),
                Arguments.of(asList("TAG1", "TAG32"), new HashSet<>(asList(1L, 3L, 5L)))
        );
    }

    private static Stream<Arguments> provideShouldReturnCorrectIdExpectedIdsWhenSearchingByOperatorToCompareDoubleValue() {
        return Stream.of(
                Arguments.of(NumericComparator.EQ, -1137.98, new HashSet<>(List.of(11L))),
                Arguments.of(NumericComparator.EQ, 353.01, new HashSet<>(List.of(10L))),
                Arguments.of(NumericComparator.GE, -1137.98, new HashSet<>(asList(10L, 11L, 12L))),
                Arguments.of(NumericComparator.GT, -1137.98, new HashSet<>(asList(10L, 12L))),
                Arguments.of(NumericComparator.LE, -1137.98, new HashSet<>(List.of(11L))),
                Arguments.of(NumericComparator.LT, -1137.98, new HashSet<>(List.of())),
                Arguments.of(NumericComparator.LT, 20490.04, new HashSet<>(asList(10L, 11L)))
        );
    }

    private static Stream<Arguments> provideShouldReturnCorrectIdExpectedIdsWhenSearchingByInOperatorToCompareEnumValue() {
        return Stream.of(
                Arguments.of(asList("SUPER", "USER"), new HashSet<>(asList(14L, 13L))),
                Arguments.of(List.of("SUPER"), new HashSet<>(List.of(13L))),
                Arguments.of(asList("ANONYMOUS", "SUPER"), new HashSet<>(asList(13L, 15L)))
        );
    }

    private static Stream<Arguments> provideShouldReturnCorrectIdExpectedIdsWhenSearchingByLIKEOperatorWithExpression() {
        return Stream.of(
                Arguments.of("this is full sentence", new HashSet<>(List.of(16L))),
                Arguments.of("this is ", new HashSet<>(List.of())),
                Arguments.of("this is %", new HashSet<>(asList(16L, 17L))),
                Arguments.of("end of", new HashSet<>(List.of())),
                Arguments.of("end of%", new HashSet<>(List.of())),
                Arguments.of("%end of%", new HashSet<>(List.of(18L)))
        );
    }

    private static Stream<Arguments> provideShouldReturnCorrectIdWhenSearchingByAnyMatchingTagsInInnerElements() {
        return Stream.of(
                Arguments.of(List.of("dog"), new HashSet<>(asList(19L, 21L, 23L))),
                Arguments.of(List.of("cat"), new HashSet<>(asList(20L, 21L))),
                Arguments.of(List.of("hamster"), new HashSet<>(List.of(22L))),
                Arguments.of(asList("hamster", "cat"), new HashSet<>(asList(20L, 21L, 22L)))
        );
    }

    private static Stream<Arguments> provideShouldReplaceJsonPropertyWithSpecificValueToInnerElement() {
        return Stream.of(
                Arguments.of(19L, "birthday", "1970-01-01")
        );
    }

    private static Stream<Arguments> provideShouldSetJsonPropertyWithSpecificValueToInnerElement() {
        return Stream.of(
                Arguments.of(19L, "birthday", "1970-01-01", "{\"child\": {\"pets\" : [\"dog\"], \"birthday\": \"1970-01-01\"}}")
        );
    }

    private static Stream<Arguments> provideShouldSetMultipleJsonPropertyWithSpecificValueToInnerElement() {
        return Stream.of(
                Arguments.of(19L, List.of(new JsonBSetTestPair(new JsonTextArrayBuilder().append("child").append("birthday"), quote("1970-01-01"))), "{\"child\": {\"pets\" : [\"dog\"], \"birthday\": \"1970-01-01\"}}"),
                Arguments.of(20L, List.of(new JsonBSetTestPair(new JsonTextArrayBuilder().append("child").append("pets").append(1), quote("monkey"))), "{\"child\": {\"pets\" : [\"cat\", \"monkey\"]}}"),
                Arguments.of(19L, Arrays.asList(new JsonBSetTestPair(new JsonTextArrayBuilder().append("child").append("birthday"), quote("2021-11-23")),
                        new JsonBSetTestPair(new JsonTextArrayBuilder().append("child").append("pets"), "[\"cat\"]")), "{\"child\": {\"pets\" : [\"cat\"], \"birthday\": \"2021-11-23\"}}")
        );
    }

    private static Stream<Arguments> provideShouldSetMultipleJsonPropertyWithSpecificValueToInnerElementWithNewFields() {
        return Stream.of(
                Arguments.of(19L, Arrays.asList(new JsonBSetTestPair(new JsonTextArrayBuilder().append("child").append("birthday"), quote("2021-11-23")),
                                new JsonBSetTestPair(new JsonTextArrayBuilder().append("child").append("pets"), "[\"cat\"]"),
                                new JsonBSetTestPair(new JsonTextArrayBuilder().append("parents").append(0), "{\"type\":\"mom\", \"name\":\"simone\"}"),
                                new JsonBSetTestPair(new JsonTextArrayBuilder().append("parents"), "[]")
                        )
                        , "{\"child\": {\"pets\" : [\"cat\"], \"birthday\": \"2021-11-23\"}, \"parents\": [{\"type\":\"mom\", \"name\":\"simone\"}]}")
        );
    }

    private static Stream<Arguments> provideShouldDeleteJsonPropertyForSpecificPath() {
        return Stream.of(
                Arguments.of(19L, "pets", "{\"child\": {}}")
        );
    }

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

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should add json property with specific value to inner element")
    @ParameterizedTest
    @MethodSource("provideShouldReplaceJsonPropertyWithSpecificValueToInnerElement")
    public void shouldReplaceJsonPropertyWithSpecificValueToInnerElement(Long itemId, String property, String value) throws JSONException {
        // when
        tested.updateJsonPropertyForItem(itemId, property, value);

        // then
        Item item = tested.findById(itemId);
        assertThat((String) JsonPath.read(item.getJsonbContent(), "$.child." + property)).isEqualTo(value);
        JSONObject jsonObject = new JSONObject().put(property, value);
        DocumentContext document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$.child"));
        assertThat(document.jsonString()).isEqualTo(jsonObject.toString());
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should add json property with specific value to inner element - demo documentation")
    @Test
    @Transactional
    public void shouldReplaceJsonPropertyWithSpecificValueToInnerElementForDemoPurpose() throws JSONException {
        // GIVEN
        Long itemId = 19L;
        String property = "birthday";
        String value = "1970-01-01";

        // WHEN
        CriteriaUpdate<Item> criteriaUpdate = entityManager.getCriteriaBuilder().createCriteriaUpdate(Item.class);
        Root<Item> root = criteriaUpdate.from(Item.class);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("child", new JSONObject());
        jsonObject.getJSONObject("child").put(property, value);
        criteriaUpdate.set("jsonbContent", new ConcatenateJsonbOperator((NodeBuilder) entityManager.getCriteriaBuilder(), root.get("jsonbContent"), jsonObject.toString(), hibernateContext));

        criteriaUpdate.where(entityManager.getCriteriaBuilder().equal(root.get("id"), itemId));

        entityManager.createQuery(criteriaUpdate).executeUpdate();

        // THEN
        Item item = tested.findById(itemId);
        assertThat((String) JsonPath.read(item.getJsonbContent(), "$.child." + property)).isEqualTo(value);
        JSONObject expectedJsonObject = new JSONObject().put(property, value);
        DocumentContext document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$.child"));
        assertThat(document.jsonString()).isEqualTo(expectedJsonObject.toString());
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should add json property with specific value to inner element by hql")
    @ParameterizedTest
    @MethodSource("provideShouldReplaceJsonPropertyWithSpecificValueToInnerElement")
    public void shouldReplaceJsonPropertyWithSpecificValueToInnerElementByHQL(Long itemId, String property, String value) throws JSONException {
        // when
        tested.updateJsonPropertyForItemByHQL(itemId, property, value);

        // then
        Item item = tested.findById(itemId);
        assertThat((String) JsonPath.read(item.getJsonbContent(), "$.child." + property)).isEqualTo(value);
        JSONObject jsonObject = new JSONObject().put(property, value);
        DocumentContext document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$.child"));
        assertThat(document.jsonString()).isEqualTo(jsonObject.toString());
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should add json property with specific value to inner element")
    @ParameterizedTest
    @MethodSource("provideShouldSetJsonPropertyWithSpecificValueToInnerElement")
    public void shouldSetJsonPropertyWithSpecificValueToInnerElement(Long itemId, String property, String value, String expectedJson) throws JSONException {
        // when
        tested.updateJsonBySettingPropertyForItem(itemId, property, value);

        // then
        Item item = tested.findById(itemId);
        assertThat((String) JsonPath.read(item.getJsonbContent(), "$.child." + property)).isEqualTo(value);
        JSONObject jsonObject = new JSONObject(expectedJson);
        DocumentContext document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$"));
        assertThat(document.jsonString()).isEqualTo(jsonObject.toString());
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should add json property with specific value to inner element - documentation demo")
    @Test
    @Transactional
    public void shouldSetJsonPropertyWithSpecificValueToInnerElementForDemoPurpose() throws JSONException {
        // GIVEN
        Long itemId = 19L;
        String property = "birthday";
        String value = "1970-01-01";
        String expectedJson = "{\"child\": {\"pets\" : [\"dog\"], \"birthday\": \"1970-01-01\"}}";
        // when
        CriteriaUpdate<Item> criteriaUpdate = entityManager.getCriteriaBuilder().createCriteriaUpdate(Item.class);
        Root<Item> root = criteriaUpdate.from(Item.class);

        // Set the property you want to update and the new value
        criteriaUpdate.set("jsonbContent", new JsonbSetFunction((NodeBuilder) entityManager.getCriteriaBuilder(), root.get("jsonbContent"), new JsonTextArrayBuilder().append("child").append(property).build().toString(), JSONObject.quote(value), hibernateContext));

        // Add any conditions to restrict which entities will be updated
        criteriaUpdate.where(entityManager.getCriteriaBuilder().equal(root.get("id"), itemId));

        // Execute the update
        entityManager.createQuery(criteriaUpdate).executeUpdate();

        // then
        Item item = tested.findById(itemId);
        assertThat((String) JsonPath.read(item.getJsonbContent(), "$.child." + property)).isEqualTo(value);
        JSONObject jsonObject = new JSONObject(expectedJson);
        DocumentContext document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$"));
        assertThat(document.jsonString()).isEqualTo(jsonObject.toString());
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should delete json property for specific path to inner element")
    @ParameterizedTest
    @MethodSource("provideShouldDeleteJsonPropertyForSpecificPath")
    public void shouldDeleteJsonPropertyForSpecificPathToInnerElement(Long itemId, String property, String expectedJson) throws JSONException {
        // when
        tested.updateJsonByDeletingSpecificPropertyForItem(itemId, property);

        // then
        Item item = tested.findById(itemId);
        JSONObject jsonObject = new JSONObject(expectedJson);
        DocumentContext document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$"));
        assertThat(document.jsonString()).isEqualTo(jsonObject.toString());
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should delete json property for specific path to inner element by hql")
    @ParameterizedTest
    @MethodSource("provideShouldDeleteJsonPropertyForSpecificPath")
    public void shouldDeleteJsonPropertyForSpecificPathToInnerElementByHql(Long itemId, String property, String expectedJson) throws JSONException {
        // when
        tested.updateJsonByDeletingSpecificPropertyForItemByHql(itemId, property);

        // then
        Item item = tested.findById(itemId);
        JSONObject jsonObject = new JSONObject(expectedJson);
        DocumentContext document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$"));
        assertThat(document.jsonString()).isEqualTo(jsonObject.toString());
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should delete json property for specific path to inner element - for demo purpose")
    @Test
    @Transactional
    public void shouldDeleteJsonPropertyForSpecificPathToInnerElementForDemoPurpose() throws JSONException {
        // GIVEN
        Item item = tested.findById(19L);
        JSONObject jsonObject = new JSONObject("{\"child\": {\"pets\" : [\"dog\"]}}");
        DocumentContext document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$"));
        assertThat(document.jsonString()).isEqualTo(jsonObject.toString());

        // WHEN
        CriteriaUpdate<Item> criteriaUpdate = entityManager.getCriteriaBuilder().createCriteriaUpdate(Item.class);
        Root<Item> root = criteriaUpdate.from(Item.class);

        // Set the property you want to update and the new value
        criteriaUpdate.set("jsonbContent", new DeleteJsonbBySpecifiedPathOperator((NodeBuilder) entityManager.getCriteriaBuilder(), root.get("jsonbContent"), new JsonTextArrayBuilder().append("child").append("pets").build().toString(), hibernateContext));

        // Add any conditions to restrict which entities will be updated
        criteriaUpdate.where(entityManager.getCriteriaBuilder().equal(root.get("id"), 19L));

        // Execute the update
        entityManager.createQuery(criteriaUpdate).executeUpdate();

        // THEN
        entityManager.refresh(item);
        jsonObject = new JSONObject("{\"child\": {}}");
        document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$"));
        assertThat(document.jsonString()).isEqualTo(jsonObject.toString());
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should add json property with specific value to inner element")
    @ParameterizedTest
    @MethodSource("provideShouldSetJsonPropertyWithSpecificValueToInnerElement")
    public void shouldSetJsonPropertyWithSpecificValueToInnerElementByHQL(Long itemId, String property, String value, String expectedJson) throws JSONException {
        // when
        tested.updateJsonBySettingPropertyForItemByHQL(itemId, property, value);

        // then
        Item item = tested.findById(itemId);
        assertThat((String) JsonPath.read(item.getJsonbContent(), "$.child." + property)).isEqualTo(value);
        JSONObject jsonObject = new JSONObject(expectedJson);
        DocumentContext document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$"));
        assertThat(document.jsonString()).isEqualTo(jsonObject.toString());
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should add json property with specific value to inner element")
    @ParameterizedTest
    @MethodSource("provideShouldSetMultipleJsonPropertyWithSpecificValueToInnerElement")
    public void shouldSetMultipleJsonPropertyWithSpecificValueToInnerElement(Long itemId, List<JsonBSetTestPair> pairs, String expectedJson) throws JSONException {
        // when
        tested.updateJsonBySettingPropertyForItem(itemId, pairs);

        // then
        Item item = tested.findById(itemId);
        JSONObject jsonObject = new JSONObject(expectedJson);
        DocumentContext document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$"));
        assertThat(document.jsonString()).isEqualTo(jsonObject.toString());
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should add json property with specific value to inner element by using Hibernate6JsonUpdateStatementBuilder")
    @ParameterizedTest
    @MethodSource({"provideShouldSetMultipleJsonPropertyWithSpecificValueToInnerElement", "provideShouldSetMultipleJsonPropertyWithSpecificValueToInnerElementWithNewFields"})
    public void shouldSetMultipleJsonPropertyWithSpecificValueToInnerElementByUsingHibernate6JsonUpdateStatementBuilder(Long itemId, List<JsonBSetTestPair> pairs, String expectedJson) throws JSONException {
        // when
        tested.updateJsonBySettingPropertyForItemWithHibernate6JsonUpdateStatementBuilder(itemId, pairs);

        // then
        Item item = tested.findById(itemId);
        JSONObject jsonObject = new JSONObject(expectedJson);
        DocumentContext document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$"));
        assertThat(document.jsonString()).isEqualTo(jsonObject.toString());
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @Test
    @Transactional
    @DisplayName("should add json property with specific value to inner element by using Hibernate6JsonUpdateStatementBuilder - documentation demo")
    public void shouldSetMultipleJsonPropertiesByUsingHibernate6JsonUpdateStatementBuilder() throws JSONException {
        // GIVEN
        Item item = tested.findById(23L);
        DocumentContext document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$"));
        assertThat(document.jsonString()).isEqualTo("{\"child\":{\"pets\":[\"dog\"]},\"inventory\":[\"mask\",\"fins\"],\"nicknames\":{\"school\":\"bambo\",\"childhood\":\"bob\"}}");
        CriteriaUpdate<Item> criteriaUpdate = entityManager.getCriteriaBuilder().createCriteriaUpdate(Item.class);
        Root<Item> root = criteriaUpdate.from(Item.class);

        Hibernate6JsonUpdateStatementBuilder hibernate6JsonUpdateStatementBuilder = new Hibernate6JsonUpdateStatementBuilder(root.get("jsonbContent"), (NodeBuilder) entityManager.getCriteriaBuilder(), hibernateContext);
        hibernate6JsonUpdateStatementBuilder.appendJsonbSet(new JsonTextArrayBuilder().append("child").append("birthday").build(), quote("2021-11-23"));
        hibernate6JsonUpdateStatementBuilder.appendJsonbSet(new JsonTextArrayBuilder().append("child").append("pets").build(), "[\"cat\"]");
        hibernate6JsonUpdateStatementBuilder.appendDeleteBySpecificPath(new JsonTextArrayBuilder().append("inventory").append("0").build());
        hibernate6JsonUpdateStatementBuilder.appendJsonbSet(new JsonTextArrayBuilder().append("parents").append(0).build(), "{\"type\":\"mom\", \"name\":\"simone\"}");
        hibernate6JsonUpdateStatementBuilder.appendJsonbSet(new JsonTextArrayBuilder().append("parents").build(), "[]");
        hibernate6JsonUpdateStatementBuilder.appendDeleteBySpecificPath(new JsonTextArrayBuilder().append("nicknames").append("childhood").build());

        // Set the property you want to update and the new value
        criteriaUpdate.set("jsonbContent", hibernate6JsonUpdateStatementBuilder.build());

        // Add any conditions to restrict which entities will be updated
        criteriaUpdate.where(entityManager.getCriteriaBuilder().equal(root.get("id"), 23L));

        // WHEN
        entityManager.createQuery(criteriaUpdate).executeUpdate();

        // THEN
        entityManager.refresh(item);
        document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$"));
        assertThat(document.jsonString()).isEqualTo("{\"child\":{\"pets\":[\"cat\"],\"birthday\":\"2021-11-23\"},\"parents\":[{\"name\":\"simone\",\"type\":\"mom\"}],\"inventory\":[\"fins\"],\"nicknames\":{\"school\":\"bambo\"}}");
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @Test
    @Transactional
    @DisplayName("should add json property with specific value to inner element by using Hibernate6JsonUpdateStatementBuilder - documentation demo")
    public void shouldSetJsonArrayWithNewValueWithUpdateStatementForDemo() throws JSONException {
        // GIVEN
        Item item = tested.findById(21L);
        DocumentContext document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$"));
        assertThat(document.jsonString()).isEqualTo("{\"child\":{\"pets\":[\"dog\",\"cat\"]}}");
        CriteriaUpdate<Item> criteriaUpdate = entityManager.getCriteriaBuilder().createCriteriaUpdate(Item.class);
        Root<Item> root = criteriaUpdate.from(Item.class);

        Hibernate6JsonUpdateStatementBuilder<Object, JsonArrayOperations> hibernate6JsonUpdateStatementBuilder = new Hibernate6JsonUpdateStatementBuilder(root.get("jsonbContent"), (NodeBuilder) entityManager.getCriteriaBuilder(), hibernateContext);
        hibernate6JsonUpdateStatementBuilder.appendJsonbSet(new JsonTextArrayBuilder().append("child").append("pets").build(), null, new JsonArrayOperations(Arrays.asList("dog", "ant"), Arrays.asList("lion", "dolphin")));
        hibernate6JsonUpdateStatementBuilder.withJsonbSetFunctionFactory(new Hibernate6JsonUpdateStatementBuilder.JsonbSetFunctionFactory<Object, JsonArrayOperations>() {

            public JsonbSetFunction build(NodeBuilder nodeBuilder, Path<Object> rootPath, JsonUpdateStatementConfiguration.JsonUpdateStatementOperation<JsonArrayOperations> operation, HibernateContext hibernateContext) {
//                new JsonbSetFunction(nodeBuilder, rootPath, operation.getJsonTextArray().toString(), operation.getValue(), hibernateContext);
                JSONArray toAddJSONArray = new JSONArray(operation.getCustomValue().getToAdd());
                ConcatenateJsonbOperator concatenateOperator = new ConcatenateJsonbOperator(nodeBuilder, new JsonBExtractPath(rootPath, nodeBuilder, operation.getJsonTextArray().getPath().stream().map(ob -> ob.toString()).collect(Collectors.toList())), toAddJSONArray.toString(), hibernateContext);
                JSONArray toRemoveJSONArray = new JSONArray(operation.getCustomValue().getToDelete());
                RemoveJsonValuesFromJsonArrayFunction deleteOperator = new RemoveJsonValuesFromJsonArrayFunction(nodeBuilder, concatenateOperator, toRemoveJSONArray.toString(), hibernateContext);
                return new JsonbSetFunction(nodeBuilder, (SqmTypedNode) rootPath, operation.getJsonTextArray().toString(), deleteOperator, hibernateContext);
            }
        });
        // Set the property you want to update and the new value
        criteriaUpdate.set("jsonbContent", hibernate6JsonUpdateStatementBuilder.build());

        // Add any conditions to restrict which entities will be updated
        criteriaUpdate.where(entityManager.getCriteriaBuilder().equal(root.get("id"), 21L));

        // WHEN
        entityManager.createQuery(criteriaUpdate).executeUpdate();

        // THEN
        entityManager.refresh(item);
        document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$"));
        assertThat(document.jsonString()).isEqualTo("{\"child\":{\"pets\":[\"cat\",\"lion\",\"dolphin\"]}}");
    }

    private static class JsonArrayOperations {

        private final List<String> toDelete;
        private final List<String> toAdd;

        public JsonArrayOperations(List<String> toDelete, List<String> toAdd) {
            this.toDelete = toDelete;
            this.toAdd = toAdd;
        }

        public List<String> getToDelete() {
            return toDelete;
        }

        public List<String> getToAdd() {
            return toAdd;
        }
    }

    public static class JsonBSetTestPair {
        private final JsonTextArrayBuilder jsonTextArrayBuilder;
        private final String jsonValue;

        public JsonBSetTestPair(JsonTextArrayBuilder jsonTextArrayBuilder, String jsonValue) {
            this.jsonTextArrayBuilder = jsonTextArrayBuilder;
            this.jsonValue = jsonValue;
        }

        public JsonTextArrayBuilder getJsonbSetFunctionJsonPathBuilder() {
            return jsonTextArrayBuilder;
        }

        @Override
        public String toString() {
            return "JsonBSetTestPair{" +
                    "jsonTextArrayBuilder=" + jsonTextArrayBuilder.buildString() +
                    ", jsonValue='" + jsonValue + '\'' +
                    '}';
        }

        public String getJsonValue() {
            return jsonValue;
        }
    }
}