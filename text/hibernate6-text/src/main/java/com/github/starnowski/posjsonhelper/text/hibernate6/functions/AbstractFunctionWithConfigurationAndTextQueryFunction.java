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

public abstract class AbstractFunctionWithConfigurationAndTextQueryFunction extends SelfRenderingSqmFunction<String> implements Serializable {


    public AbstractFunctionWithConfigurationAndTextQueryFunction(NodeBuilder nodeBuilder, String configuration, String query, String functionName) {
        this(mapPathParameters(nodeBuilder, configuration, query), nodeBuilder, functionName);
    }

    public AbstractFunctionWithConfigurationAndTextQueryFunction(NodeBuilder nodeBuilder, SqmExpression<?> configuration, String query, String functionName) {
        this(mapPathParameters(nodeBuilder, configuration, query), nodeBuilder, functionName);
    }

    public AbstractFunctionWithConfigurationAndTextQueryFunction(List<? extends SqmTypedNode<?>> parameters, NodeBuilder nodeBuilder, String functionName) {
        super(nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(functionName),
                (FunctionRenderer) nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(functionName),
                //TODO Check if only two arguments are being passed!
                parameters,
                null,
                null,
                StandardFunctionReturnTypeResolvers.invariant(nodeBuilder.getTypeConfiguration().getBasicTypeRegistry().resolve(StandardBasicTypes.STRING)),
                nodeBuilder,
                functionName);
    }

    private static List<? extends SqmTypedNode<?>> mapPathParameters(NodeBuilder nodeBuilder, String configuration, String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Query argument can not be null or empty string");
        }
        List<SqmTypedNode<?>> result = new ArrayList<>();
        if (configuration != null) {
            if (configuration.trim().isEmpty()) {
                throw new IllegalArgumentException("Configuration argument can not be empty string");
            }
            //TODO Literal
            result.add(nodeBuilder.literal(configuration));
        }
        result.add((SqmTypedNode<?>) nodeBuilder.value(query));
        return result;
    }

    private static List<? extends SqmTypedNode<?>> mapPathParameters(NodeBuilder nodeBuilder, SqmExpression<?> configuration, String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Query argument can not be null or empty string");
        }
        List<SqmTypedNode<?>> result = new ArrayList<>();
        if (configuration != null) {
            result.add(configuration);
        }
        result.add((SqmTypedNode<?>) nodeBuilder.value(query));
        return result;
    }
}
