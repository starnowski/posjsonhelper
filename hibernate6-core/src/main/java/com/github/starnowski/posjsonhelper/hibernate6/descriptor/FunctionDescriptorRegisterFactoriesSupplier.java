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
package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.core.SystemPropertyReader;
import org.reflections.Reflections;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.github.starnowski.posjsonhelper.hibernate6.Constants.FUNCTIONDESCRIPTORREGISTERFACTORY_TYPES_EXCLUDED_PROPERTY;
import static com.github.starnowski.posjsonhelper.hibernate6.Constants.FUNCTIONDESCRIPTORREGISTERFACTORY_TYPES_PROPERTY;
import static java.util.Arrays.asList;

/**
 * Loads instances of type {{@link  FunctionDescriptorRegisterFactory}}.
 * By default, component scans package "com.github.starnowski.posjsonhelper" to search correct types.
 * If there is specified system property "com.github.starnowski.posjsonhelper.hibernate6.functiondescriptorregisterfactory.types"
 * then the component tries to load classes specified by this property.
 *
 * @see #PACKAGE_TO_SCAN
 * @see com.github.starnowski.posjsonhelper.hibernate6.Constants#FUNCTIONDESCRIPTORREGISTERFACTORY_TYPES_PROPERTY
 */
public class FunctionDescriptorRegisterFactoriesSupplier {

    /**
     * Package that is supposes to scan to search subtypes of  {{@link  FunctionDescriptorRegisterFactory}}
     */
    public static final String PACKAGE_TO_SCAN = "com.github.starnowski.posjsonhelper";
    private final Supplier<Reflections> reflectionsSupplier;
    private final SystemPropertyReader systemPropertyReader;

    public FunctionDescriptorRegisterFactoriesSupplier() {
        this(() -> new Reflections(PACKAGE_TO_SCAN), new SystemPropertyReader());
    }

    public FunctionDescriptorRegisterFactoriesSupplier(Supplier<Reflections> reflectionsSupplier, SystemPropertyReader systemPropertyReader) {
        this.reflectionsSupplier = reflectionsSupplier;
        this.systemPropertyReader = systemPropertyReader;
    }

    public List<FunctionDescriptorRegisterFactory> get() {
        String excludedTypesProperty = systemPropertyReader.read(FUNCTIONDESCRIPTORREGISTERFACTORY_TYPES_EXCLUDED_PROPERTY);
        Set<String> excludedTypes = excludedTypesProperty == null ? new HashSet<>() : new HashSet<>(asList(excludedTypesProperty.split(",")));
        String factoriesPropertyValue = systemPropertyReader.read(FUNCTIONDESCRIPTORREGISTERFACTORY_TYPES_PROPERTY);
        Set<Class<? extends FunctionDescriptorRegisterFactory>> types = null;
        if (factoriesPropertyValue != null) {
            types = Arrays.stream(factoriesPropertyValue.split(",")).filter(v -> !excludedTypes.contains(v)).map(value -> {
                try {
                    return (Class<? extends FunctionDescriptorRegisterFactory>) Class.forName(value);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("No such class for name " + value, e);
                }
            }).collect(Collectors.toSet());
        } else {
            types = reflectionsSupplier.get().getSubTypesOf(FunctionDescriptorRegisterFactory.class);
        }
        List<FunctionDescriptorRegisterFactory> results = new ArrayList<>();
        for (Class<? extends FunctionDescriptorRegisterFactory> type : types) {
            try {
                if (excludedTypes.contains(type.getName())) {
                    continue;
                }
                results.add(type.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                throw new RuntimeException("Unable to create instance of class with default constructor", e);
            }
        }
        return results;
    }
}
