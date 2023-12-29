package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.operators.CastOperatorFunction;
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
import org.hibernate.sql.ast.tree.expression.Literal;
import org.hibernate.type.spi.TypeConfiguration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CastOperatorFunctionDescriptor extends AbstractSqmSelfRenderingFunctionDescriptor {
    protected final HibernateContext hibernateContext;

    public CastOperatorFunctionDescriptor(HibernateContext hibernateContext) {
        super(hibernateContext.getCastFunctionOperator(), null, null, null);
        this.hibernateContext = hibernateContext;
    }

    @Override
    public void render(SqlAppender sqlAppender, List<? extends SqlAstNode> sqlAstArguments, ReturnableType<?> returnableType, SqlAstTranslator<?> translator) {
        //TODO Check if sqlAstArguments has two arguments only!
        boolean firstPass = true;
        for (Iterator var11 = sqlAstArguments.iterator(); var11.hasNext(); firstPass = false) {
            SqlAstNode arg = (SqlAstNode) var11.next();
            if (firstPass) {
                translator.render(arg, SqlAstNodeRenderingMode.DEFAULT);
            } else {
                sqlAppender.appendSql("::");
                if (arg instanceof Literal) {
                    Object value = ((Literal) arg).getLiteralValue();
                    if (value == null) {
                        translator.render(arg, SqlAstNodeRenderingMode.DEFAULT);
                    } else {
                        sqlAppender.appendSql(value.toString());
                    }
                } else {
                    translator.render(arg, SqlAstNodeRenderingMode.DEFAULT);
                }
            }
        }
    }

    @Override
    protected <T> SelfRenderingSqmFunction<T> generateSqmFunctionExpression(List<? extends SqmTypedNode<?>> arguments, ReturnableType<T> impliedResultType, QueryEngine queryEngine) {
        List<SqmExpression<String>> args = new ArrayList<>();
        //TODO Check if arguments has two arguments only!
        for (int i = 0; i < arguments.size(); i++) {
            args.add((SqmExpression<String>) arguments.get(i));
        }
        return (SelfRenderingSqmFunction<T>) new CastOperatorFunction(queryEngine.getCriteriaBuilder(), args.get(0), args.get(1), hibernateContext);
    }
}
