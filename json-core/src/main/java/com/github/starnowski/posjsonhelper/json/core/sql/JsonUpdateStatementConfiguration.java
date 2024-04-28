package com.github.starnowski.posjsonhelper.json.core.sql;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

/**
 * Component that has aggregation of operations (object of type {@link JsonUpdateStatementOperation}).
 * Operations should be performed in the order in which they are written in the @link #operations} list.
 */
public class JsonUpdateStatementConfiguration {

    private final List<JsonUpdateStatementOperation> operations;

    public JsonUpdateStatementConfiguration(List<JsonUpdateStatementOperation> operations) {
        this.operations = ofNullable(operations).map(Collections::unmodifiableList).orElse(emptyList());
    }

    public List<JsonUpdateStatementOperation> getOperations() {
        return operations;
    }

    /**
     * An object representing the operation that should be performed on the JSON object.
     */
    public static class JsonUpdateStatementOperation {
        /**
         * Path for the JSON property that should be changed.
         */
        private final JsonTextArray jsonTextArray;
        /**
         * The type of operation to be used.
         */
        private final JsonUpdateStatementOperationType operation;
        /**
         * The JSON value that should be set.
         */
        private final String value;

        public JsonUpdateStatementOperation(JsonTextArray jsonTextArray, JsonUpdateStatementOperationType operation, String value) {
            this.jsonTextArray = jsonTextArray;
            this.operation = operation;
            this.value = value;
        }

        public JsonTextArray getJsonTextArray() {
            return jsonTextArray;
        }

        public JsonUpdateStatementOperationType getOperation() {
            return operation;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "JsonUpdateStatementOperation{" +
                    "jsonTextArray=" + jsonTextArray +
                    ", operation=" + operation +
                    ", value='" + value + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            JsonUpdateStatementOperation that = (JsonUpdateStatementOperation) o;
            return Objects.equals(jsonTextArray, that.jsonTextArray) && operation == that.operation && Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(jsonTextArray, operation, value);
        }
    }
}
