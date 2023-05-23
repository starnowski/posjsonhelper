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
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.tree.expression.SqmExpression;
import org.hibernate.query.sqm.tree.predicate.AbstractNegatableSqmPredicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * The component renders arguments in the below form. Based on string arguments, JsonPath, and main function.
 * Let's assume that for the below example, we have two arguments, JsonPath
 *
 * <pre>{@code
 * {{main_func}}( jsonb_extract_path( generatedAlias0.jsonbContent , :param0 ) , json_function_json_array(:param1, :param2)) = TRUE
 * }</pre>
 *
 * where:
 * {{main_func}} - name of main function returned by method {@link #getFunctionName()}
 * jsonb_extract_path( generatedAlias0.jsonbContent , :param0 ) - json path part, with this example path has only one element normally this could part could contain more elements "param"
 * {{json_function_json_array}} - hibernate operator that wraps the "array" operator in postgres. Values comes from  {@link HibernateContext#getJsonFunctionJsonArrayOperator()}
 * (:param1, :param2) - rendered string arguments
 * TRUE - expected predicate value, by default it is "TRUE"
 *
 */
public abstract class AbstractJsonbArrayStringsExistPredicate extends AbstractNegatableSqmPredicate {

    private final HibernateContext context;
    private final JsonBExtractPath jsonBExtractPath;
    private final List<SqmExpression> values;

    public AbstractJsonbArrayStringsExistPredicate(HibernateContext context, NodeBuilder nodeBuilder, JsonBExtractPath jsonBExtractPath, String[] values, boolean negated) {
        super(negated, nodeBuilder);
        this.jsonBExtractPath = jsonBExtractPath;
        this.context = context;
        Iterator var6 = Arrays.asList(values).iterator();
        this.values = new ArrayList(values.length);
        while (var6.hasNext()) {
            Object value = var6.next();
            this.values.add(nodeBuilder.literal(value));
        }
    }

//    private String renderValues(RenderingContext renderingContext) {
//        StringBuilder sb = new StringBuilder();
//        sb.append(context.getJsonFunctionJsonArrayOperator());
//        sb.append("(");
//
//        String sep = "";
//        for (Iterator var11 = this.values.iterator(); var11.hasNext(); sep = ", ") {
//            Expression value = (Expression) var11.next();
//            sb.append(sep).append(((Renderable) value).render(renderingContext));
//        }
//
//        sb.append(")");
//        return sb.toString();
//    }

    /**
     * Main HQL function for predicate.
     * @return name of the HQL function
     */
    abstract protected String getFunctionName();

    protected HibernateContext getContext() {
        return context;
    }
}
