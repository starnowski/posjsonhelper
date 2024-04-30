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
import org.hibernate.query.sqm.tree.expression.SqmExpression;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Implemented of HQL function defined by property {@link HibernateContext#deleteJsonBySpecificPathOperator}.
 * It is wrapper for operator that deletes the field or array element at the specified path, where path elements can be either field keys or array indexes.
 */
public class DeleteJsonbBySpecifiedPathOperator extends SelfRenderingSqmFunction<String> implements Serializable {

    /**
     * @param nodeBuilder          component of type {@link NodeBuilder}
     * @param referencedPathSource path for property that represent JSON or String type. Property has to implement {@link SqmTypedNode}
     * @param jsonPath             value for a text array that represents the JSON path for an element that is supposed to be deleted. For example "{parent,child,property}"
     * @param hibernateContext     object of type {@link HibernateContext}
     */
    public DeleteJsonbBySpecifiedPathOperator(NodeBuilder nodeBuilder, Path referencedPathSource, String jsonPath, HibernateContext hibernateContext) {
        this(nodeBuilder, (SqmTypedNode) referencedPathSource, generateCastedJsonPathToTextArray(nodeBuilder, jsonPath, hibernateContext), hibernateContext);
    }

    /**
     * @param nodeBuilder          component of type {@link NodeBuilder}
     * @param referencedPathSource path for property that represent JSON or String type
     * @param jsonPath             value for a text array that represents the JSON path for an element that is supposed to be deleted. For example "{parent,child,property}"
     * @param hibernateContext     object of type {@link HibernateContext}
     */
    public DeleteJsonbBySpecifiedPathOperator(NodeBuilder nodeBuilder, SqmTypedNode referencedPathSource, String jsonPath, HibernateContext hibernateContext) {
        this(nodeBuilder, referencedPathSource, generateCastedJsonPathToTextArray(nodeBuilder, jsonPath, hibernateContext), hibernateContext);
    }

    /**
     * @param nodeBuilder          component of type {@link NodeBuilder}
     * @param referencedPathSource path for property that represent JSON or String type
     * @param jsonPath             value for a text array that represents the JSON path for an element that is supposed to be deleted. For example "{parent,child,property}"
     * @param hibernateContext     object of type {@link HibernateContext}
     */
    public DeleteJsonbBySpecifiedPathOperator(NodeBuilder nodeBuilder, SqmTypedNode referencedPathSource, SqmTypedNode jsonPath, HibernateContext hibernateContext) {
        super(
                nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(hibernateContext.getDeleteJsonBySpecificPathOperator()),
                (FunctionRenderer) nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(hibernateContext.getDeleteJsonBySpecificPathOperator()),
                mapParameters(referencedPathSource, jsonPath),
                null,
                null,
                StandardFunctionReturnTypeResolvers.useFirstNonNull(),
                nodeBuilder,
                hibernateContext.getDeleteJsonBySpecificPathOperator());
    }

    private static List<? extends SqmTypedNode<?>> mapParameters(SqmTypedNode referencedPathSource, SqmTypedNode jsonPath) {
        List<SqmTypedNode<?>> result = new ArrayList<>();
        result.add((SqmTypedNode<?>) referencedPathSource);
        result.add((SqmTypedNode<?>) jsonPath);
        return result;
    }

    private static CastOperatorFunction generateCastedJsonPathToTextArray(NodeBuilder nodeBuilder, String jsonPath, HibernateContext hibernateContext) {
        return new CastOperatorFunction(nodeBuilder, (SqmExpression<?>) nodeBuilder.value(jsonPath), "text[]", hibernateContext);
    }
}
