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

import static com.github.starnowski.posjsonhelper.core.Constants.*;

/**
 * Supplier of  {@link HibernateContext} based on system properties.
 */
public class HibernateContextPropertiesSupplier {

    private final SystemPropertyReader systemPropertyReader;

    public HibernateContextPropertiesSupplier() {
        this(new SystemPropertyReader());
    }

    HibernateContextPropertiesSupplier(SystemPropertyReader systemPropertyReader) {
        this.systemPropertyReader = systemPropertyReader;
    }

    /**
     * Generates object to type {@link HibernateContext}, based on system properties.
     * Component sets below properties:
     * {@link HibernateContext#jsonbAllArrayStringsExistOperator} based on {@link Constants#JSONB_ALL_ARRAY_STRINGS_EXIST_HIBERNATE_OPERATOR_PROPERTY}
     * {@link HibernateContext#jsonbAnyArrayStringsExistOperator} based on {@link Constants#JSONB_ANY_ARRAY_STRINGS_EXIST_HIBERNATE_OPERATOR_PROPERTY}
     * {@link HibernateContext#jsonFunctionJsonArrayOperator} based on {@link Constants#JSON_FUNCTION_JSON_ARRAY_HIBERNATE_OPERATOR_PROPERTY}
     * @return
     */
    public HibernateContext get(){
        HibernateContext.ContextBuilder builder = HibernateContext.builder();
        String jsonbAllArrayStringsExist = systemPropertyReader.read(JSONB_ALL_ARRAY_STRINGS_EXIST_HIBERNATE_OPERATOR_PROPERTY);
        if (jsonbAllArrayStringsExist != null) {
            builder = builder.withJsonbAllArrayStringsExistOperator(jsonbAllArrayStringsExist);
        }
        String jsonbAnyArrayStringsExist = systemPropertyReader.read(JSONB_ANY_ARRAY_STRINGS_EXIST_HIBERNATE_OPERATOR_PROPERTY);
        if (jsonbAnyArrayStringsExist != null) {
            builder = builder.withJsonbAnyArrayStringsExistOperator(jsonbAnyArrayStringsExist);
        }
        String jsonFunctionJsonArrayOperator = systemPropertyReader.read(JSON_FUNCTION_JSON_ARRAY_HIBERNATE_OPERATOR_PROPERTY);
        if (jsonFunctionJsonArrayOperator != null) {
            builder = builder.withJsonFunctionJsonArrayOperator(jsonFunctionJsonArrayOperator);
        }
        return builder.build();
    }

    SystemPropertyReader getSystemPropertyReader() {
        return systemPropertyReader;
    }
}
