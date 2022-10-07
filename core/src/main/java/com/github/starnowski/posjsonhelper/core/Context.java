package com.github.starnowski.posjsonhelper.core;

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
        private String jsonbAllArrayStringsExistFunctionReference = "jsonb_all_array_strings_exist";
        private String jsonbAnyArrayStringsExistFunctionReference = "jsonb_any_array_strings_exist";
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
