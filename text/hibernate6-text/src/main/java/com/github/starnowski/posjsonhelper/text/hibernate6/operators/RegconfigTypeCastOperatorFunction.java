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
package com.github.starnowski.posjsonhelper.text.hibernate6.operators;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.operators.CastOperatorFunction;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.tree.expression.SqmExpression;

/**
 * Component extends type {@link CastOperatorFunction}.
 * It extends its base type so that pass the "regconfig" as type for which the value should be cast.
 */
public class RegconfigTypeCastOperatorFunction extends CastOperatorFunction {

    /**
     * @param nodeBuilder      component of type {@link NodeBuilder}
     * @param value            value for cast operation
     * @param hibernateContext context object of type {@link HibernateContext}
     */
    public RegconfigTypeCastOperatorFunction(NodeBuilder nodeBuilder, String value, HibernateContext hibernateContext) {
        super(nodeBuilder, (SqmExpression<?>) nodeBuilder.value(value), "regconfig", hibernateContext);
    }
}
