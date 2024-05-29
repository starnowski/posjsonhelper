/**
 * Posjsonhelper library is an open-source project that adds support of
 * Hibernate query for https://www.postgresql.org/docs/10/functions-json.html)
 * <p>
 * Copyright (C) 2023  Szymon Tarnowski
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package com.github.starnowski.posjsonhelper.hibernate6;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.functions.JsonbSetFunction;
import com.github.starnowski.posjsonhelper.hibernate6.operators.DeleteJsonbBySpecifiedPathOperator;
import com.github.starnowski.posjsonhelper.json.core.sql.*;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.tree.SqmTypedNode;

import static com.github.starnowski.posjsonhelper.json.core.sql.JsonUpdateStatementOperationType.DELETE_BY_SPECIFIC_PATH;
import static com.github.starnowski.posjsonhelper.json.core.sql.JsonUpdateStatementOperationType.JSONB_SET;

/**
 * Builder for SQL statement part that allows to set particular json properties.
 * The idea is to execute some kind of patch operation instead of full update operation for json column value.
 * To set correct order for operation it uses {@link #jsonUpdateStatementConfigurationBuilder} component.
 * For example lets imagine that there is entity class Item that has jsonbContent that stores json.
 * It is possible to update json we below code:
 * <pre>{@code
 *         // GIVEN
 *         Item item = tested.findById(23L);
 *         DocumentContext document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$"));
 *         assertThat(document.jsonString()).isEqualTo("{\"child\":{\"pets\":[\"dog\"]},\"inventory\":[\"mask\",\"fins\"],\"nicknames\":{\"school\":\"bambo\",\"childhood\":\"bob\"}}");
 *         CriteriaUpdate<Item> criteriaUpdate = entityManager.getCriteriaBuilder().createCriteriaUpdate(Item.class);
 *         Root<Item> root = criteriaUpdate.from(Item.class);
 *
 *         Hibernate6JsonUpdateStatementBuilder hibernate6JsonUpdateStatementBuilder = new Hibernate6JsonUpdateStatementBuilder(root.get("jsonbContent"), (NodeBuilder) entityManager.getCriteriaBuilder(), hibernateContext);
 *         hibernate6JsonUpdateStatementBuilder.appendJsonbSet(new JsonTextArrayBuilder().append("child").append("birthday").build(), quote("2021-11-23"));
 *         hibernate6JsonUpdateStatementBuilder.appendJsonbSet(new JsonTextArrayBuilder().append("child").append("pets").build(), "[\"cat\"]");
 *         hibernate6JsonUpdateStatementBuilder.appendDeleteBySpecificPath(new JsonTextArrayBuilder().append("inventory").append("0").build());
 *         hibernate6JsonUpdateStatementBuilder.appendJsonbSet(new JsonTextArrayBuilder().append("parents").append(0).build(), "{\"type\":\"mom\", \"name\":\"simone\"}");
 *         hibernate6JsonUpdateStatementBuilder.appendJsonbSet(new JsonTextArrayBuilder().append("parents").build(), "[]");
 *         hibernate6JsonUpdateStatementBuilder.appendDeleteBySpecificPath(new JsonTextArrayBuilder().append("nicknames").append("childhood").build());
 *
 *         // Set the property you want to update and the new value
 *         criteriaUpdate.set("jsonbContent", hibernate6JsonUpdateStatementBuilder.build());
 *
 *         // Add any conditions to restrict which entities will be updated
 *         criteriaUpdate.where(entityManager.getCriteriaBuilder().equal(root.get("id"), 23L));
 *
 *         // WHEN
 *         entityManager.createQuery(criteriaUpdate).executeUpdate();
 *
 *         // THEN
 *         entityManager.refresh(item);
 *         document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$"));
 *         assertThat(document.jsonString()).isEqualTo("{\"child\":{\"pets\":[\"cat\"],\"birthday\":\"2021-11-23\"},\"parents\":[{\"name\":\"simone\",\"type\":\"mom\"}],\"inventory\":[\"fins\"],\"nicknames\":{\"school\":\"bambo\"}}");
 * }</pre>
 * <p>
 * The above code is going to execute below sql statement for update:
 *
 * <pre>{@code
 * update
 *         item
 *     set
 *         jsonb_content=
 *          jsonb_set(
 *              jsonb_set(
 *                  jsonb_set(
 *                      jsonb_set(
 *                          (
 *                              (jsonb_content #- ?::text[]) -- the most nested #- operator
 *                          #- ?::text[])
 *                      , ?::text[], ?::jsonb) -- the most nested jsonb_set operation
 *                  , ?::text[], ?::jsonb)
 *              , ?::text[], ?::jsonb)
 *          , ?::text[], ?::jsonb)
 *     where
 *         id=?
 * }</pre>
 * <p>
 * <p>
 * As it can be observed based on generated SQL, by default, the first operation is going to be an operation that deletes JSON content.
 * The most nested jsonb_set operation is going to set property "parents" with value "[]".
 *
 * @param <T>
 * @see #build()
 */
