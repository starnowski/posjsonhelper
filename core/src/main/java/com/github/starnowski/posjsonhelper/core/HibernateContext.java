/**
 * Posjsonhelper library is an open-source project that adds support of
 * Hibernate query for https://www.postgresql.org/docs/10/functions-json.html)
 * <p>
 * Copyright (C) 2023  Szymon Tarnowski
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package com.github.starnowski.posjsonhelper.core;

import static com.github.starnowski.posjsonhelper.core.Constants.*;

/**
 * Component that store properties used by hibernate components
 */
public class HibernateContext {

    /**
     * Name of HQL function that wraps SQL function {@link Context#jsonbAllArrayStringsExistFunctionReference}.
     * By default, the property is initialized with the value of  {@link Constants#DEFAULT_JSONB_ALL_ARRAY_STRINGS_EXIST_HIBERNATE_OPERATOR} constant.
     */
    private final String jsonbAllArrayStringsExistOperator;
    /**
     * Name of HQL function that wraps SQL function {@link Context#jsonbAnyArrayStringsExistFunctionReference}.
     * By default, the property is initialized with the value of  {@link Constants#DEFAULT_JSONB_ANY_ARRAY_STRINGS_EXIST_HIBERNATE_OPERATOR} constant.
     */
    private final String jsonbAnyArrayStringsExistOperator;
    /**
     * Name of HQL function that wraps Postgres "ARRAY" operator.
     * By default, the property is initialized with the value of  {@link Constants#DEFAULT_JSON_FUNCTION_JSON_ARRAY_HIBERNATE_OPERATOR} constant.
     */
    private final String jsonFunctionJsonArrayOperator;
    /**
     * Name of HQL function that wraps Postgres text "@@" operator.
     * By default, the property is initialized with the value of  {@link Constants#DEFAULT_TEXT_FUNCTION_HIBERNATE_OPERATOR} constant.
     */
    private final String textFunctionOperator;
    /**
     * Name of HQL function that wraps Postgres text "::" operator.
     * By default, the property is initialized with the value of  {@link Constants#DEFAULT_CAST_FUNCTION_HIBERNATE_OPERATOR} constant.
     */
    private final String castFunctionOperator;
    /**
     * Name of HQL function that wraps Postgres concatenate "||" operator.
     * By default, the property is initialized with the value of  {@link Constants#DEFAULT_CONCATENATE_JSONB_HIBERNATE_OPERATOR} constant.
     */
    private final String concatenateJsonbOperator;

    public HibernateContext(String jsonbAllArrayStringsExistOperator, String jsonbAnyArrayStringsExistOperator, String jsonFunctionJsonArrayOperator,
                            String textFunctionOperator, String castFunctionOperator, String concatenateJsonbOperator) {
        this.jsonbAllArrayStringsExistOperator = jsonbAllArrayStringsExistOperator;
        this.jsonbAnyArrayStringsExistOperator = jsonbAnyArrayStringsExistOperator;
        this.jsonFunctionJsonArrayOperator = jsonFunctionJsonArrayOperator;
        this.textFunctionOperator = textFunctionOperator;
        this.castFunctionOperator = castFunctionOperator;
        this.concatenateJsonbOperator = concatenateJsonbOperator;
    }

    public static ContextBuilder builder() {
        return new ContextBuilder();
    }

    public String getConcatenateJsonbOperator() {
        return concatenateJsonbOperator;
    }

    public String getCastFunctionOperator() {
        return castFunctionOperator;
    }

    /**
     * Returns value of property {@link #textFunctionOperator}
     *
     * @return value of property {@link #textFunctionOperator}
     */
    public String getTextFunctionOperator() {
        return textFunctionOperator;
    }

    /**
     * Returns value of property {@link #jsonbAllArrayStringsExistOperator}
     *
     * @return value of property {@link #jsonbAllArrayStringsExistOperator}
     */
    public String getJsonbAllArrayStringsExistOperator() {
        return jsonbAllArrayStringsExistOperator;
    }

    /**
     * Returns value of property {@link #jsonbAnyArrayStringsExistOperator}
     *
     * @return value of property {@link #jsonbAnyArrayStringsExistOperator}
     */
    public String getJsonbAnyArrayStringsExistOperator() {
        return jsonbAnyArrayStringsExistOperator;
    }

    /**
     * Returns value of property {@link #jsonFunctionJsonArrayOperator}
     *
     * @return value of property {@link #jsonFunctionJsonArrayOperator}
     */
    public String getJsonFunctionJsonArrayOperator() {
        return jsonFunctionJsonArrayOperator;
    }

    public static class ContextBuilder {

        private String jsonbAllArrayStringsExistOperator = DEFAULT_JSONB_ALL_ARRAY_STRINGS_EXIST_HIBERNATE_OPERATOR;
        private String jsonbAnyArrayStringsExistOperator = DEFAULT_JSONB_ANY_ARRAY_STRINGS_EXIST_HIBERNATE_OPERATOR;
        private String jsonFunctionJsonArrayOperator = DEFAULT_JSON_FUNCTION_JSON_ARRAY_HIBERNATE_OPERATOR;
        private String textFunctionOperator = DEFAULT_TEXT_FUNCTION_HIBERNATE_OPERATOR;
        private String castFunctionOperator = DEFAULT_CAST_FUNCTION_HIBERNATE_OPERATOR;
        private String concatenateJsonbOperator = DEFAULT_CONCATENATE_JSONB_HIBERNATE_OPERATOR;

        public ContextBuilder withConcatenateJsonbOperator(String concatenateJsonbOperator) {
            this.concatenateJsonbOperator = concatenateJsonbOperator;
            return this;
        }

        public ContextBuilder withCastFunctionOperator(String castFunctionOperator) {
            this.castFunctionOperator = castFunctionOperator;
            return this;
        }

        public ContextBuilder withTextFunctionOperator(String textFunctionOperator) {
            this.textFunctionOperator = textFunctionOperator;
            return this;
        }

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
            return new HibernateContext(this.jsonbAllArrayStringsExistOperator, this.jsonbAnyArrayStringsExistOperator, this.jsonFunctionJsonArrayOperator,
                    this.textFunctionOperator, this.castFunctionOperator, concatenateJsonbOperator);
        }

        public ContextBuilder withHibernateContext(HibernateContext hibernateContext) {
            return withJsonbAllArrayStringsExistOperator(hibernateContext.getJsonbAllArrayStringsExistOperator())
                    .withJsonbAnyArrayStringsExistOperator(hibernateContext.getJsonbAnyArrayStringsExistOperator())
                    .withJsonFunctionJsonArrayOperator(hibernateContext.getJsonFunctionJsonArrayOperator())
                    .withTextFunctionOperator(hibernateContext.getTextFunctionOperator())
                    .withCastFunctionOperator(hibernateContext.getCastFunctionOperator())
                    .withConcatenateJsonbOperator(hibernateContext.getConcatenateJsonbOperator());
        }
    }
}
