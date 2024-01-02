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
package com.github.starnowski.posjsonhelper.text.hibernate6.functions;

import jakarta.persistence.criteria.Path;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.FunctionRenderer;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.produce.function.StandardFunctionReturnTypeResolvers;
import org.hibernate.query.sqm.tree.SqmTypedNode;
import org.hibernate.query.sqm.tree.expression.SqmExpression;
import org.hibernate.type.StandardBasicTypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.github.starnowski.posjsonhelper.core.Constants.TO_TSVECTOR_FUNCTION_NAME;

public class TSVectorFunction extends SelfRenderingSqmFunction<String> implements Serializable {
    public TSVectorFunction(Path referencedPathSource, NodeBuilder nodeBuilder) {
        this(referencedPathSource, (String) null, nodeBuilder);
    }

    public TSVectorFunction(Path referencedPathSource, String configuration, NodeBuilder nodeBuilder) {
        super(nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(TO_TSVECTOR_FUNCTION_NAME),
                (FunctionRenderer) nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(TO_TSVECTOR_FUNCTION_NAME),
                contactParameters(referencedPathSource, configuration, nodeBuilder),
                null,
                null,
                StandardFunctionReturnTypeResolvers.invariant(nodeBuilder.getTypeConfiguration().getBasicTypeRegistry().resolve(StandardBasicTypes.STRING)),
                nodeBuilder,
                TO_TSVECTOR_FUNCTION_NAME);
    }

    public TSVectorFunction(Path referencedPathSource, SqmExpression<?> configuration, NodeBuilder nodeBuilder) {
        super(nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(TO_TSVECTOR_FUNCTION_NAME),
                (FunctionRenderer) nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(TO_TSVECTOR_FUNCTION_NAME),
                contactParameters(referencedPathSource, configuration),
                null,
                null,
                StandardFunctionReturnTypeResolvers.invariant(nodeBuilder.getTypeConfiguration().getBasicTypeRegistry().resolve(StandardBasicTypes.STRING)),
                nodeBuilder,
                TO_TSVECTOR_FUNCTION_NAME);
    }

    private static List<? extends SqmTypedNode<?>> contactParameters(Path referencedPathSource, String configuration, NodeBuilder nodeBuilder) {
        if (referencedPathSource == null) {
            throw new IllegalArgumentException("ReferencedPathSource argument can not be null");
        }
        List<SqmTypedNode<?>> result = new ArrayList<>();
        if (configuration != null) {
            result.add(nodeBuilder.literal(configuration));
        }
        result.add((SqmTypedNode<?>) referencedPathSource);
        return result;
    }

    private static List<? extends SqmTypedNode<?>> contactParameters(Path referencedPathSource, SqmExpression<?> configuration) {
        if (referencedPathSource == null) {
            throw new IllegalArgumentException("ReferencedPathSource argument can not be null");
        }
        List<SqmTypedNode<?>> result = new ArrayList<>();
        if (configuration != null) {
            result.add(configuration);
        }
        result.add((SqmTypedNode<?>) referencedPathSource);
        return result;
    }
}
