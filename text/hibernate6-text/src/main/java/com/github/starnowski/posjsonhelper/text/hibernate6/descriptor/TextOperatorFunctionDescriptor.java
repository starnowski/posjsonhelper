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
import org.hibernate.type.spi.TypeConfiguration;

import java.util.ArrayList;
import java.util.List;

public class TextOperatorFunctionDescriptor extends AbstractSqmSelfRenderingFunctionDescriptor {

    protected final HibernateContext hibernateContext;

    public TextOperatorFunctionDescriptor(HibernateContext hibernateContext) {
        super(hibernateContext.getTextFunctionOperator(), null, null, null);
        this.hibernateContext = hibernateContext;
    }

    @Override
    public void render(SqlAppender sqlAppender, List<? extends SqlAstNode> sqlAstArguments, SqlAstTranslator<?> translator) {
        //TODO Check if sqlAstArguments has two arguments only!

        translator.render(sqlAstArguments.get(0), SqlAstNodeRenderingMode.DEFAULT);
        sqlAppender.appendSql(" @@ ");
        translator.render(sqlAstArguments.get(1), SqlAstNodeRenderingMode.DEFAULT);
    }

    @Override
    protected <T> SelfRenderingSqmFunction<T> generateSqmFunctionExpression(List<? extends SqmTypedNode<?>> arguments, ReturnableType<T> impliedResultType, QueryEngine queryEngine, TypeConfiguration typeConfiguration) {
        List<SqmExpression<String>> args = new ArrayList<>();
        //TODO Check if arguments has two arguments only!
        for (int i = 0; i < arguments.size(); i++) {
            args.add((SqmExpression<String>) arguments.get(i));
        }
        return (SelfRenderingSqmFunction<T>) new TextOperatorFunction(queryEngine.getCriteriaBuilder(), (TSVectorFunction) args.get(0), args.get(1), hibernateContext);
    }
}