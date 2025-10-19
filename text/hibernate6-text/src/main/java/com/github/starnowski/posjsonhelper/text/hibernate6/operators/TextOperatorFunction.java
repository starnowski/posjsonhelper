/**
 * Posjsonhelper library is an open-source project that adds support of
 * Hibernate query for https://www.postgresql.org/docs/10/functions-json.html)
 * <p/>
 * Copyright (C) 2023  Szymon Tarnowski
 * <p/>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package com.github.starnowski.posjsonhelper.text.hibernate6.operators;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.text.hibernate6.functions.TSVectorFunction;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.FunctionRenderer;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.produce.function.StandardFunctionReturnTypeResolvers;
import org.hibernate.query.sqm.tree.SqmCopyContext;
import org.hibernate.query.sqm.tree.expression.SqmExpression;
import org.hibernate.type.StandardBasicTypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Component that generates SQM nodes for the <a href="https://www.postgresql.org/docs/9.4/textsearch-controls.html">text operator</a> function.
 * It uses value returned by {@link HibernateContext#getTextFunctionOperator()} method as function name.
 *
 * @see com.github.starnowski.posjsonhelper.text.hibernate6.descriptor.TextOperatorFunctionDescriptor
 */
public class TextOperatorFunction extends SelfRenderingSqmFunction<Boolean> implements Serializable {

    private final HibernateContext context;
    private final SqmExpression<String> tsVectorFunction;
    private final SqmExpression<String> argument;

    /**
     * @param nodeBuilder      component of type {@link NodeBuilder}
     * @param tsVectorFunction object of type {@link TSVectorFunction}
     * @param argument         text query expression
     * @param hibernateContext context object of type {@link HibernateContext}
     */
    public TextOperatorFunction(NodeBuilder nodeBuilder, SqmExpression<String> tsVectorFunction, SqmExpression<String> argument, HibernateContext hibernateContext) {
        super(nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(hibernateContext.getTextFunctionOperator()),
                (FunctionRenderer) nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(hibernateContext.getTextFunctionOperator()),
                contactParameters(tsVectorFunction, argument),
                null,
                null,
                StandardFunctionReturnTypeResolvers.invariant(nodeBuilder.getTypeConfiguration().getBasicTypeRegistry().resolve(StandardBasicTypes.BOOLEAN)),
                nodeBuilder,
                hibernateContext.getTextFunctionOperator());
        this.context = hibernateContext;
        this.tsVectorFunction = tsVectorFunction;
        this.argument = argument;
    }

    private static List<? extends SqmExpression<String>> contactParameters(SqmExpression tsVectorFunction,
                                                                           SqmExpression<String> argument) {
        if (tsVectorFunction == null) {
            throw new IllegalArgumentException("TSVectorFunction argument can not be null");
        }
        List<SqmExpression<String>> result = new ArrayList<>();
        result.add(tsVectorFunction);
        result.add(argument);
        return result;
    }

    public TextOperatorFunction copy(SqmCopyContext context) {
        TextOperatorFunction existing = context.getCopy(this);
        if (existing != null) {
            return existing;
        } else {
            TextOperatorFunction predicate = context.registerCopy(this, new TextOperatorFunction(nodeBuilder(), this.tsVectorFunction, this.argument, this.context));
            this.copyTo(predicate, context);
            return predicate;
        }
    }
}
