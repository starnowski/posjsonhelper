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
package com.github.starnowski.posjsonhelper.hibernate5;

import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.hibernate.query.criteria.internal.Renderable;
import org.hibernate.query.criteria.internal.compile.RenderingContext;
import org.hibernate.query.criteria.internal.expression.LiteralExpression;
import org.hibernate.query.criteria.internal.expression.function.BasicFunctionExpression;

import javax.persistence.criteria.Expression;
import java.io.Serializable;
import java.util.Iterator;
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
public abstract class AbstractJsonBExtractPath extends BasicFunctionExpression<String> implements Serializable {
    private final List<Expression> pathValues;
    private final Expression<?> operand;

    public AbstractJsonBExtractPath(CriteriaBuilderImpl criteriaBuilder, List<String> path, Expression<?> operand, String functionName) {
        super(criteriaBuilder, String.class, functionName);
        this.operand = operand;
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Path argument can not be null or empty list");
        }
        this.pathValues = path.stream().map(p -> new LiteralExpression(criteriaBuilder, p)).collect(Collectors.toList());
    }


    protected Expression<?> getOperand() {
        return operand;
    }

    public Class getJavaType() {
        return operand.getJavaType();
    }

    public String render(RenderingContext renderingContext) {
        renderingContext.getFunctionStack().push(this);
        String var3;
        try {
            //TODO Checkin path can be empty (or null) from Postgres perspective
            var3 = getFunctionName() + "( " + ((Renderable) this.getOperand()).render(renderingContext) + " , " + renderJsonPath(renderingContext) + " )";
        } finally {
            renderingContext.getFunctionStack().pop();
        }
        return var3;
    }

    private String renderJsonPath(RenderingContext renderingContext) {
        StringBuilder sb = new StringBuilder();
        String sep = "";
        for (Iterator var11 = pathValues.iterator(); var11.hasNext(); sep = ", ") {
            Expression value = (Expression) var11.next();
            sb.append(sep).append(((Renderable) value).render(renderingContext));
        }
        return sb.toString();
    }
}