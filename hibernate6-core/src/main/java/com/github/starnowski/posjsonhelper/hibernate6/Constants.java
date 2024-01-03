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
package com.github.starnowski.posjsonhelper.hibernate6;

public class Constants {
    /**
     * System property that stores list of {@link com.github.starnowski.posjsonhelper.hibernate6.descriptor.FunctionDescriptorRegisterFactoriesSupplier} types that should be loaded.
     * Instead of loading types that can be found on the classpath for package "com.github.starnowski.posjsonhelper".
     * Types on the list are separated by comma character ".".
     */
    public static final String FUNCTIONDESCRIPTORREGISTERFACTORY_TYPES_PROPERTY = "com.github.starnowski.posjsonhelper.hibernate6.functiondescriptorregisterfactory.types";
    /**
     * System property that stores list of {@link com.github.starnowski.posjsonhelper.hibernate6.descriptor.FunctionDescriptorRegisterFactoriesSupplier} types that should be excluded from loading.
     * If {@link #FUNCTIONDESCRIPTORREGISTERFACTORY_TYPES_PROPERTY} property is also specified then {@link #FUNCTIONDESCRIPTORREGISTERFACTORY_TYPES_EXCLUDED_PROPERTY} has higher priority.
     * Types on the list are separated by comma character ".".
     */
    public static final String FUNCTIONDESCRIPTORREGISTERFACTORY_TYPES_EXCLUDED_PROPERTY = "com.github.starnowski.posjsonhelper.hibernate6.functiondescriptorregisterfactory.types.excluded";
}
