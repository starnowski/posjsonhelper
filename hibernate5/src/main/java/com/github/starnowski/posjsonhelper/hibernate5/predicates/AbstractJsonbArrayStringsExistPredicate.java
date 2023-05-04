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
package com.github.starnowski.posjsonhelper.hibernate5.predicates;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate5.JsonBExtractPath;
import org.hibernate.query.criteria.internal.*;
import org.hibernate.query.criteria.internal.compile.RenderingContext;
import org.hibernate.query.criteria.internal.expression.LiteralExpression;
import org.hibernate.query.criteria.internal.expression.UnaryOperatorExpression;
import org.hibernate.query.criteria.internal.predicate.AbstractSimplePredicate;

import javax.persistence.criteria.Expression;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Component that renders arguments into below form.TODO
 *
 * {{main_func}}( jsonb_extract_path( generatedAlias0.jsonbContent , :param0 ) , json_function_json_array(:param1, :param2)) = TRUE
 */
public abstract class AbstractJsonbArrayStringsExistPredicate extends AbstractSimplePredicate implements UnaryOperatorExpression<Boolean>, Serializable {

    private final HibernateContext context;
    private final JsonBExtractPath jsonBExtractPath;
    private final List<Expression> values;

    public AbstractJsonbArrayStringsExistPredicate(HibernateContext context, CriteriaBuilderImpl criteriaBuilder, JsonBExtractPath jsonBExtractPath, String[] values) {
        super(criteriaBuilder);
        this.jsonBExtractPath = jsonBExtractPath;
        this.context = context;
        Class javaType = jsonBExtractPath.getJavaType();
        ValueHandlerFactory.ValueHandler valueHandler = javaType != null && ValueHandlerFactory.isNumeric(javaType) ? ValueHandlerFactory.determineAppropriateHandler(javaType) : new ValueHandlerFactory.NoOpValueHandler();
        Iterator var6 = Arrays.asList(values).iterator();
        this.values = new ArrayList(values.length);
        while (var6.hasNext()) {
            Object value = var6.next();
            this.values.add(new LiteralExpression(criteriaBuilder, valueHandler.convert(value)));
        }
    }

    @Override
    public void registerParameters(ParameterRegistry registry) {
        // do nothing
    }

    private String renderValues(RenderingContext renderingContext) {
        StringBuilder sb = new StringBuilder();
        sb.append(context.getJsonFunctionJsonArrayOperator());
        sb.append("(");

        String sep = "";
        for (Iterator var11 = this.values.iterator(); var11.hasNext(); sep = ", ") {
            Expression value = (Expression) var11.next();
            sb.append(sep).append(((Renderable) value).render(renderingContext));
        }

        sb.append(")");
        return sb.toString();
    }

    @Override
    public Expression<?> getOperand() {
        return this.jsonBExtractPath;
    }

    @Override
    public String render(boolean isNegated, RenderingContext renderingContext) {
        return getFunctionName() + "( " + this.jsonBExtractPath.render(renderingContext) + " , " + renderValues(renderingContext) + ") = " + (isNegated ? "FALSE" : "TRUE");
    }

    /**
     * Main HQL function for predicate.
     * @return name of the HQL function
     */
    abstract protected String getFunctionName();

    protected HibernateContext getContext() {
        return context;
    }
}
