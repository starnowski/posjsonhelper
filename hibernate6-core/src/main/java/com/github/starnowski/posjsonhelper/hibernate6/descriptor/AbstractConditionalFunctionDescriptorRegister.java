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

import org.hibernate.query.sqm.function.SqmFunctionDescriptor;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

/**
 * Abstract type, responsible for registration of custom functions.
 * Implementation of interface {@link FunctionDescriptorRegister}
 *
 */
public abstract class AbstractConditionalFunctionDescriptorRegister implements FunctionDescriptorRegister{

    /**
     * Flag determines if in the case when the function is already registered if it can be overridden.
     */
    final boolean shouldOverrideFunctionIfAlreadyRegistered;

    /**
     *
     * @param shouldOverrideFunctionIfAlreadyRegistered value of property {@link #shouldOverrideFunctionIfAlreadyRegistered}
     */
    protected AbstractConditionalFunctionDescriptorRegister(boolean shouldOverrideFunctionIfAlreadyRegistered) {
        this.shouldOverrideFunctionIfAlreadyRegistered = shouldOverrideFunctionIfAlreadyRegistered;
    }

    /**
     * Method that register function with {@link SqmFunctionRegistry}.
     * First, in the method, it is checked if the function is already registered.
     * If the function is not register or the function actually already is but the {@link  #shouldOverrideFunctionIfAlreadyRegistered}
     * has the value true then the method set or overrides the function
     *
     * @param sqmFunctionRegistry object of type SqmFunctionRegistry that is going to be used for function registration
     * @return function descriptor
     */
    public SqmFunctionDescriptor registerFunction(SqmFunctionRegistry sqmFunctionRegistry) {
        SqmFunctionDescriptor functionDescriptor = sqmFunctionRegistry.findFunctionDescriptor(getHqlFunctionName());
        return functionDescriptor == null || isShouldOverrideFunctionIfAlreadyRegistered() ? register(sqmFunctionRegistry) : functionDescriptor;
    }

    /**
     * Method that register function with {@link SqmFunctionRegistry}
     * @param registry object of type SqmFunctionRegistry that is going to be used for function registration
     * @return function descriptor
     */
    protected abstract SqmFunctionDescriptor register(SqmFunctionRegistry registry);

    /**
     * Name of HQL function, with which components would try to register function.
     * @return name of HQL function, with which components would try to register function
     */
    protected abstract String getHqlFunctionName();

    /**
     * Returns value of property {@link #shouldOverrideFunctionIfAlreadyRegistered}
     * @return value of property {@link #shouldOverrideFunctionIfAlreadyRegistered}
     */
    public boolean isShouldOverrideFunctionIfAlreadyRegistered() {
        return shouldOverrideFunctionIfAlreadyRegistered;
    }
}
