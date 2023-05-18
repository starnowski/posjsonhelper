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

import static com.github.starnowski.posjsonhelper.core.Constants.JSONB_ALL_ARRAY_STRINGS_EXIST_FUNCTION_NAME_PROPERTY;
import static com.github.starnowski.posjsonhelper.core.Constants.JSONB_ANY_ARRAY_STRINGS_EXIST_FUNCTION_NAME_PROPERTY;
import static com.github.starnowski.posjsonhelper.core.Constants.SCHEMA_PROPERTY;

/**
 * Supplier of  {@link Context} based on system properties.
 */
public class CoreContextPropertiesSupplier {

    private final SystemPropertyReader systemPropertyReader;

    public CoreContextPropertiesSupplier() {
        this(new SystemPropertyReader());
    }

    CoreContextPropertiesSupplier(SystemPropertyReader systemPropertyReader) {
        this.systemPropertyReader = systemPropertyReader;
    }

    /**
     * Generates object to type {@link Context}, based on system properties.
     * Component sets below properties:
     * {@link Context#jsonbAllArrayStringsExistFunctionReference} based on {@link Constants#JSONB_ALL_ARRAY_STRINGS_EXIST_FUNCTION_NAME_PROPERTY}
     * {@link Context#jsonbAnyArrayStringsExistFunctionReference} based on {@link Constants#JSONB_ANY_ARRAY_STRINGS_EXIST_FUNCTION_NAME_PROPERTY}
     * {@link Context#schema} based on {@link Constants#SCHEMA_PROPERTY}
     * @return core context
     */
    public Context get(){
        Context.ContextBuilder builder = Context.builder();
        String jsonbAllArrayStringsExistFunctionReference = systemPropertyReader.read(JSONB_ALL_ARRAY_STRINGS_EXIST_FUNCTION_NAME_PROPERTY);
        if (jsonbAllArrayStringsExistFunctionReference != null) {
            builder = builder.withJsonbAllArrayStringsExistFunctionReference(jsonbAllArrayStringsExistFunctionReference);
        }
        String jsonbAnyArrayStringsExistFunctionReference = systemPropertyReader.read(JSONB_ANY_ARRAY_STRINGS_EXIST_FUNCTION_NAME_PROPERTY);
        if (jsonbAnyArrayStringsExistFunctionReference != null) {
            builder = builder.withJsonbAnyArrayStringsExistFunctionReference(jsonbAnyArrayStringsExistFunctionReference);
        }
        String schema = systemPropertyReader.read(SCHEMA_PROPERTY);
        if (schema != null) {
            builder = builder.withSchema(schema);
        }
        return builder.build();
    }

    SystemPropertyReader getSystemPropertyReader() {
        return systemPropertyReader;
    }
}
