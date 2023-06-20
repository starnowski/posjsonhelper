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

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPath;
import com.github.starnowski.posjsonhelper.hibernate6.operators.JsonArrayFunction;
import com.github.starnowski.posjsonhelper.hibernate6.predicates.AbstractJsonbArrayStringsExistPredicate;
import org.hibernate.query.ReturnableType;
import org.hibernate.query.spi.QueryEngine;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.NamedSqmFunctionDescriptor;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.tree.SqmTypedNode;
import org.hibernate.type.spi.TypeConfiguration;

import java.util.List;

/**
 * Function descriptor for child type of {@link AbstractJsonbArrayStringsExistPredicate}
 *
 * @param <T> child type for {@link AbstractJsonbArrayStringsExistPredicate}
 */
public abstract class AbstractJsonbArrayStringsExistPredicateDescriptor<T extends AbstractJsonbArrayStringsExistPredicate> extends NamedSqmFunctionDescriptor {

    protected final HibernateContext hibernateContext;

    public AbstractJsonbArrayStringsExistPredicateDescriptor(String name, HibernateContext hibernateContext) {
        super(name, false, null, null);
        this.hibernateContext = hibernateContext;
    }

    abstract protected T generateJsonbArrayStringsExistPredicate(HibernateContext context, NodeBuilder nodeBuilder, JsonBExtractPath jsonBExtractPath, JsonArrayFunction arrayFunction);

    /**
     * Name of function for which descriptor should be register
     *
     * @return name of function for which descriptor should be register
     */
    abstract public String getSqmFunction();

    @Override
    protected <T> SelfRenderingSqmFunction<T> generateSqmFunctionExpression(List<? extends SqmTypedNode<?>> arguments, ReturnableType<T> impliedResultType, QueryEngine queryEngine, TypeConfiguration typeConfiguration) {
        return generateJsonbArrayStringsExistPredicate(this.hibernateContext, queryEngine.getCriteriaBuilder(), (JsonBExtractPath) arguments.get(0), (JsonArrayFunction) arguments.get(1));
    }
}
