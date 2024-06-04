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
package com.github.starnowski.posjsonhelper.hibernate6.operators;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import jakarta.persistence.criteria.Path;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.FunctionRenderer;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.produce.function.StandardFunctionReturnTypeResolvers;
import org.hibernate.query.sqm.tree.SqmTypedNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Implemented of HQL function defined by property {@link HibernateContext#concatenateJsonbOperator}.
 * It is wrapper for Concatenation operator.
 */
public class ConcatenateJsonbOperator extends SelfRenderingSqmFunction<String> implements Serializable {
    public ConcatenateJsonbOperator(NodeBuilder nodeBuilder, Path referencedPathSource, String value, HibernateContext hibernateContext) {
        this(nodeBuilder, referencedPathSource, new JsonbCastOperatorFunction(nodeBuilder, value, hibernateContext), hibernateContext);
    }

    public ConcatenateJsonbOperator(NodeBuilder nodeBuilder, Path referencedPathSource, SqmTypedNode value, HibernateContext hibernateContext) {
        this(nodeBuilder, (SqmTypedNode<?>)referencedPathSource, value, hibernateContext);
    }

    public ConcatenateJsonbOperator(NodeBuilder nodeBuilder, SqmTypedNode<?> referencedPathSource, String value, HibernateContext hibernateContext) {
        this(nodeBuilder, referencedPathSource, new JsonbCastOperatorFunction(nodeBuilder, value, hibernateContext), hibernateContext);
    }

    public ConcatenateJsonbOperator(NodeBuilder nodeBuilder, SqmTypedNode<?> referencedPathSource, SqmTypedNode value, HibernateContext hibernateContext) {
        super(
                nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(hibernateContext.getConcatenateJsonbOperator()),
                (FunctionRenderer) nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(hibernateContext.getConcatenateJsonbOperator()),
                mapParameters(nodeBuilder, referencedPathSource, value),
                null,
                null,
                StandardFunctionReturnTypeResolvers.useFirstNonNull(),
                nodeBuilder,
                hibernateContext.getConcatenateJsonbOperator());
    }

    private static List<? extends SqmTypedNode<?>> mapParameters(NodeBuilder nodeBuilder, SqmTypedNode<?> referencedPathSource, SqmTypedNode value) {
        List<SqmTypedNode<?>> result = new ArrayList<>();
        result.add(referencedPathSource);
        result.add((SqmTypedNode<String>) nodeBuilder.value(value));
        return result;
    }
}
