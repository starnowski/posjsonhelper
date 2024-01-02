/**
 * Posjsonhelper library is an open-source project that adds support of
 * Hibernate query for https://www.postgresql.org/docs/10/functions-json.html)
 * <p>
 * Copyright (C) 2023  Szymon Tarnowski
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package com.github.starnowski.posjsonhelper.hibernate6.operators;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.FunctionRenderer;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.produce.function.StandardFunctionReturnTypeResolvers;
import org.hibernate.query.sqm.tree.expression.SqmExpression;

import java.io.Serializable;
import java.util.List;

/**
 * Component that generates SQM nodes for array operation.
 * It uses value returned by {@link HibernateContext#getJsonFunctionJsonArrayOperator()} method as function name.
 *
 * @see com.github.starnowski.posjsonhelper.hibernate6.descriptor.JsonArrayFunctionDescriptor
 */
public class JsonArrayFunction extends SelfRenderingSqmFunction<String> implements Serializable {

    /**
     * @param nodeBuilder      node builder {@link NodeBuilder}
     * @param arguments        array of values passed to as argument for function
     * @param hibernateContext context object of type {@link HibernateContext}
     */
    public JsonArrayFunction(NodeBuilder nodeBuilder, List<SqmExpression<String>> arguments, HibernateContext hibernateContext) {
        super(nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(hibernateContext.getJsonFunctionJsonArrayOperator()),
                (FunctionRenderer) nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(hibernateContext.getJsonFunctionJsonArrayOperator()),
                arguments,
                null,
                null,
                StandardFunctionReturnTypeResolvers.useFirstNonNull(),
                nodeBuilder,
                hibernateContext.getJsonFunctionJsonArrayOperator());
    }
}
