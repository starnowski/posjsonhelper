package com.github.starnowski.posjsonhelper.hibernate6;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.functions.JsonbSetFunction;
import com.github.starnowski.posjsonhelper.json.core.sql.*;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.tree.SqmTypedNode;

import static com.github.starnowski.posjsonhelper.json.core.sql.JsonUpdateStatementOperationType.JSONB_SET;

/**
 * Builder for SQL statement part that allows to set particular json properties.
 * The idea is to execute some kind of patch operation instead of full update operation for json column value.
 * To set correct order for operation it uses {@link #jsonUpdateStatementConfigurationBuilder} component.
 * For example lets imagine that there is entity class Item that has jsonbContent that stores json.
 * It is possible to update json we below code:
 * <pre>{@code
 *         // GIVEN
 *         CriteriaUpdate<Item> criteriaUpdate = entityManager.getCriteriaBuilder().createCriteriaUpdate(Item.class);
 *         Root<Item> root = criteriaUpdate.from(Item.class);
 *
 *         Hibernate6JsonUpdateStatementBuilder hibernate6JsonUpdateStatementBuilder = new Hibernate6JsonUpdateStatementBuilder(root.get("jsonbContent"), (NodeBuilder) entityManager.getCriteriaBuilder(), hibernateContext);
 *         hibernate6JsonUpdateStatementBuilder.appendJsonbSet(new JsonTextArrayBuilder().append("child").append("birthday").build(), quote("2021-11-23"));
 *         hibernate6JsonUpdateStatementBuilder.appendJsonbSet(new JsonTextArrayBuilder().append("child").append("pets").build(), "[\"cat\"]");
 *         hibernate6JsonUpdateStatementBuilder.appendJsonbSet(new JsonTextArrayBuilder().append("parents").append(0).build(), "{\"type\":\"mom\", \"name\":\"simone\"}");
 *         hibernate6JsonUpdateStatementBuilder.appendJsonbSet(new JsonTextArrayBuilder().append("parents").build(), "[]");
 *
 *         // Set the property you want to update and the new value
 *         criteriaUpdate.set("jsonbContent", hibernate6JsonUpdateStatementBuilder.build());
 *
 *         // Add any conditions to restrict which entities will be updated
 *         criteriaUpdate.where(entityManager.getCriteriaBuilder().equal(root.get("id"), 19L));
 *
 *         // WHEN
 *         entityManager.createQuery(criteriaUpdate).executeUpdate();
 *
 *         // THEN
 *         Item item = tested.findById(19L);
 *         JSONObject jsonObject = new JSONObject("{\"child\": {\"pets\" : [\"cat\"], \"birthday\": \"2021-11-23\"}, \"parents\": [{\"type\":\"mom\", \"name\":\"simone\"}]}");
 *         DocumentContext document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$"));
 *         assertThat(document.jsonString()).isEqualTo(jsonObject.toString());
 * }</pre>
 *
 * The above code is going to execute below sql statement for update:
 *
 * <pre>{@code
 * update
 *     item
 *     set
 *         jsonb_content=
 *          jsonb_set(
 *              jsonb_set(
 *                  jsonb_set(
 *                      jsonb_set(
 *                          jsonb_content, ?::text[], ?::jsonb) -- the most nested operation
 *                      , ?::text[], ?::jsonb)
 *                  , ?::text[], ?::jsonb)
 *              ,?::text[], ?::jsonb
 *          )
 *     where
 *         id=?
 * }</pre>
 *
 * The most nested operation is going to set property "parents" with value "[]".
 *
 * @param <T>
 * @see #build()
 */
public class Hibernate6JsonUpdateStatementBuilder<T> {

    private final Path<T> rootPath;
    private final NodeBuilder nodeBuilder;
    private final HibernateContext hibernateContext;

    public JsonUpdateStatementConfigurationBuilder getJsonUpdateStatementConfigurationBuilder() {
        return jsonUpdateStatementConfigurationBuilder;
    }

    private final JsonUpdateStatementConfigurationBuilder jsonUpdateStatementConfigurationBuilder;

