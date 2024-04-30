package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.operators.ConcatenateJsonbOperator;
import com.github.starnowski.posjsonhelper.hibernate6.operators.DeleteJsonbBySpecifiedPathOperator;
import jakarta.persistence.criteria.Path;
import org.hibernate.query.ReturnableType;
import org.hibernate.query.spi.QueryEngine;
import org.hibernate.query.sqm.function.AbstractSqmSelfRenderingFunctionDescriptor;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.tree.SqmTypedNode;
import org.hibernate.sql.ast.SqlAstNodeRenderingMode;
import org.hibernate.sql.ast.SqlAstTranslator;
import org.hibernate.sql.ast.spi.SqlAppender;
import org.hibernate.sql.ast.tree.SqlAstNode;

import java.util.Iterator;
import java.util.List;

public class DeleteJsonbBySpecifiedPathOperatorDescriptor extends AbstractSqmSelfRenderingFunctionDescriptor {
    protected final HibernateContext hibernateContext;

    public DeleteJsonbBySpecifiedPathOperatorDescriptor(HibernateContext hibernateContext) {
        super(hibernateContext.getConcatenateJsonbOperator(), null, null, null);
        this.hibernateContext = hibernateContext;
    }

    @Override
    public void render(SqlAppender sqlAppender, List<? extends SqlAstNode> sqlAstArguments, ReturnableType<?> returnableType, SqlAstTranslator<?> translator) {
        boolean firstPass = true;
        sqlAppender.appendSql("(");
        for (Iterator var11 = sqlAstArguments.iterator(); var11.hasNext(); firstPass = false) {
            SqlAstNode arg = (SqlAstNode) var11.next();
            if (!firstPass) {
                sqlAppender.appendSql(" #- ");
            }
            translator.render(arg, SqlAstNodeRenderingMode.DEFAULT);
        }
        sqlAppender.appendSql(")");
    }

    @Override
    protected <T> SelfRenderingSqmFunction<T> generateSqmFunctionExpression(List<? extends SqmTypedNode<?>> arguments, ReturnableType<T> impliedResultType, QueryEngine queryEngine) {
        return (SelfRenderingSqmFunction<T>) new DeleteJsonbBySpecifiedPathOperator(queryEngine.getCriteriaBuilder(), arguments.get(0), arguments.get(1), hibernateContext);
    }
}
