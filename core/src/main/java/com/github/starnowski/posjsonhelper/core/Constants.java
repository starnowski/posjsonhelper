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

    /**
     * Name of the Postgres function 'jsonb_extract_path_text' that returns JSON value pointed to by path elements as text.
     * Please check <a href="https://www.postgresql.org/docs/9.4/functions-json.html">Postgres doc</a>
     */
    public static final String JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME = "jsonb_extract_path_text";
    /**
     * Name of the Postgres function 'jsonb_extract_path' that returns JSON value pointed to by path elements.
     * Please check <a href="https://www.postgresql.org/docs/9.4/functions-json.html">Postgres doc</a>
     */
    public static final String JSONB_EXTRACT_PATH_FUNCTION_NAME = "jsonb_extract_path";

    /**
     * Name of the Postgres function 'plainto_tsquery' plainto_tsquery transforms unformatted text querytext to tsquery.
     * The text is parsed and normalized much as for to_tsvector, then the &amp; (AND) Boolean operator is inserted between surviving words.
     * The to_tsquery offers access to more features than plainto_tsquery, but is less forgiving about its input.
     * Please check <a href="https://www.postgresql.org/docs/9.4/textsearch-controls.html">Postgres doc</a>
     */
    public static final String PLAINTO_TSQUERY_FUNCTION_NAME = "plainto_tsquery";
    /**
     * Name of the Postgres function 'phraseto_tsquery' phraseto_tsquery behaves much like plainto_tsquery, except that it
     * inserts the &lt;-&gt; (FOLLOWED BY) operator between surviving words instead of the &amp; (AND) operator.
     * Also, stop words are not simply discarded, but are accounted for by inserting &lt;N&gt; operators rather than &lt;-&gt; operators.
     * This function is useful when searching for exact lexeme sequences, since the FOLLOWED BY operators check lexeme order not just the presence of all the lexemes.
     * Please check <a href="https://www.postgresql.org/docs/9.4/textsearch-controls.html">Postgres doc</a>
     */
    public static final String PHRASETO_TSQUERY_FUNCTION_NAME = "phraseto_tsquery";
    /**
     * Name of the Postgres function 'websearch_to_tsquery' websearch_to_tsquery.
     * creates a tsquery value from querytext using an alternative syntax in which simple unformatted text is a valid query.
     * Unlike plainto_tsquery and phraseto_tsquery, it also recognizes certain operators.
     * Moreover, this function will never raise syntax errors, which makes it possible to use raw user-supplied input for search.
     * Please check <a href="https://www.postgresql.org/docs/11/textsearch-controls.html">Postgres doc</a>
     */
    public static final String WEBSEARCH_TO_TSQUERY_FUNCTION_NAME = "websearch_to_tsquery";
    /**
     * Name of the Postgres function 'to_tsvector' that is converting a document to the tsvector data type.
     * Please check <a href="https://www.postgresql.org/docs/9.4/textsearch-controls.html">Postgres doc</a>
     */
    public static final String TO_TSVECTOR_FUNCTION_NAME = "to_tsvector";
    /**
     * Name of the Postgres function 'jsonb_set' that replace json with new value.
     * Please check <a href="https://www.postgresql.org/docs/9.5/functions-json.html">Postgres doc</a>
     */
    public static final String JSONB_SET_FUNCTION_NAME = "jsonb_set";
    /**
     * Default value for property {@link Context#jsonbAllArrayStringsExistFunctionReference}
     */
    public static final String DEFAULT_JSONB_ALL_ARRAY_STRINGS_EXIST_FUNCTION_NAME = "jsonb_all_array_strings_exist";
    /**
     * Default value for property {@link Context#jsonbAnyArrayStringsExistFunctionReference}
     */
    public static final String DEFAULT_JSONB_ANY_ARRAY_STRINGS_EXIST_FUNCTION_NAME = "jsonb_any_array_strings_exist";

    public static final String DEFAULT_REMOVE_VALUES_FROM_JSON_ARRAY_FUNCTION_NAME = "remove_values_from_json_array";
    /**
     * System property that is being used to set the name of SQL function that checks if all passed elements as the text[] exist in the JSON array property.
     */
    public static final String JSONB_ALL_ARRAY_STRINGS_EXIST_FUNCTION_NAME_PROPERTY = "com.github.starnowski.posjsonhelper.core.functions.jsonb_all_array_strings_exist";
    /**
     * System property that is being used to set the name of SQL function that checks if any passed elements as the text[] exist in the JSON array property.
     */
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
     * 	The default name for the HQL function that wraps Postgres text "@@" operator.
     */
    public static final String DEFAULT_TEXT_FUNCTION_HIBERNATE_OPERATOR = "text_operator_function";
    /**
     * 	The default name for the HQL function that wraps Postgres cast "::" operator.
     */
    public static final String DEFAULT_CAST_FUNCTION_HIBERNATE_OPERATOR = "cast_operator_function";
    /**
     * 	The default name for the HQL function that wraps Postgres concatenate "||" operator.
     */
    public static final String DEFAULT_CONCATENATE_JSONB_HIBERNATE_OPERATOR = "concatenate_jsonb_operator_function";
    /**
     * 	The default name for the HQL function that wraps Postgres "#-" operator that deletes the field or array element at the specified path, where path elements can be either field keys or array indexes.
     */
    public static final String DEFAULT_DELETE_JSONB_BY_SPECIFIC_PATH_HIBERNATE_OPERATOR = "delete_jsonb_by_specific_path_hibernate_operator";
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
    /**
     * System property that stores list of {@link com.github.starnowski.posjsonhelper.core.sql.ISQLDefinitionContextFactory} types that should be loaded.
     * Instead of loading types that can be found on the classpath for package "com.github.starnowski.posjsonhelper".
     * Types on the list are separated by comma character ".".
     */
    public static final String SQLDEFINITIONCONTEXTFACTORY_TYPES_PROPERTY = "com.github.starnowski.posjsonhelper.core.hibernate.functions.sqldefinitioncontextfactory.types";
}
