package com.github.starnowski.posjsonhelper.json.core.sql;

import java.util.ArrayList;
import java.util.List;

public class JsonUpdateStatementConfigurationBuilder {

    private final List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> operations = new ArrayList<>();
    private JsonUpdateStatementOperationSort sort;
    private JsonUpdateStatementOperationFilter postSortFilter;

    public JsonUpdateStatementConfigurationBuilder withSort(JsonUpdateStatementOperationSort sort) {
        this.sort = sort;
        return this;
    }

    public JsonUpdateStatementConfigurationBuilder withPostSortFilter(JsonUpdateStatementOperationFilter postSortFilter) {
        this.postSortFilter = postSortFilter;
        return this;
    }

    public JsonUpdateStatementConfigurationBuilder append(JsonUpdateStatementOperationType operation, JsonTextArray jsonTextArray, String value) {
        operations.add(new JsonUpdateStatementConfiguration.JsonUpdateStatementOperation(jsonTextArray, operation, value));
        return this;
    }

    public JsonUpdateStatementConfiguration build() {
        //TODO
        return new JsonUpdateStatementConfiguration(operations);
    }

    public interface JsonUpdateStatementOperationSort {

        List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> sort(List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> operations);
    }

    public interface JsonUpdateStatementOperationFilter {

        List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> filter(List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> operations);
    }
}
