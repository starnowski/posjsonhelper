package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.core.Context;
import org.hibernate.query.ReturnableType;
import org.hibernate.query.sqm.function.NamedSqmFunctionDescriptor;
import org.hibernate.sql.ast.SqlAstTranslator;
import org.hibernate.sql.ast.spi.SqlAppender;
import org.hibernate.sql.ast.tree.SqlAstNode;
import org.hibernate.sql.ast.tree.predicate.Predicate;
import org.hibernate.sql.ast.tree.select.SortSpecification;

import java.util.List;

public class NamedSqmFunctionWithSchemaReferenceDescriptor extends NamedSqmFunctionDescriptor {

    protected final Context context;

    public NamedSqmFunctionWithSchemaReferenceDescriptor(String functionName, Context context) {
        super(functionName, false, null, null);
        this.context = context;
    }

    @Override
    public void render(SqlAppender sqlAppender, List<? extends SqlAstNode> sqlAstArguments, ReturnableType<?> returnType, SqlAstTranslator<?> translator) {
        renderOptionalSchemaReference(sqlAppender);
        super.render(sqlAppender, sqlAstArguments, returnType, translator);
    }

    @Override
    public void render(SqlAppender sqlAppender, List<? extends SqlAstNode> sqlAstArguments, Predicate filter, List<SortSpecification> withinGroup, ReturnableType<?> returnType, SqlAstTranslator<?> translator) {
        renderOptionalSchemaReference(sqlAppender);
        super.render(sqlAppender, sqlAstArguments, filter, withinGroup, returnType, translator);
    }

    @Override
    public void render(SqlAppender sqlAppender, List<? extends SqlAstNode> sqlAstArguments, Predicate filter, ReturnableType<?> returnType, SqlAstTranslator<?> translator) {
        renderOptionalSchemaReference(sqlAppender);
        super.render(sqlAppender, sqlAstArguments, filter, returnType, translator);
    }

    @Override
    public void render(SqlAppender sqlAppender, List<? extends SqlAstNode> sqlAstArguments, Predicate filter, Boolean respectNulls, Boolean fromFirst, ReturnableType<?> returnType, SqlAstTranslator<?> walker) {
        renderOptionalSchemaReference(sqlAppender);
        super.render(sqlAppender, sqlAstArguments, filter, respectNulls, fromFirst, returnType, walker);
    }

    protected void renderOptionalSchemaReference(SqlAppender sqlAppender) {
        if (context.getFunctionsThatShouldBeExecutedWithSchemaReference() != null && context.getFunctionsThatShouldBeExecutedWithSchemaReference().contains(getName())) {
            sqlAppender.appendSql(context.getSchema());
            sqlAppender.appendSql(".");
        }
    }
}
