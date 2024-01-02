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
import org.hibernate.query.sqm.tree.SqmCopyContext;
import org.hibernate.query.sqm.tree.expression.SqmExpression;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CastOperatorFunction extends SelfRenderingSqmFunction<String> implements Serializable {

    private final HibernateContext context;
    private final SqmExpression<String> type;
    private final SqmExpression<?> argument;

    /**
     * @param nodeBuilder      component of type {@link NodeBuilder}
     * @param argument         value for cast operator
     * @param type             type to which values should be cast
     * @param hibernateContext context object of type {@link HibernateContext}
     */
    public CastOperatorFunction(NodeBuilder nodeBuilder, SqmExpression<?> argument, String type, HibernateContext hibernateContext) {
        this(nodeBuilder, argument, nodeBuilder.literal(type), hibernateContext);
    }

    /**
     * @param nodeBuilder      component of type {@link NodeBuilder}
     * @param argument         value for cast operator
     * @param type             type to which values should be cast
     * @param hibernateContext context object of type {@link HibernateContext}
     */
    public CastOperatorFunction(NodeBuilder nodeBuilder, SqmExpression<?> argument, SqmExpression<String> type, HibernateContext hibernateContext) {
        super(nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(hibernateContext.getCastFunctionOperator()),
                (FunctionRenderer) nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(hibernateContext.getCastFunctionOperator()),
                contactParameters(argument, type),
                null,
                null,
                StandardFunctionReturnTypeResolvers.useFirstNonNull(),
                nodeBuilder,
                hibernateContext.getCastFunctionOperator());
        this.context = hibernateContext;
        this.argument = argument;
        this.type = type;
    }

    private static List<? extends SqmExpression<?>> contactParameters(SqmExpression<?> argument, SqmExpression<String> type) {
        if (argument == null) {
            throw new IllegalArgumentException("Argument argument can not be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Type argument can not be null");
        }
        List<SqmExpression<?>> result = new ArrayList<>();
        result.add(argument);
        result.add(type);
        return result;
    }

    public CastOperatorFunction copy(SqmCopyContext context) {
        CastOperatorFunction existing = context.getCopy(this);
        if (existing != null) {
            return existing;
        } else {
            CastOperatorFunction predicate = context.registerCopy(this, new CastOperatorFunction(nodeBuilder(), this.argument, this.type, this.context));
            this.copyTo(predicate, context);
            return predicate;
        }
    }
}
