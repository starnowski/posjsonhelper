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
package com.github.starnowski.posjsonhelper.text.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.descriptor.AbstractConditionalFunctionDescriptorRegister;
import org.hibernate.query.sqm.function.SqmFunctionDescriptor;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

/**
 * Type extends {@link  AbstractConditionalFunctionDescriptorRegister} type.
 * Responsible for register of hql function that is going to be rendered to the postgres text operator "@@".
 * It uses component of type {@link TextOperatorFunctionDescriptor} for rendering.
 * As key the component use {@link HibernateContext#getTextFunctionOperator()} ()}
 */
public class TextOperatorFunctionDescriptorRegister extends AbstractConditionalFunctionDescriptorRegister {

    private final HibernateContext hibernateContext;

    public TextOperatorFunctionDescriptorRegister(HibernateContext hibernateContext, boolean shouldTryToRegisterFunction) {
        super(shouldTryToRegisterFunction);
        this.hibernateContext = hibernateContext;
    }

    @Override
    protected SqmFunctionDescriptor register(SqmFunctionRegistry registry) {
        return registry.register(getHqlFunctionName(),
                new TextOperatorFunctionDescriptor(hibernateContext));
    }

    @Override
    protected String getHqlFunctionName() {
        return hibernateContext.getTextFunctionOperator();
    }
}