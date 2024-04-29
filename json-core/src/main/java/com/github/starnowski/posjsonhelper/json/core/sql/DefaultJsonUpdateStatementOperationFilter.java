package com.github.starnowski.posjsonhelper.json.core.sql;

import java.util.*;

import static java.util.Optional.ofNullable;

/**
 * //TODO
 * Default implementation of {@link com.github.starnowski.posjsonhelper.json.core.sql.JsonUpdateStatementConfigurationBuilder.JsonUpdateStatementOperationFilter}.
 * Removes operation for the same path. For example, if there are two operations for the same JSON path, the first operation will be removed.
 * Because changes for this operation at the end are going to be overridden.
 */
public class DefaultJsonUpdateStatementOperationFilter implements JsonUpdateStatementConfigurationBuilder.JsonUpdateStatementOperationFilter {
    @Override
    public List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> filter(List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> operations) {
        // Process/Gather information about operations objects
        OperationFilterContext context = buildOperationFilterContext(operations);
        int i = 0;
        List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> results = new ArrayList<>(operations);
        Iterator<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> it = results.iterator();
        while (it.hasNext()) {
            JsonUpdateStatementConfiguration.JsonUpdateStatementOperation op = it.next();
            mainswitch:
            switch (op.getOperation()) {
                case JSONB_SET:
                    JsonTextArrayJsonUpdateStatementOperationTypeKey key = new JsonTextArrayJsonUpdateStatementOperationTypeKey(op.getJsonTextArray(), op.getOperation());
                    if (context.jsonbSetOperationNumberOfOperations.get(key) > 1) {
                        int lastIndex = context.jsonbSetOperationLastIndexOperations.get(key);
                        if (lastIndex != i) {
                            it.remove();
                        }
                    }
                    break;
                case DELETE_BY_SPECIFIC_PATH:
                    String opJsonTextArrayString = op.getJsonTextArray().toString();
                    Iterator<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> tmpIt = operations.iterator();
                    for (JsonUpdateStatementConfiguration.JsonUpdateStatementOperation current = tmpIt.next(); current != op && tmpIt.hasNext(); current = tmpIt.next()) {
                        String currentKey = current.getJsonTextArray().toString();
                        if (currentKey.equals(opJsonTextArrayString)) {
                            it.remove();
                            break mainswitch;
                        }
                    }
                    for (JsonUpdateStatementConfiguration.JsonUpdateStatementOperation current : operations) {
                        if (current == op) {
                            continue;
                        }
                        String currentKeyKey = current.getJsonTextArray().toString();
                        if (opJsonTextArrayString.startsWith(currentKeyKey.replace("}",","))) {
                            it.remove();
                            break mainswitch;
                        }
                    }
            }
            i++;
        }
        // Filter operations objects based on context
        return results;
    }

    private OperationFilterContext buildOperationFilterContext(List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> operations) {
        final OperationFilterContext context = new OperationFilterContext();
        for (int i = 0; i < operations.size(); i++) {
            JsonUpdateStatementConfiguration.JsonUpdateStatementOperation op = operations.get(i);
            JsonTextArrayJsonUpdateStatementOperationTypeKey key = new JsonTextArrayJsonUpdateStatementOperationTypeKey(op.getJsonTextArray(), op.getOperation());
            context.jsonbSetOperationNumberOfOperations.merge(key, 1, (integer, integer2) -> integer + (integer2 == null ? 0 : integer2));
            context.jsonbSetOperationLastIndexOperations.put(key, i);
        }
        return context;
    }

    private class OperationFilterContext {

        Map<JsonTextArrayJsonUpdateStatementOperationTypeKey, Integer> jsonbSetOperationNumberOfOperations = new HashMap<>();
        Map<JsonTextArrayJsonUpdateStatementOperationTypeKey, Integer> jsonbSetOperationLastIndexOperations = new HashMap<>();
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
            return Objects.equals(ofNullable(jsonTextArray).map(jta -> jta.toString()).orElse(null), ofNullable(that.jsonTextArray).map(jta -> jta.toString()).orElse(null)) && type == that.type;
        }

        @Override
        public int hashCode() {
            return Objects.hash(ofNullable(jsonTextArray).map(jta -> jta.toString()).orElse(null), type);
        }
    }
}
