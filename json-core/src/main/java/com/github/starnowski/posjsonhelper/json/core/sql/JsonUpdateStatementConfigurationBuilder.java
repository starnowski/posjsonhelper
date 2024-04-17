package com.github.starnowski.posjsonhelper.json.core.sql;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

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