    /**
     * TODO Add note about default values for the sort and the filter components
     * @param rootPath
     * @param nodeBuilder
     * @param hibernateContext
     */
    public Hibernate6JsonUpdateStatementBuilder(Path<T> rootPath, NodeBuilder nodeBuilder, HibernateContext hibernateContext) {
        this.rootPath = rootPath;
        this.nodeBuilder = nodeBuilder;
        this.hibernateContext = hibernateContext;
        jsonUpdateStatementConfigurationBuilder = new JsonUpdateStatementConfigurationBuilder()
                .withSort(new DefaultJsonUpdateStatementOperationSort())
                .withPostSortFilter(new DefaultJsonUpdateStatementOperationFilter());
    }

    /**
     * Adding {@link JsonUpdateStatementOperationType#JSONB_SET} type operation that set value for specific json path
     * @param jsonTextArray json array that specified path for property
     * @param value json value that suppose to be set
     * @return a reference to the constructor component for which the methods were executed
     */
    public Hibernate6JsonUpdateStatementBuilder appendJsonbSet(JsonTextArray jsonTextArray, String value) {
        jsonUpdateStatementConfigurationBuilder.append(JSONB_SET, jsonTextArray, value);
        return this;
    }

    /**
     * Setting the {@link JsonUpdateStatementConfigurationBuilder#sort} property for {@link #jsonUpdateStatementConfigurationBuilder} component
     * @param sort sorting component
     * @return a reference to the constructor component for which the methods were executed
     */
    public Hibernate6JsonUpdateStatementBuilder withSort(JsonUpdateStatementConfigurationBuilder.JsonUpdateStatementOperationSort sort) {
        jsonUpdateStatementConfigurationBuilder.withSort(sort);
        return this;
    }

    /**
     * Setting the {@link JsonUpdateStatementConfigurationBuilder#postSortFilter} property for {@link #jsonUpdateStatementConfigurationBuilder} component
     * @param postSortFilter postSortFilter filtering component
     * @return a reference to the constructor component for which the methods were executed
     */
    public Hibernate6JsonUpdateStatementBuilder withPostSortFilter(JsonUpdateStatementConfigurationBuilder.JsonUpdateStatementOperationFilter postSortFilter) {
        jsonUpdateStatementConfigurationBuilder.withPostSortFilter(postSortFilter);
        return this;
    }

    /**
     * Build part of statement that set json property specified by {@link #rootPath}.
     * Based on configuration produced by {@link #jsonUpdateStatementConfigurationBuilder} the method generates final expression.
     * For example:
     * Lest assume that method {@link  JsonUpdateStatementConfigurationBuilder#build()} returns configuration with below list:
     *
     * <pre>{@code
     * [
     * JsonUpdateStatementOperation{jsonTextArray={parents}, operation=JSONB_SET, value='[]'},
     * JsonUpdateStatementOperation{jsonTextArray={child,birthday}, operation=JSONB_SET, value='"2021-11-23"'},
     * JsonUpdateStatementOperation{jsonTextArray={child,pets}, operation=JSONB_SET, value='["cat"]'},
     * JsonUpdateStatementOperation{jsonTextArray={parents,0}, operation=JSONB_SET, value='{"type":"mom", "name":"simone"}'}
     * ]
     * }</pre>
     *
     * The expression generated on such would be translated to below sql part:
     *
     * <pre>{@code
     *  jsonb_set(
     *      jsonb_set(
     *          jsonb_set(
     *              jsonb_set(jsonb_content, ?::text[], ?::jsonb) -- top operation
     *      , ?::text[], ?::jsonb)
     *  , ?::text[], ?::jsonb)
     * , ?::text[], ?::jsonb)
     * }</pre>
     *
     * @return expression object generated based on {@link #jsonUpdateStatementConfigurationBuilder} configuration
     */
    public Expression<? extends T> build() {
        JsonUpdateStatementConfiguration configuration = jsonUpdateStatementConfigurationBuilder.build();
        SqmTypedNode current = null;
        for (JsonUpdateStatementConfiguration.JsonUpdateStatementOperation operation : configuration.getOperations()) {
            if (current == null) {
                current = new JsonbSetFunction(nodeBuilder, rootPath, operation.getJsonTextArray().toString(), operation.getValue(), hibernateContext);
            } else {
                current = new JsonbSetFunction(nodeBuilder, current, operation.getJsonTextArray().toString(), operation.getValue(), hibernateContext);
            }
        }
        return (Expression<? extends T>) current;
    }
}
