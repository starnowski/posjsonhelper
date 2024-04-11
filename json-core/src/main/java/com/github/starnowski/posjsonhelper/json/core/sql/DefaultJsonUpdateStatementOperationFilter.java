package com.github.starnowski.posjsonhelper.json.core.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DefaultJsonUpdateStatementOperationFilter implements JsonUpdateStatementConfigurationBuilder.JsonUpdateStatementOperationFilter {
    @Override
    public List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> filter(List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> operations) {
        // Process/Gather information about operations objects
        // Filter operations objects based on context
        return operations;
    }

    private OperationFilterContext buildOperationFilterContext(List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> operations){
        OperationFilterContext context = new OperationFilterContext();

        return context;
    }

    private class OperationFilterContext {

        Map<JsonTextArrayJsonUpdateStatementOperationTypeKey, Integer> numberOfOperations = new HashMap<>();
        Map<JsonTextArrayJsonUpdateStatementOperationTypeKey, Integer> lastIndexOperations = new HashMap<>();
    }

    private class JsonTextArrayJsonUpdateStatementOperationTypeKey {
        private final JsonTextArray jsonTextArray;
        private final JsonUpdateStatementOperationType type;

        public JsonTextArrayJsonUpdateStatementOperationTypeKey(JsonTextArray jsonTextArray, JsonUpdateStatementOperationType type) {
            this.jsonTextArray = jsonTextArray;
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            JsonTextArrayJsonUpdateStatementOperationTypeKey that = (JsonTextArrayJsonUpdateStatementOperationTypeKey) o;
            return Objects.equals(jsonTextArray, that.jsonTextArray) && type == that.type;
        }

        @Override
        public int hashCode() {
            return Objects.hash(jsonTextArray, type);
        }
    }
}
