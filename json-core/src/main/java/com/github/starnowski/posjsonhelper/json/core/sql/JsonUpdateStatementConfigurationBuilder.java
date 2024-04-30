/**
 *     Posjsonhelper library is an open-source project that adds support of
 *     Hibernate query for https://www.postgresql.org/docs/10/functions-json.html)
 *
 *     Copyright (C) 2023  Szymon Tarnowski
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 */
package com.github.starnowski.posjsonhelper.json.core.sql;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

/**
 * Organizes a list of operations, each of which modifies a JSON property,
 * so you can easily create a single SQL update statement that can add multiple changes to a JSON column.
 * For example for below code:
 * <pre>{@code
 * given:
 *  def tested = new JsonUpdateStatementConfigurationBuilder()
 *  .withSort(new DefaultJsonUpdateStatementOperationSort())
 *  .withPostSortFilter(new DefaultJsonUpdateStatementOperationFilter())
 *  .append(JSONB_SET, new JsonTextArrayBuilder().append("child").append("birthday").build(), "\"2021-11-23\"")
 *  .append(JSONB_SET, new JsonTextArrayBuilder().append("child").append("pets").build(), "[\"cat\"]")
 *  .append(JSONB_SET, new JsonTextArrayBuilder().append("parents").append(0).build(), "{\"type\":\"mom\", \"name\":\"simone\"}")
 *  .append(JSONB_SET, new JsonTextArrayBuilder().append("parents").build(), "[]")
 *
 *
 * when:
 *  def result = tested.build()
 *
 * then:
 *  result
 *  result.operations
 *  System.out.println(result.operations)
 * }</pre>
 * <p>
 * It generates configuration with below operations:
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
 * Assuming that we have database table "item" and column that stores json is called "jsonb_content" the update statement
 * would like as below example:
 *
 * <pre>{@code
 * UPDATE
 * item
 * SET
 * jsonb_content=
 *  jsonb_set(
 *      jsonb_set(
 *          jsonb_set(
 *              jsonb_set(jsonb_content, ?::text[], ?::jsonb) -- top operation
 *      , ?::text[], ?::jsonb)
 *  , ?::text[], ?::jsonb)
 * , ?::text[], ?::jsonb)
 * WHERE
 * id=?
 * }</pre>
 * <p>
 * In such a prepared statement, the top operation would set the "parents" property with an empty array.
 * The next operation will set the "child, birthday" property to "2021-11-23" and so on.
 * <p>
 * For more details please check {@link #build()} method.
 */
public class JsonUpdateStatementConfigurationBuilder {

    private final List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> operations = new ArrayList<>();
    /**
     * Sorting component, by default property has null value
     */
    private JsonUpdateStatementOperationSort sort;
    /**
     * Filtering component, by default property has null value
     */
    private JsonUpdateStatementOperationFilter postSortFilter;

    /**
     * Setting the {@link #sort} property
     * @param sort sorting component
     * @return a reference to the constructor component for which the methods were executed
     */
    public JsonUpdateStatementConfigurationBuilder withSort(JsonUpdateStatementOperationSort sort) {
        this.sort = sort;
        return this;
    }

    public JsonUpdateStatementOperationSort getSort() {
        return sort;
    }

    public JsonUpdateStatementOperationFilter getPostSortFilter() {
        return postSortFilter;
    }

    /**
     * Setting the {@link #postSortFilter} property
     * @param postSortFilter filtering component
     * @return a reference to the constructor component for which the methods were executed
     */
    public JsonUpdateStatementConfigurationBuilder withPostSortFilter(JsonUpdateStatementOperationFilter postSortFilter) {
        this.postSortFilter = postSortFilter;
        return this;
    }

    /**
     * Adds a json property change
     * @param operation the type of operation that should be applied to the json property
     * @param jsonTextArray the path of the property that should be added or changed
     * @param value value that should be applied
     * @return a reference to the constructor component for which the methods were executed
     */
    public JsonUpdateStatementConfigurationBuilder append(JsonUpdateStatementOperationType operation, JsonTextArray jsonTextArray, String value) {
        return append(new JsonUpdateStatementConfiguration.JsonUpdateStatementOperation(jsonTextArray, operation, value));
    }

    /**
     * Adds a json property change
     * @param operation operation to apply to the json property
     * @return a reference to the constructor component for which the methods were executed
     */
    public JsonUpdateStatementConfigurationBuilder append(JsonUpdateStatementConfiguration.JsonUpdateStatementOperation operation) {
        operations.add(operation);
        return this;
    }

    /**
     * Generate a configuration with a list of operations that need to be added in the correct order to apply all changes to the json column.
     * First, the method uses a sorting component ({@link #sort}) to sort operations.
     * If the {@link #sort} component is null, this step is skipped.
     * After potential sorting, the next step is to filter the operations.
     * Which means that some operations ({@link #operations}) may be filtered out and not added to the final list of the configuration object.
     * In can when the {@link #postSortFilter} component is null, this step is also skipped.
     *
     * @return configuration object with final operations list
     */
    public JsonUpdateStatementConfiguration build() {
        List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> operationsCopy = unmodifiableList(operations);
        if (sort != null) {
            operationsCopy = sort.sort(operationsCopy);
        }
        if (postSortFilter != null) {
            operationsCopy = postSortFilter.filter(operationsCopy);
        }
        return new JsonUpdateStatementConfiguration(operationsCopy);
    }

    /**
     * Component that sorts operation list
     * @see DefaultJsonUpdateStatementOperationSort
     */
    public interface JsonUpdateStatementOperationSort {

        /**
         * Sort operations list
         *
         * @param operations list of operations that should be sorted
         * @return new sorted list of operations
         */
        List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> sort(List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> operations);
    }

    /**
     * Component that filters operation list
     * @see DefaultJsonUpdateStatementOperationFilter
     */
    public interface JsonUpdateStatementOperationFilter {

        /**
         * Filter operations list
         *
         * @param operations list of operations that should be filtered
         * @return new filtered list of operations
         */
        List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> filter(List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> operations);
    }
}
