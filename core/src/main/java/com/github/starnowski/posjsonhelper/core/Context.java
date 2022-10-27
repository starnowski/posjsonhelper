package com.github.starnowski.posjsonhelper.core;

import static com.github.starnowski.posjsonhelper.core.Constants.DEFAULT_JSONB_ALL_ARRAY_STRINGS_EXIST_FUNCTION_NAME;
import static com.github.starnowski.posjsonhelper.core.Constants.DEFAULT_JSONB_ANY_ARRAY_STRINGS_EXIST_FUNCTION_NAME;

public class Context {

    private final String jsonbAllArrayStringsExistFunctionReference;
    private final String jsonbAnyArrayStringsExistFunctionReference;
    private final String schema;

    public Context(String jsonbAllArrayStringsExistFunctionReference, String jsonbAnyArrayStringsExistFunctionReference, String schema) {
        this.jsonbAllArrayStringsExistFunctionReference = jsonbAllArrayStringsExistFunctionReference;
        this.jsonbAnyArrayStringsExistFunctionReference = jsonbAnyArrayStringsExistFunctionReference;
        this.schema = schema;
    }

    public static ContextBuilder builder() {
        return new ContextBuilder();
    }

    public String getSchema() {
        return schema;
    }

    public String getJsonbAllArrayStringsExistFunctionReference() {
        return jsonbAllArrayStringsExistFunctionReference;
    }

    public String getJsonbAnyArrayStringsExistFunctionReference() {
        return jsonbAnyArrayStringsExistFunctionReference;
    }

    public static class ContextBuilder {
        private String jsonbAllArrayStringsExistFunctionReference = DEFAULT_JSONB_ALL_ARRAY_STRINGS_EXIST_FUNCTION_NAME;
        private String jsonbAnyArrayStringsExistFunctionReference = DEFAULT_JSONB_ANY_ARRAY_STRINGS_EXIST_FUNCTION_NAME;
        private String schema;

        public ContextBuilder withSchema(String schema) {
            this.schema = schema;
            return this;
        }

        public ContextBuilder withJsonbAllArrayStringsExistFunctionReference(String jsonbAllArrayStringsExistFunctionReference) {
            this.jsonbAllArrayStringsExistFunctionReference = jsonbAllArrayStringsExistFunctionReference;
            return this;
        }

        public ContextBuilder withJsonbAnyArrayStringsExistFunctionReference(String jsonbAnyArrayStringsExistFunctionReference) {
            this.jsonbAnyArrayStringsExistFunctionReference = jsonbAnyArrayStringsExistFunctionReference;
            return this;
        }

        public Context build() {
            return new Context(this.jsonbAllArrayStringsExistFunctionReference, this.jsonbAnyArrayStringsExistFunctionReference, schema);
        }
    }
}
