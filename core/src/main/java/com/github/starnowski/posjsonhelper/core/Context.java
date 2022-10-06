package com.github.starnowski.posjsonhelper.core;

public class Context {

    private final String jsonbAllArrayStringsExistFunctionReference;
    private final String jsonbAnyArrayStringsExistFunctionReference;

    public Context(String jsonbAllArrayStringsExistFunctionReference, String jsonbAnyArrayStringsExistFunctionReference) {
        this.jsonbAllArrayStringsExistFunctionReference = jsonbAllArrayStringsExistFunctionReference;
        this.jsonbAnyArrayStringsExistFunctionReference = jsonbAnyArrayStringsExistFunctionReference;
    }

    public static ContextBuilder builder() {
        return new ContextBuilder();
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

        public ContextBuilder withJsonbAllArrayStringsExistFunctionReference(String jsonbAllArrayStringsExistFunctionReference) {
            this.jsonbAllArrayStringsExistFunctionReference = jsonbAllArrayStringsExistFunctionReference;
            return this;
        }

        public ContextBuilder withJsonbAnyArrayStringsExistFunctionReference(String jsonbAnyArrayStringsExistFunctionReference) {
            this.jsonbAnyArrayStringsExistFunctionReference = jsonbAnyArrayStringsExistFunctionReference;
            return this;
        }

        public Context build() {
            return new Context(this.jsonbAllArrayStringsExistFunctionReference, this.jsonbAnyArrayStringsExistFunctionReference);
        }
    }
}
