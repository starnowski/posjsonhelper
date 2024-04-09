package com.github.starnowski.posjsonhelper.json.core.sql;

import java.util.Objects;

public class JsonUpdateStatementConfiguration {

    //TODO List<Tuple<JsonTextArray, OperationType, jsonValue>>

    public static class JsonUpdateStatementOperation {
        private final JsonTextArray jsonTextArray;
        private final JsonUpdateStatementOperationType operation;
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
