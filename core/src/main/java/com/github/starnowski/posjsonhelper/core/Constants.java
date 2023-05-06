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

public class Constants {

    public static final String JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME = "jsonb_extract_path_text";
    public static final String JSONB_EXTRACT_PATH_FUNCTION_NAME = "jsonb_extract_path";
    public static final String DEFAULT_JSONB_ALL_ARRAY_STRINGS_EXIST_FUNCTION_NAME = "jsonb_all_array_strings_exist";
    public static final String DEFAULT_JSONB_ANY_ARRAY_STRINGS_EXIST_FUNCTION_NAME = "jsonb_any_array_strings_exist";
    public static final String JSONB_ALL_ARRAY_STRINGS_EXIST_FUNCTION_NAME_PROPERTY = "com.github.starnowski.posjsonhelper.core.functions.jsonb_all_array_strings_exist";
    public static final String JSONB_ANY_ARRAY_STRINGS_EXIST_FUNCTION_NAME_PROPERTY = "com.github.starnowski.posjsonhelper.core.functions.jsonb_any_array_strings_exist";
    /**
     * 	System property that is being used to set the name of database schema where the SQL functions should be created
     */
    public static final String SCHEMA_PROPERTY = "com.github.starnowski.posjsonhelper.core.schema";
    /**
     * 	The default name for the HQL function that wraps SQL function defined in core context {@link Context#jsonbAllArrayStringsExistFunctionReference}.
     */
    public static final String DEFAULT_JSONB_ALL_ARRAY_STRINGS_EXIST_HIBERNATE_OPERATOR = "jsonb_all_array_strings_exist";
    /**
     * 	The default name for the HQL function that wraps SQL function defined in core context {@link Context#jsonbAnyArrayStringsExistFunctionReference}.
     */
    public static final String DEFAULT_JSONB_ANY_ARRAY_STRINGS_EXIST_HIBERNATE_OPERATOR = "jsonb_any_array_strings_exist";
    /**
     * 	The default name for the HQL function that wraps Postgres "ARRAY" operator.
     */
    public static final String DEFAULT_JSON_FUNCTION_JSON_ARRAY_HIBERNATE_OPERATOR = "json_function_json_array";
    /**
     * System property that is being used to set the name of HQL function that invokes SQL function defined in core context {@link Context#jsonbAllArrayStringsExistFunctionReference}.
     */
    public static final String JSONB_ALL_ARRAY_STRINGS_EXIST_HIBERNATE_OPERATOR_PROPERTY = "com.github.starnowski.posjsonhelper.core.hibernate.functions.jsonb_all_array_strings_exist";
    /**
     * System property that is being used to set the name of HQL function that invokes SQL function defined in core context {@link Context#jsonbAnyArrayStringsExistFunctionReference}.
     */
    public static final String JSONB_ANY_ARRAY_STRINGS_EXIST_HIBERNATE_OPERATOR_PROPERTY = "com.github.starnowski.posjsonhelper.core.hibernate.functions.jsonb_any_array_strings_exist";
    /**
     * System property that is being used to set the name of HQL function that wraps the array operator in Postgresql.
     */
    public static final String JSON_FUNCTION_JSON_ARRAY_HIBERNATE_OPERATOR_PROPERTY = "com.github.starnowski.posjsonhelper.core.hibernate.functions.json_function_json_array";
}
