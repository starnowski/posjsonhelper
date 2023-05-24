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
package com.github.starnowski.posjsonhelper.hibernate6.predicates;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPath;
import org.hibernate.query.sqm.NodeBuilder;

/**
 * Type that extends {@link AbstractJsonbArrayStringsExistPredicate}.
 * Implemented of HQL function defined by method {@link HibernateContext#getJsonbAnyArrayStringsExistOperator()} }
 */
public class JsonbAnyArrayStringsExistPredicate extends AbstractJsonbArrayStringsExistPredicate<JsonbAnyArrayStringsExistPredicate> {
    public JsonbAnyArrayStringsExistPredicate(HibernateContext context, NodeBuilder nodeBuilder, JsonBExtractPath jsonBExtractPath, String[] values) {
        super(context, nodeBuilder, jsonBExtractPath, values, context.getJsonbAnyArrayStringsExistOperator());
    }

    @Override
    protected JsonbAnyArrayStringsExistPredicate generateCopy(HibernateContext context, NodeBuilder nodeBuilder, JsonBExtractPath jsonBExtractPath, String[] values) {
        return new JsonbAnyArrayStringsExistPredicate(context, nodeBuilder, jsonBExtractPath, values);
    }
}