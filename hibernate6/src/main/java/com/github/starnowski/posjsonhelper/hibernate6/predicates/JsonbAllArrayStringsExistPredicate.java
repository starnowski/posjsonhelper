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
package com.github.starnowski.posjsonhelper.hibernate6.predicates;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPath;
import com.github.starnowski.posjsonhelper.hibernate6.operators.JsonArrayFunction;
import org.hibernate.query.sqm.NodeBuilder;

/**
 * Type that extends {@link AbstractJsonbArrayStringsExistPredicate}.
 * Implemented of HQL function defined by method {@link HibernateContext#getJsonbAllArrayStringsExistOperator()}
 */
public class JsonbAllArrayStringsExistPredicate extends AbstractJsonbArrayStringsExistPredicate<JsonbAllArrayStringsExistPredicate> {
    /**
     * @param context context object of type {@link HibernateContext}
     * @param nodeBuilder node builder {@link NodeBuilder}
     * @param jsonBExtractPath json path for json property {@link JsonBExtractPath}
     * @param values array of string values passed to as argument for function
     */
    public JsonbAllArrayStringsExistPredicate(HibernateContext context, NodeBuilder nodeBuilder, JsonBExtractPath jsonBExtractPath, Comparable[] values) {
        super(context, nodeBuilder, jsonBExtractPath, values, context.getJsonbAllArrayStringsExistOperator());
    }

    /**
     * @param context object of type {@link HibernateContext}
     * @param nodeBuilder node builder {@link NodeBuilder}
     * @param jsonBExtractPath json path for json property {@link JsonBExtractPath}
     * @param jsonArrayFunction component of type {@link JsonArrayFunction} that represent array of values passed to as argument for function
     */
    public JsonbAllArrayStringsExistPredicate(HibernateContext context, NodeBuilder nodeBuilder, JsonBExtractPath jsonBExtractPath, JsonArrayFunction jsonArrayFunction) {
        super(context, nodeBuilder, jsonBExtractPath, jsonArrayFunction, context.getJsonbAllArrayStringsExistOperator());
    }

    @Override
    protected JsonbAllArrayStringsExistPredicate generateCopy(HibernateContext context, NodeBuilder nodeBuilder, JsonBExtractPath jsonBExtractPath, JsonArrayFunction jsonArrayFunction) {
        return new JsonbAllArrayStringsExistPredicate(context, nodeBuilder, jsonBExtractPath, jsonArrayFunction);
    }
}