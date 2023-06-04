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
import com.github.starnowski.posjsonhelper.hibernate6.descriptor.AbstractConditionalFunctionDescriptorRegister;
import com.github.starnowski.posjsonhelper.hibernate6.descriptor.FunctionByNameRegister;
import com.github.starnowski.posjsonhelper.hibernate6.descriptor.JsonArrayFunctionDescriptorRegister;
import org.hibernate.dialect.PostgreSQL81Dialect;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

import java.util.List;

import static com.github.starnowski.posjsonhelper.core.Constants.JSONB_EXTRACT_PATH_FUNCTION_NAME;
import static com.github.starnowski.posjsonhelper.core.Constants.JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME;

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

    public SqmFunctionRegistryEnricher() {
        this(new CoreContextPropertiesSupplier(), new HibernateContextPropertiesSupplier());
    }

    SqmFunctionRegistryEnricher(CoreContextPropertiesSupplier coreContextPropertiesSupplier, HibernateContextPropertiesSupplier hibernateContextPropertiesSupplier) {
        this.coreContextPropertiesSupplier = coreContextPropertiesSupplier;
        this.hibernateContextPropertiesSupplier = hibernateContextPropertiesSupplier;
    }

    interface FunctionDescriptorRegisterSupplier {

        AbstractConditionalFunctionDescriptorRegister get(Context context, HibernateContext hibernateContext);
    }

    private final List<FunctionDescriptorRegisterSupplier> functionDescriptorRegisterSuppliers = List.of(
            (context, hibernateContext) ->
                    new FunctionByNameRegister(JSONB_EXTRACT_PATH_FUNCTION_NAME, JSONB_EXTRACT_PATH_FUNCTION_NAME, true),
            (context, hibernateContext) ->
                    new FunctionByNameRegister(JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME, JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME, true),
            (context, hibernateContext) ->
                    new FunctionByNameRegister(hibernateContext.getJsonbAllArrayStringsExistOperator(), context.getJsonbAllArrayStringsExistFunctionReference(), true),
            (context, hibernateContext) ->
                    new FunctionByNameRegister(hibernateContext.getJsonbAnyArrayStringsExistOperator(), context.getJsonbAnyArrayStringsExistFunctionReference(), true),
            (context, hibernateContext) ->
                    new JsonArrayFunctionDescriptorRegister(hibernateContext, true)
    );

    public void enrich(SqmFunctionRegistry sqmFunctionRegistry) {
        Context context = coreContextPropertiesSupplier.get();
        HibernateContext hibernateContext = hibernateContextPropertiesSupplier.get();
        this.enrich(sqmFunctionRegistry, context, hibernateContext);
    }

    public void enrich(SqmFunctionRegistry sqmFunctionRegistry, Context context, HibernateContext hibernateContext) {
        functionDescriptorRegisterSuppliers.stream().map(supplier -> supplier.get(context, hibernateContext)).forEach(functionDescriptorRegister ->
                functionDescriptorRegister.registerFunction(sqmFunctionRegistry));
    }
}
