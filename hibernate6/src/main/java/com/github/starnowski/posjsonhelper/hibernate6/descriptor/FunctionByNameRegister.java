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
 * Type extends {@link AbstractConditionalFunctionDescriptorRegister} type.
 * It registers function with key {@link #hqlFunctionName}
 * and link it with SQL function with name {@link #sqlFunctionName}.
 */
public class FunctionByNameRegister extends AbstractConditionalFunctionDescriptorRegister {

    /**
     * Key on which the hql function is going to be registered
     */
    private final String hqlFunctionName;
    /**
     * Name of sql function
     */
    private final String sqlFunctionName;

    /**
     *
     * @param hqlFunctionName value of {@link #hqlFunctionName}
     * @param sqlFunctionName value of {@link #sqlFunctionName}
     * @param shouldOverrideFunctionIfAlreadyRegistered value of {{@link #shouldOverrideFunctionIfAlreadyRegistered}}
     */
    public FunctionByNameRegister(String hqlFunctionName, String sqlFunctionName, boolean shouldOverrideFunctionIfAlreadyRegistered) {
        super(shouldOverrideFunctionIfAlreadyRegistered);
        this.hqlFunctionName = hqlFunctionName;
        this.sqlFunctionName = sqlFunctionName;
    }

    @Override
    protected SqmFunctionDescriptor register(SqmFunctionRegistry registry) {
        return registry.namedDescriptorBuilder(hqlFunctionName, sqlFunctionName).register();
    }

    @Override
    protected String getHqlFunctionName() {
        return hqlFunctionName;
    }
}
