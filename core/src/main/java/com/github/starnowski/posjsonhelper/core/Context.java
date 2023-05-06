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
package com.github.starnowski.posjsonhelper.core;

import static com.github.starnowski.posjsonhelper.core.Constants.DEFAULT_JSONB_ALL_ARRAY_STRINGS_EXIST_FUNCTION_NAME;
import static com.github.starnowski.posjsonhelper.core.Constants.DEFAULT_JSONB_ANY_ARRAY_STRINGS_EXIST_FUNCTION_NAME;

/**
 * Component that store properties used by core components
 */
public class Context {

    /**
     * Name of SQL function that checks if all passed elements as the text[] exist in the JSON array property.
     * By default, the property is initialized with the value of  {@link Constants#DEFAULT_JSONB_ALL_ARRAY_STRINGS_EXIST_FUNCTION_NAME} constant.
     */
    private final String jsonbAllArrayStringsExistFunctionReference;
    /**
     * Name of SQL function that checks if any passed elements as the text[] exist in the JSON array property.
     * By default, the property is initialized with the value of  {@link Constants#DEFAULT_JSONB_ANY_ARRAY_STRINGS_EXIST_FUNCTION_NAME} constant.
     */
    private final String jsonbAnyArrayStringsExistFunctionReference;
    /**
     * Name of database schema where the SQL functions should be created.
     * By default, the property is initialized with the null value.
     */
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

        public ContextBuilder withContext(Context context) {
            return withJsonbAllArrayStringsExistFunctionReference(context.getJsonbAllArrayStringsExistFunctionReference())
                    .withJsonbAnyArrayStringsExistFunctionReference(context.getJsonbAnyArrayStringsExistFunctionReference())
                    .withSchema(context.getSchema());
        }

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
