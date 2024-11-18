package com.github.starnowski.posjsonhelper.hibernate6.demo.dao;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.Hibernate6JsonUpdateStatementBuilder;
import com.github.starnowski.posjsonhelper.hibernate6.demo.model.Item;
import com.github.starnowski.posjsonhelper.json.core.sql.JsonTextArrayBuilder;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.hibernate.query.sqm.NodeBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static com.github.starnowski.posjsonhelper.hibernate6.demo.Application.CLEAR_DATABASE_SCRIPT_PATH;
import static com.github.starnowski.posjsonhelper.hibernate6.demo.Application.ITEMS_SCRIPT_PATH;
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
public abstract class AbstractHibernate6JsonUpdateStatementBuilderTest {

    @Autowired
    private ItemDao tested;
    @Autowired
    private HibernateContext hibernateContext;
    @Autowired
    private EntityManager entityManager;


    private static Stream<Arguments> provideShouldAddArrayItems() {
        return Stream.of(
                Arguments.of(19L, List.of(new JsonBSetTestPair(new JsonTextArrayBuilder().append("child").append("pets"), Arrays.asList("dog", "cat"))), "{\"child\":{\"pets\":[\"dog\",\"dog\",\"cat\"]}}")
                ,
                Arguments.of(19L, List.of(new JsonBSetTestPair(new JsonTextArrayBuilder().append("child").append("pets"), Arrays.asList(32, "cat", 47))), "{\"child\":{\"pets\":[\"dog\",32,\"cat\",47]}}")

        );
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @ParameterizedTest
    @MethodSource("provideShouldAddArrayItems")
    @Transactional
    @DisplayName("should modify json array elements by adding specific values with the udpate statement by using Hibernate6JsonUpdateStatementBuilder")
    public void shouldAddArrayItems(Long itemId, List<JsonBSetTestPair> changes, String expectedJson) {
        // GIVEN
        Item item = tested.findById(itemId);
        CriteriaUpdate<Item> criteriaUpdate = entityManager.getCriteriaBuilder().createCriteriaUpdate(Item.class);
        Root<Item> root = criteriaUpdate.from(Item.class);

        Hibernate6JsonUpdateStatementBuilder hibernate6JsonUpdateStatementBuilder = new Hibernate6JsonUpdateStatementBuilder(root.get("jsonbContent"), (NodeBuilder) entityManager.getCriteriaBuilder(), hibernateContext);

        changes.forEach(change ->
                hibernate6JsonUpdateStatementBuilder.appendAddArrayItems(change.getJsonbSetFunctionJsonPathBuilder().build(), change.getJsonValue())
        )
        ;

        // Set the property you want to update and the new value
        criteriaUpdate.set("jsonbContent", hibernate6JsonUpdateStatementBuilder.build());

        // Add any conditions to restrict which entities will be updated
        criteriaUpdate.where(entityManager.getCriteriaBuilder().equal(root.get("id"), itemId));

        // WHEN
        entityManager.createQuery(criteriaUpdate).executeUpdate();

        // THEN
        entityManager.refresh(item);
        DocumentContext document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$"));
        assertThat(document.jsonString()).isEqualTo(expectedJson);
    }

    public static class JsonBSetTestPair {
        private final JsonTextArrayBuilder jsonTextArrayBuilder;
        private final Collection arrayElements;

        public JsonBSetTestPair(JsonTextArrayBuilder jsonTextArrayBuilder, Collection arrayElements) {
            this.jsonTextArrayBuilder = jsonTextArrayBuilder;
            this.arrayElements = arrayElements;
        }

        public JsonTextArrayBuilder getJsonbSetFunctionJsonPathBuilder() {
            return jsonTextArrayBuilder;
        }

        @Override
        public String toString() {
            return "JsonBSetTestPair{" +
                    "jsonTextArrayBuilder=" + jsonTextArrayBuilder.buildString() +
                    ", arrayElements='" + arrayElements + '\'' +
                    '}';
        }

        public Collection getJsonValue() {
            return arrayElements;
        }
    }
}