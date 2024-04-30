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

import java.util.*;

import static java.util.Optional.ofNullable;

/**
 * Default implementation of {@link com.github.starnowski.posjsonhelper.json.core.sql.JsonUpdateStatementConfigurationBuilder.JsonUpdateStatementOperationFilter}.
 * The filtering logic depends on type of operation {@link JsonUpdateStatementConfiguration.JsonUpdateStatementOperation#operation}.
 *
 * For <b>{@link JsonUpdateStatementOperationType#JSONB_SET}</b> operations:
 *
 * Removes operation for the same path. For example, if there are two operations for the same JSON path, the first operation will be removed.
 * Because changes for this operation at the end are going to be overridden.
 *
 * For <b>{@link JsonUpdateStatementOperationType#DELETE_BY_SPECIFIC_PATH}</b> operations:
 *
 * Removes operation for the same path. For example, if there are two operations for the same JSON path, the second operation will be removed.
 *
 * If for particular operation there is another operation in collection that has path that point to property that is parent for this child element then
 * operation with path that points to child element is going to be removed.
 * Because changes for this operation was already made.
 * For example, if we have to delete operations with below paths:
 *
 * <pre>{@code
 * [{parent,child1, child11}, {parent}]
 * }</pre>
 *
 * then the element with '{parent,child1, child11}' is going to be removed.
 *
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