public class Hibernate6JsonUpdateStatementBuilder<T, C> {

    /**
     * path object that refers to json property that suppose to be modified
     */
    private final Path<T> rootPath;
    /**
     * hibernate component used to create modification operations
     */
    private final NodeBuilder nodeBuilder;
    /**
     * Hibernate context
     */
    private final HibernateContext hibernateContext;
    private final JsonUpdateStatementConfigurationBuilder<C> jsonUpdateStatementConfigurationBuilder;
    private JsonbSetFunctionFactory<T, C> jsonbSetFunctionFactory = new JsonbSetFunctionFactory<T, C>() {
    };
    private DeleteJsonbBySpecifiedPathOperatorFactory<T, C> deleteJsonbBySpecifiedPathOperatorFactory = new DeleteJsonbBySpecifiedPathOperatorFactory<T, C>() {
    };

    /**
     * Construction initialize property {@link #jsonUpdateStatementConfigurationBuilder} and an instance of
     * {@link DefaultJsonUpdateStatementOperationSort} as sort component ({@link JsonUpdateStatementConfigurationBuilder#sort}) and an instance
     * of {@link DefaultJsonUpdateStatementOperationFilter} as filter component ({@link JsonUpdateStatementConfigurationBuilder#postSortFilter}).
     *
     * @param rootPath         value for {@link #rootPath}
     * @param nodeBuilder      value for {@link #nodeBuilder}
     * @param hibernateContext value for {@link #hibernateContext}
     */
    public Hibernate6JsonUpdateStatementBuilder(Path<T> rootPath, NodeBuilder nodeBuilder, HibernateContext hibernateContext) {
        this.rootPath = rootPath;
        this.nodeBuilder = nodeBuilder;
        this.hibernateContext = hibernateContext;
        jsonUpdateStatementConfigurationBuilder = new JsonUpdateStatementConfigurationBuilder<C>()
                .withSort(new DefaultJsonUpdateStatementOperationSort<C>())
                .withPostSortFilter(new DefaultJsonUpdateStatementOperationFilter<C>());
    }

    public Hibernate6JsonUpdateStatementBuilder<T, C> withJsonbSetFunctionFactory(JsonbSetFunctionFactory<T, C> jsonbSetFunctionFactory) {
        this.jsonbSetFunctionFactory = jsonbSetFunctionFactory;
        return this;
    }

    public Hibernate6JsonUpdateStatementBuilder<T, C> withDeleteJsonbBySpecifiedPathOperatorFactory(DeleteJsonbBySpecifiedPathOperatorFactory<T, C> deleteJsonbBySpecifiedPathOperatorFactory) {
        this.deleteJsonbBySpecifiedPathOperatorFactory = deleteJsonbBySpecifiedPathOperatorFactory;
        return this;
    }

    public JsonUpdateStatementConfigurationBuilder<C> getJsonUpdateStatementConfigurationBuilder() {
        return jsonUpdateStatementConfigurationBuilder;
    }

    /**
     * Adding {@link JsonUpdateStatementOperationType#JSONB_SET} type operation that set value for specific json path
     *
     * @param jsonTextArray json array that specified path for property
     * @param value         json value that suppose to be set
     * @return a reference to the constructor component for which the methods were executed
     */
    public Hibernate6JsonUpdateStatementBuilder<T, C> appendJsonbSet(JsonTextArray jsonTextArray, String value) {
        jsonUpdateStatementConfigurationBuilder.append(JSONB_SET, jsonTextArray, value);
        return this;
    }

    public Hibernate6JsonUpdateStatementBuilder<T, C> appendJsonbSet(JsonTextArray jsonTextArray, String value, C customValue) {
        jsonUpdateStatementConfigurationBuilder.append(JSONB_SET, jsonTextArray, value);
        return this;
    }


