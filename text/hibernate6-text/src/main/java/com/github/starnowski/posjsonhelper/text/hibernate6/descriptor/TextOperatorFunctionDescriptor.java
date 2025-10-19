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
package com.github.starnowski.posjsonhelper.text.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.text.hibernate6.functions.TSVectorFunction;
import com.github.starnowski.posjsonhelper.text.hibernate6.operators.TextOperatorFunction;
import org.hibernate.query.ReturnableType;
import org.hibernate.query.spi.QueryEngine;
import org.hibernate.query.sqm.function.AbstractSqmSelfRenderingFunctionDescriptor;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.tree.SqmTypedNode;
import org.hibernate.query.sqm.tree.expression.SqmExpression;
import org.hibernate.sql.ast.SqlAstNodeRenderingMode;
import org.hibernate.sql.ast.SqlAstTranslator;
import org.hibernate.sql.ast.spi.SqlAppender;
import org.hibernate.sql.ast.tree.SqlAstNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Function descriptor for <a href="https://www.postgresql.org/docs/current/textsearch-intro.html">text postgres operator</a>.
 * Generally it renders passed arguments that one is SqmExpression<String> object type and second is expression that represents text query.
 * For example:
 * SQL
 * <pre>{@code
 *  :to_tsvector @@ :text_query
 * }</pre>
 *
 */
public class TextOperatorFunctionDescriptor extends AbstractSqmSelfRenderingFunctionDescriptor {

    protected final HibernateContext hibernateContext;

    public TextOperatorFunctionDescriptor(HibernateContext hibernateContext) {
        super(hibernateContext.getTextFunctionOperator(), null, null, null);
        this.hibernateContext = hibernateContext;
    }

    @Override
    public void render(SqlAppender sqlAppender, List<? extends SqlAstNode> sqlAstArguments, ReturnableType<?> returnableType, SqlAstTranslator<?> translator) {
        //TODO Check if sqlAstArguments has two arguments only!

        translator.render(sqlAstArguments.get(0), SqlAstNodeRenderingMode.DEFAULT);
        sqlAppender.appendSql(" @@ ");
        translator.render(sqlAstArguments.get(1), SqlAstNodeRenderingMode.DEFAULT);
    }

    @Override
    protected <T> SelfRenderingSqmFunction<T> generateSqmFunctionExpression(List<? extends SqmTypedNode<?>> arguments, ReturnableType<T> impliedResultType, QueryEngine queryEngine) {
        List<SqmExpression<String>> args = new ArrayList<>();
        //TODO Check if arguments has two arguments only!
        for (int i = 0; i < arguments.size(); i++) {
            args.add((SqmExpression<String>) arguments.get(i));
        }
        return (SelfRenderingSqmFunction<T>) new TextOperatorFunction(queryEngine.getCriteriaBuilder(),  args.get(0), args.get(1), hibernateContext);
    }
}