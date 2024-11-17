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

import static com.github.starnowski.posjsonhelper.core.Constants.JSONB_SET_FUNCTION_NAME;
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
public abstract class AbstractHibernate6JsonUpdateStatementBuilderTest {

    private static final Set<Long> ALL_ITEMS_IDS = new HashSet<>(asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L, 21L, 22L, 23L, 24L));
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
    @Test
    @Transactional
    @DisplayName("should modify json array elements by removing and adding specific values with the udpate statement by using Hibernate6JsonUpdateStatementBuilder - documentation demo")
    public void shouldUpdateJsonObjectWithBuilderForDemo() {
        // GIVEN
        Item item = tested.findById(24L);
        DocumentContext document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$"));
        assertThat(document.jsonString()).isEqualTo("{\"child\":{\"pets\":[\"crab\",\"chameleon\"]},\"inventory\":[\"mask\",\"fins\",\"compass\"]}");
        CriteriaUpdate<Item> criteriaUpdate = entityManager.getCriteriaBuilder().createCriteriaUpdate(Item.class);
        Root<Item> root = criteriaUpdate.from(Item.class);

        Hibernate6JsonUpdateStatementBuilder hibernate6JsonUpdateStatementBuilder = new Hibernate6JsonUpdateStatementBuilder(root.get("jsonbContent"), (NodeBuilder) entityManager.getCriteriaBuilder(), hibernateContext)
                .appendAddArrayItems(new JsonTextArrayBuilder().append("child").append("pets").build(), Arrays.asList("lion", "dolphin"))
                .appendRemoveArrayItems(new JsonTextArrayBuilder().append("child").append("pets").build(), Arrays.asList("crab", "ant"))
                .appendJsonbSet(new JsonTextArrayBuilder().append("name").build(), JSONObject.quote("Simon"))
                .appendRemoveArrayItems(new JsonTextArrayBuilder().append("inventory").build(), Arrays.asList("compass", "mask"))
                .appendAddArrayItems(new JsonTextArrayBuilder().append("inventory").build(), new JSONArray(List.of("knife")).toString());

        // Set the property you want to update and the new value
        criteriaUpdate.set("jsonbContent", hibernate6JsonUpdateStatementBuilder.build());

        // Add any conditions to restrict which entities will be updated
        criteriaUpdate.where(entityManager.getCriteriaBuilder().equal(root.get("id"), 24L));

        // WHEN
        entityManager.createQuery(criteriaUpdate).executeUpdate();

        // THEN
        entityManager.refresh(item);
        document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$"));
        assertThat(document.jsonString()).isEqualTo("{\"name\":\"Simon\",\"child\":{\"pets\":[\"chameleon\",\"lion\",\"dolphin\"]},\"inventory\":[\"fins\",\"knife\"]}");
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @Test
    @Transactional
    @DisplayName("should remove json array elements with the update statement by using RemoveJsonValuesFromJsonArrayFunction - documentation demo")
    public void shouldRemoveJsonArrayElementseWithUpdateStatementForDemo() {
        // GIVEN
        Item item = tested.findById(24L);
        DocumentContext document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$"));
        assertThat(document.jsonString()).isEqualTo("{\"child\":{\"pets\":[\"crab\",\"chameleon\"]},\"inventory\":[\"mask\",\"fins\",\"compass\"]}");
        CriteriaUpdate<Item> criteriaUpdate = entityManager.getCriteriaBuilder().createCriteriaUpdate(Item.class);
        Root<Item> root = criteriaUpdate.from(Item.class);

        NodeBuilder nodeBuilder = (NodeBuilder) entityManager.getCriteriaBuilder();
        JSONArray toRemoveJSONArray = new JSONArray(Arrays.asList("mask", "compass"));
        RemoveJsonValuesFromJsonArrayFunction deleteOperator = new RemoveJsonValuesFromJsonArrayFunction(nodeBuilder, new JsonBExtractPath(root.get("jsonbContent"), nodeBuilder, List.of("inventory")), toRemoveJSONArray.toString(), hibernateContext);
        JsonbSetFunction jsonbSetFunction = new JsonbSetFunction(nodeBuilder, (SqmTypedNode) root.get("jsonbContent"), new JsonTextArrayBuilder().append("inventory").build().toString(), deleteOperator, hibernateContext);
        // Set the property you want to update and the new value
        criteriaUpdate.set("jsonbContent", jsonbSetFunction);

        // Add any conditions to restrict which entities will be updated
        criteriaUpdate.where(entityManager.getCriteriaBuilder().equal(root.get("id"), 24L));

        // WHEN
        entityManager.createQuery(criteriaUpdate).executeUpdate();

        // THEN
        entityManager.refresh(item);
        document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$"));
        assertThat(document.jsonString()).isEqualTo("{\"child\":{\"pets\":[\"crab\",\"chameleon\"]},\"inventory\":[\"fins\"]}");
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @Test
    @Transactional
    @DisplayName("should remove json array elements with the update statement by using Hibernate6JsonUpdateStatementBuilder - documentation demo")
    public void shouldRemoveJsonArrayElementsWithUpdateStatementWithJsonUpdateStatementBuilder() {
        // GIVEN
        Item item = tested.findById(24L);
        DocumentContext document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$"));
        assertThat(document.jsonString()).isEqualTo("{\"child\":{\"pets\":[\"crab\",\"chameleon\"]},\"inventory\":[\"mask\",\"fins\",\"compass\"]}");
        CriteriaUpdate<Item> criteriaUpdate = entityManager.getCriteriaBuilder().createCriteriaUpdate(Item.class);
        Root<Item> root = criteriaUpdate.from(Item.class);

        JSONArray toRemoveJSONArray = new JSONArray(Arrays.asList("mask", "compass"));
        Hibernate6JsonUpdateStatementBuilder hibernate6JsonUpdateStatementBuilder = new Hibernate6JsonUpdateStatementBuilder(root.get("jsonbContent"), (NodeBuilder) entityManager.getCriteriaBuilder(), hibernateContext);
        hibernate6JsonUpdateStatementBuilder.appendRemoveArrayItems(new JsonTextArrayBuilder().append("inventory").build(), toRemoveJSONArray.toString());

        // Set the property you want to update and the new value
        criteriaUpdate.set("jsonbContent", hibernate6JsonUpdateStatementBuilder.build());

        // Add any conditions to restrict which entities will be updated
        criteriaUpdate.where(entityManager.getCriteriaBuilder().equal(root.get("id"), 24L));

        // WHEN
        entityManager.createQuery(criteriaUpdate).executeUpdate();

        // THEN
        entityManager.refresh(item);
        document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$"));
        assertThat(document.jsonString()).isEqualTo("{\"child\":{\"pets\":[\"crab\",\"chameleon\"]},\"inventory\":[\"fins\"]}");
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