    /**
     * Adding {@link JsonUpdateStatementOperationType#DELETE_BY_SPECIFIC_PATH} type operation that deletes property for specific json path
     *
     * @param jsonTextArray json array that specified path for property
     * @return a reference to the constructor component for which the methods were executed
     */
    public Hibernate6JsonUpdateStatementBuilder<T, C> appendDeleteBySpecificPath(JsonTextArray jsonTextArray) {
        jsonUpdateStatementConfigurationBuilder.append(DELETE_BY_SPECIFIC_PATH, jsonTextArray, null);
        return this;
    }

    /**
     * Setting the {@link JsonUpdateStatementConfigurationBuilder#sort} property for {@link #jsonUpdateStatementConfigurationBuilder} component
     *
     * @param sort sorting component
     * @return a reference to the constructor component for which the methods were executed
     */
    public Hibernate6JsonUpdateStatementBuilder<T, C> withSort(JsonUpdateStatementConfigurationBuilder.JsonUpdateStatementOperationSort<C> sort) {
        jsonUpdateStatementConfigurationBuilder.withSort(sort);
        return this;
    }

    /**
     * Setting the {@link JsonUpdateStatementConfigurationBuilder#postSortFilter} property for {@link #jsonUpdateStatementConfigurationBuilder} component
     *
     * @param postSortFilter postSortFilter filtering component
     * @return a reference to the constructor component for which the methods were executed
     */
    public Hibernate6JsonUpdateStatementBuilder<T, C> withPostSortFilter(JsonUpdateStatementConfigurationBuilder.JsonUpdateStatementOperationFilter<C> postSortFilter) {
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
     * <p>
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
        JsonUpdateStatementConfiguration<C> configuration = jsonUpdateStatementConfigurationBuilder.build();
        SqmTypedNode current = null;
        for (JsonUpdateStatementConfiguration.JsonUpdateStatementOperation<C> operation : configuration.getOperations()) {
            switch (operation.getOperation()) {
                case DELETE_BY_SPECIFIC_PATH:
                    if (current == null) {
                        current = deleteJsonbBySpecifiedPathOperatorFactory.build(nodeBuilder, rootPath, operation, hibernateContext);
                    } else {
                        current = deleteJsonbBySpecifiedPathOperatorFactory.build(nodeBuilder, current, operation, hibernateContext);
                    }
                    break;
                case JSONB_SET:
                    if (current == null) {
                        current = jsonbSetFunctionFactory.build(nodeBuilder, rootPath, operation, hibernateContext);
                    } else {
                        current = jsonbSetFunctionFactory.build(nodeBuilder, current, operation, hibernateContext);
                    }
            }
        }
        return (Expression<? extends T>) current;
    }

    interface JsonbSetFunctionFactory<T, C> {

        default JsonbSetFunction build(NodeBuilder nodeBuilder, Path<T> rootPath, JsonUpdateStatementConfiguration.JsonUpdateStatementOperation<C> operation, HibernateContext hibernateContext) {
            return new JsonbSetFunction(nodeBuilder, rootPath, operation.getJsonTextArray().toString(), operation.getValue(), hibernateContext);
        }

        default JsonbSetFunction build(NodeBuilder nodeBuilder, SqmTypedNode sqmTypedNode, JsonUpdateStatementConfiguration.JsonUpdateStatementOperation<C> operation, HibernateContext hibernateContext) {
            return new JsonbSetFunction(nodeBuilder, sqmTypedNode, operation.getJsonTextArray().toString(), operation.getValue(), hibernateContext);
        }
    }

    interface DeleteJsonbBySpecifiedPathOperatorFactory<T, C> {
        default DeleteJsonbBySpecifiedPathOperator build(NodeBuilder nodeBuilder, Path<T> rootPath, JsonUpdateStatementConfiguration.JsonUpdateStatementOperation<C> operation, HibernateContext hibernateContext) {
            return new DeleteJsonbBySpecifiedPathOperator(nodeBuilder, rootPath, operation.getJsonTextArray().toString(), hibernateContext);
        }

        default DeleteJsonbBySpecifiedPathOperator build(NodeBuilder nodeBuilder, SqmTypedNode sqmTypedNode, JsonUpdateStatementConfiguration.JsonUpdateStatementOperation<C> operation, HibernateContext hibernateContext) {
            return new DeleteJsonbBySpecifiedPathOperator(nodeBuilder, sqmTypedNode, operation.getJsonTextArray().toString(), hibernateContext);
        }
    }
}
