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

import com.github.starnowski.posjsonhelper.hibernate6.AbstractJsonBExtractPath;
import jakarta.persistence.criteria.Path;
import org.hibernate.query.ReturnableType;
import org.hibernate.query.spi.QueryEngine;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.NamedSqmFunctionDescriptor;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.tree.SqmTypedNode;
import org.hibernate.type.spi.TypeConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Function descriptor for child type of {@link AbstractJsonBExtractPath}
 *
 * @param <T> child type for {@link AbstractJsonBExtractPath}
 */
public abstract class AbstractJsonBExtractPathDescriptor<T extends AbstractJsonBExtractPath> extends NamedSqmFunctionDescriptor {

    public AbstractJsonBExtractPathDescriptor(String functionName) {
        super(functionName, false, null, null);
    }


    abstract protected T generateAbstractJsonBExtractPathImpl(SqmTypedNode referencedPathSource, List<SqmTypedNode<?>> pathArguments, NodeBuilder nodeBuilder);

    protected <T> SelfRenderingSqmFunction<T> generateSqmFunctionExpression(List<? extends SqmTypedNode<?>> arguments, ReturnableType<T> impliedResultType, QueryEngine queryEngine) {
        List<SqmTypedNode<?>> pathArguments = new ArrayList<>();
        for (int i = 1; i < arguments.size(); i++) {
            pathArguments.add(arguments.get(i));
        }
        return generateAbstractJsonBExtractPathImpl( arguments.get(0), pathArguments, queryEngine.getCriteriaBuilder());
    }
}
