package com.github.starnowski.posjsonhelper.json.core.sql;

import java.sql.Array;
import java.util.*;

public class DefaultJsonUpdateStatementOperationFilter implements JsonUpdateStatementConfigurationBuilder.JsonUpdateStatementOperationFilter {
    @Override
    public List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> filter(List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> operations) {
        // Process/Gather information about operations objects
        OperationFilterContext context = buildOperationFilterContext(operations);
        int i = 0;
        List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> results = new ArrayList<>(operations);
        Iterator<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> it = results.iterator();
        while (it.hasNext())
        {
            JsonUpdateStatementConfiguration.JsonUpdateStatementOperation op = it.next();
            JsonTextArrayJsonUpdateStatementOperationTypeKey key = new JsonTextArrayJsonUpdateStatementOperationTypeKey(op.getJsonTextArray(), op.getOperation());
            if (context.numberOfOperations.get(key) > 1) {
                int lastIndex = context.lastIndexOperations.get(key);
                if (lastIndex != i) {
                    it.remove();
                }
            }
            i++;
        }
        // Filter operations objects based on context
        return results;
    }

    private OperationFilterContext buildOperationFilterContext(List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> operations){
        final OperationFilterContext context = new OperationFilterContext();
        for(int i = 0; i < operations.size(); i++) {
            JsonUpdateStatementConfiguration.JsonUpdateStatementOperation op = operations.get(i);
            JsonTextArrayJsonUpdateStatementOperationTypeKey key = new JsonTextArrayJsonUpdateStatementOperationTypeKey(op.getJsonTextArray(), op.getOperation());
            context.numberOfOperations.merge(key, 1, (integer, integer2) -> integer + (integer2 == null ? 0 : integer2));
            context.lastIndexOperations.put(key, i);
        }
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
