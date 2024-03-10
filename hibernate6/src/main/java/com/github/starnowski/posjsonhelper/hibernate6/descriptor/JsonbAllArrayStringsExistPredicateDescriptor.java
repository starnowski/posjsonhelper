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

import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPath;
import com.github.starnowski.posjsonhelper.hibernate6.operators.JsonArrayFunction;
import com.github.starnowski.posjsonhelper.hibernate6.predicates.JsonbAllArrayStringsExistPredicate;
import org.hibernate.query.sqm.NodeBuilder;

/**
 * Function descriptor for child type of {@link JsonbAllArrayStringsExistPredicate}
 */
public class JsonbAllArrayStringsExistPredicateDescriptor extends AbstractJsonbArrayStringsExistPredicateDescriptor<JsonbAllArrayStringsExistPredicate> {
    public JsonbAllArrayStringsExistPredicateDescriptor(Context context, HibernateContext hibernateContext) {
        super(context.getJsonbAllArrayStringsExistFunctionReference(), context, hibernateContext);
    }

    @Override
    protected JsonbAllArrayStringsExistPredicate generateJsonbArrayStringsExistPredicate(HibernateContext context, NodeBuilder nodeBuilder, JsonBExtractPath jsonBExtractPath, JsonArrayFunction arrayFunction) {
        return new JsonbAllArrayStringsExistPredicate(context, nodeBuilder, jsonBExtractPath, arrayFunction);
    }

    @Override
    public String getSqmFunction() {
        return this.hibernateContext.getJsonbAllArrayStringsExistOperator();
    }

}
