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

import jakarta.persistence.criteria.Path;
import org.hibernate.query.criteria.JpaExpression;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.FunctionRenderer;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.produce.function.StandardFunctionReturnTypeResolvers;
import org.hibernate.query.sqm.tree.SqmTypedNode;
import org.hibernate.query.sqm.tree.domain.SqmBasicValuedSimplePath;
import org.hibernate.type.StandardBasicTypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * The component renders arguments in the below form. Based on string arguments and main function.
 *
 * Examples:
 *
 * Only one argument
 *
 * {@code {{main_func}}( generatedAlias0.jsonbContent , :param0 )}
 *
 * Two arguments
 *
 * {@code {{main_func}}( generatedAlias0.jsonbContent , :param0, :param1 )}
 */
public abstract class AbstractJsonBExtractPath<T extends AbstractJsonBExtractPath>
        extends SelfRenderingSqmFunction<String> implements Serializable {

    /**
     *
     * @param referencedPathSource path for property that represent JSON or String type. Property has to implement {@link SqmTypedNode}
     * @param nodeBuilder component of type {@link NodeBuilder}
     * @param path list of values that represent JSON path. The order of elements is important!
     * @param functionName name of the main function
     */
    public AbstractJsonBExtractPath(Path referencedPathSource, NodeBuilder nodeBuilder, List<String> path, String functionName) {
        this(referencedPathSource, mapPathParameters(nodeBuilder, path), nodeBuilder, functionName);
    }

    /**
     *
     * @param referencedPathSource path for property that represent JSON or String type. Property has to implement {@link SqmTypedNode}
     * @param path list of values that represent JSON path. The order of elements is important!
     * @param nodeBuilder component of type {@link NodeBuilder}
     * @param functionName name of the main function
     */
    public AbstractJsonBExtractPath(Path referencedPathSource, List<? extends SqmTypedNode<?>> path, NodeBuilder nodeBuilder, String functionName) {
        super(nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(functionName),
                (FunctionRenderer) nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(functionName),
                contactParameters(referencedPathSource, (List<? extends SqmTypedNode<?>>) path),
                null,
                null,
                StandardFunctionReturnTypeResolvers.invariant(nodeBuilder.getTypeConfiguration().getBasicTypeRegistry().resolve(StandardBasicTypes.STRING)),
                nodeBuilder,
                functionName);
    }

    private static List<? extends SqmTypedNode<?>> mapPathParameters(NodeBuilder nodeBuilder, List<String> path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Path argument can not be null or empty list");
        }
        List<SqmTypedNode<?>> result = new ArrayList<>();
        result.addAll(path.stream().map(p -> (SqmTypedNode<String>)nodeBuilder.value(p)).collect(Collectors.toList()));
        return result;
    }

    private static List<? extends SqmTypedNode<?>> contactParameters(Path referencedPathSource, List<? extends SqmTypedNode<?>> path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Path argument can not be null or empty list");
        }
        List<SqmTypedNode<?>> result = new ArrayList<>();
        result.add((SqmTypedNode) referencedPathSource);
        result.addAll(path);
        return result;
    }
}