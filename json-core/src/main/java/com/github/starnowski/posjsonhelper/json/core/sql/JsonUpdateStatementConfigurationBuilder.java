package com.github.starnowski.posjsonhelper.json.core.sql;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

/**
 * Organizes a list of operations, so you can easily create a single SQL update statement that can add multiple changes to a JSON column.
 * For example for below code:
 * <pre>{@code
 * given:
 *  def tested = new JsonUpdateStatementConfigurationBuilder()
 *  .withSort(new DefaultJsonUpdateStatementOperationSort())
 *  .withPostSortFilter(new DefaultJsonUpdateStatementOperationFilter())
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
 *
 * It generates configuration with below operations:
 *
 * <pre>{@code
 * [
 * JsonUpdateStatementOperation{jsonTextArray={parents}, operation=JSONB_SET, value='[]'},
 * JsonUpdateStatementOperation{jsonTextArray={child,pets}, operation=JSONB_SET, value='["cat"]'},
 * JsonUpdateStatementOperation{jsonTextArray={parents,0}, operation=JSONB_SET, value='{"type":"mom", "name":"simone"}'}
 * ]
 * }</pre>
 *
 */
public class JsonUpdateStatementConfigurationBuilder {

    private final List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> operations = new ArrayList<>();
    private JsonUpdateStatementOperationSort sort;
    private JsonUpdateStatementOperationFilter postSortFilter;

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

    public JsonUpdateStatementConfigurationBuilder withPostSortFilter(JsonUpdateStatementOperationFilter postSortFilter) {
        this.postSortFilter = postSortFilter;
        return this;
    }

    public JsonUpdateStatementConfigurationBuilder append(JsonUpdateStatementOperationType operation, JsonTextArray jsonTextArray, String value) {
        return append(new JsonUpdateStatementConfiguration.JsonUpdateStatementOperation(jsonTextArray, operation, value));
    }

    public JsonUpdateStatementConfigurationBuilder append(JsonUpdateStatementConfiguration.JsonUpdateStatementOperation operation) {
        operations.add(operation);
        return this;
    }

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

    public interface JsonUpdateStatementOperationSort {

        List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> sort(List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> operations);
    }

    public interface JsonUpdateStatementOperationFilter {

        List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> filter(List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> operations);
    }
}
