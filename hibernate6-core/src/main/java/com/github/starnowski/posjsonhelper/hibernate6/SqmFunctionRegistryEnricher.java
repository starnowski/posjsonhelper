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

import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.core.CoreContextPropertiesSupplier;
import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.core.HibernateContextPropertiesSupplier;
import com.github.starnowski.posjsonhelper.hibernate6.descriptor.FunctionDescriptorRegisterFactoriesSupplier;
import com.github.starnowski.posjsonhelper.hibernate6.descriptor.FunctionDescriptorRegisterFactory;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

import java.util.List;

/**
 * The component that enriches the {@link SqmFunctionRegistry} object with Hibernate and SQL definitions used by the library.
 */
public class SqmFunctionRegistryEnricher {

    /**
     * Supplier for {@link Context} object based on system properties
     */
    private final CoreContextPropertiesSupplier coreContextPropertiesSupplier;
    /**
     * Supplier for {@link HibernateContext} object based on system properties
     */
    private final HibernateContextPropertiesSupplier hibernateContextPropertiesSupplier;
    private final List<FunctionDescriptorRegisterFactory> functionDescriptorRegisterFactories;

    public SqmFunctionRegistryEnricher() {
        this(new CoreContextPropertiesSupplier(), new HibernateContextPropertiesSupplier(), new FunctionDescriptorRegisterFactoriesSupplier());
    }

    SqmFunctionRegistryEnricher(CoreContextPropertiesSupplier coreContextPropertiesSupplier, HibernateContextPropertiesSupplier hibernateContextPropertiesSupplier, FunctionDescriptorRegisterFactoriesSupplier functionDescriptorRegisterFactoriesSupplier) {
        this.coreContextPropertiesSupplier = coreContextPropertiesSupplier;
        this.hibernateContextPropertiesSupplier = hibernateContextPropertiesSupplier;
        this.functionDescriptorRegisterFactories = functionDescriptorRegisterFactoriesSupplier.get();
    }

    public void enrich(SqmFunctionRegistry sqmFunctionRegistry) {
        Context context = coreContextPropertiesSupplier.get();
        HibernateContext hibernateContext = hibernateContextPropertiesSupplier.get();
        this.enrich(sqmFunctionRegistry, context, hibernateContext);
    }

    public void enrich(SqmFunctionRegistry sqmFunctionRegistry, Context context, HibernateContext hibernateContext) {
        functionDescriptorRegisterFactories.stream().map(supplier -> supplier.get(context, hibernateContext)).forEach(functionDescriptorRegister ->
                functionDescriptorRegister.registerFunction(sqmFunctionRegistry));
    }
}
