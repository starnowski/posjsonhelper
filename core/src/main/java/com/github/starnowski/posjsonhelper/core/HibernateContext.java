package com.github.starnowski.posjsonhelper.core;

import static com.github.starnowski.posjsonhelper.core.Constants.*;

public class HibernateContext {

    private final String jsonbAllArrayStringsExistOperator;
    private final String jsonbAnyArrayStringsExistOperator;
    private final String jsonFunctionJsonArrayOperator;

    public HibernateContext(String jsonbAllArrayStringsExistOperator, String jsonbAnyArrayStringsExistOperator, String jsonFunctionJsonArrayOperator) {
        this.jsonbAllArrayStringsExistOperator = jsonbAllArrayStringsExistOperator;
        this.jsonbAnyArrayStringsExistOperator = jsonbAnyArrayStringsExistOperator;
        this.jsonFunctionJsonArrayOperator = jsonFunctionJsonArrayOperator;
    }

    public static ContextBuilder builder() {
        return new ContextBuilder();
    }

    public String getJsonbAllArrayStringsExistOperator() {
        return jsonbAllArrayStringsExistOperator;
    }

    public String getJsonbAnyArrayStringsExistOperator() {
        return jsonbAnyArrayStringsExistOperator;
    }

    public String getJsonFunctionJsonArrayOperator() {
        return jsonFunctionJsonArrayOperator;
    }

    @Override
    public String toString() {
        return "HibernateContext{" +
                "jsonbAllArrayStringsExistOperator='" + jsonbAllArrayStringsExistOperator + '\'' +
                ", jsonbAnyArrayStringsExistOperator='" + jsonbAnyArrayStringsExistOperator + '\'' +
                ", jsonFunctionJsonArrayOperator='" + jsonFunctionJsonArrayOperator + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HibernateContext that = (HibernateContext) o;

        if (jsonbAllArrayStringsExistOperator != null ? !jsonbAllArrayStringsExistOperator.equals(that.jsonbAllArrayStringsExistOperator) : that.jsonbAllArrayStringsExistOperator != null)
            return false;
        if (jsonbAnyArrayStringsExistOperator != null ? !jsonbAnyArrayStringsExistOperator.equals(that.jsonbAnyArrayStringsExistOperator) : that.jsonbAnyArrayStringsExistOperator != null)
            return false;
        return jsonFunctionJsonArrayOperator != null ? jsonFunctionJsonArrayOperator.equals(that.jsonFunctionJsonArrayOperator) : that.jsonFunctionJsonArrayOperator == null;
    }

    @Override
    public int hashCode() {
        int result = jsonbAllArrayStringsExistOperator != null ? jsonbAllArrayStringsExistOperator.hashCode() : 0;
        result = 31 * result + (jsonbAnyArrayStringsExistOperator != null ? jsonbAnyArrayStringsExistOperator.hashCode() : 0);
        result = 31 * result + (jsonFunctionJsonArrayOperator != null ? jsonFunctionJsonArrayOperator.hashCode() : 0);
        return result;
    }

    public static class ContextBuilder {

        private String jsonbAllArrayStringsExistOperator = DEFAULT_JSONB_ALL_ARRAY_STRINGS_EXIST_HIBERNATE_OPERATOR;
        private String jsonbAnyArrayStringsExistOperator = DEFAULT_JSONB_ANY_ARRAY_STRINGS_EXIST_HIBERNATE_OPERATOR;
        private String jsonFunctionJsonArrayOperator = DEFAULT_JSON_FUNCTION_JSON_ARRAY_HIBERNATE_OPERATOR;

        public ContextBuilder withJsonbAllArrayStringsExistOperator(String jsonbAllArrayStringsExistOperator) {
            this.jsonbAllArrayStringsExistOperator = jsonbAllArrayStringsExistOperator;
            return this;
        }

        public ContextBuilder withJsonbAnyArrayStringsExistOperator(String jsonbAnyArrayStringsExistOperator) {
            this.jsonbAnyArrayStringsExistOperator = jsonbAnyArrayStringsExistOperator;
            return this;
        }

        public ContextBuilder withJsonFunctionJsonArrayOperator(String jsonFunctionJsonArrayOperator) {
            this.jsonFunctionJsonArrayOperator = jsonFunctionJsonArrayOperator;
            return this;
        }

        public HibernateContext build() {
            return new HibernateContext(this.jsonbAllArrayStringsExistOperator, this.jsonbAnyArrayStringsExistOperator, jsonFunctionJsonArrayOperator);
        }

        public ContextBuilder withHibernateContext(HibernateContext hibernateContext) {
            return withJsonbAllArrayStringsExistOperator(hibernateContext.getJsonbAllArrayStringsExistOperator());
        }
    }
}
