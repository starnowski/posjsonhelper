package com.github.starnowski.posjsonhelper.core;

import static com.github.starnowski.posjsonhelper.core.Constants.DEFAULT_JSONB_ALL_ARRAY_STRINGS_EXIST_HIBERNATE_OPERATOR;
import static com.github.starnowski.posjsonhelper.core.Constants.DEFAULT_JSONB_ANY_ARRAY_STRINGS_EXIST_HIBERNATE_OPERATOR;

public class HibernateContext {

    private final String jsonbAllArrayStringsExistOperator;
    private final String jsonbAnyArrayStringsExistOperator;

    public HibernateContext(String jsonbAllArrayStringsExistOperator, String jsonbAnyArrayStringsExistOperator) {
        this.jsonbAllArrayStringsExistOperator = jsonbAllArrayStringsExistOperator;
        this.jsonbAnyArrayStringsExistOperator = jsonbAnyArrayStringsExistOperator;
    }

    public String getJsonbAllArrayStringsExistOperator() {
        return jsonbAllArrayStringsExistOperator;
    }

    public String getJsonbAnyArrayStringsExistOperator() {
        return jsonbAnyArrayStringsExistOperator;
    }

    public static ContextBuilder builder() {
        return new ContextBuilder();
    }

    public static class ContextBuilder {
        private String jsonbAllArrayStringsExistOperator = DEFAULT_JSONB_ALL_ARRAY_STRINGS_EXIST_HIBERNATE_OPERATOR;
        private String jsonbAnyArrayStringsExistOperator = DEFAULT_JSONB_ANY_ARRAY_STRINGS_EXIST_HIBERNATE_OPERATOR;

        public ContextBuilder withJsonbAllArrayStringsExistOperator(String jsonbAllArrayStringsExistOperator) {
            this.jsonbAllArrayStringsExistOperator = jsonbAllArrayStringsExistOperator;
            return this;
        }

        public ContextBuilder withJsonbAnyArrayStringsExistOperator(String jsonbAnyArrayStringsExistOperator) {
            this.jsonbAnyArrayStringsExistOperator = jsonbAnyArrayStringsExistOperator;
            return this;
        }

        public HibernateContext build() {
            return new HibernateContext(this.jsonbAllArrayStringsExistOperator, this.jsonbAnyArrayStringsExistOperator);
        }
    }
}
