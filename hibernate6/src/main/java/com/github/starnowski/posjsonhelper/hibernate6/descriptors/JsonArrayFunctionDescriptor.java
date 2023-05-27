package com.github.starnowski.posjsonhelper.hibernate6.descriptors;

import org.hibernate.query.sqm.function.AbstractSqmSelfRenderingFunctionDescriptor;
import org.hibernate.sql.ast.SqlAstNodeRenderingMode;
import org.hibernate.sql.ast.SqlAstTranslator;
import org.hibernate.sql.ast.spi.SqlAppender;
import org.hibernate.sql.ast.tree.SqlAstNode;

import java.util.Iterator;
import java.util.List;

public class JsonArrayFunctionDescriptor extends AbstractSqmSelfRenderingFunctionDescriptor {
    public JsonArrayFunctionDescriptor() {
        super("array", null, null, null);
    }

    @Override
    public void render(SqlAppender sqlAppender, List<? extends SqlAstNode> sqlAstArguments, SqlAstTranslator<?> translator) {
        sqlAppender.appendSql(this.getName());
        sqlAppender.appendSql("[");

        boolean firstPass = true;

        for (Iterator var11 = sqlAstArguments.iterator(); var11.hasNext(); firstPass = false) {
            SqlAstNode arg = (SqlAstNode) var11.next();
            if (!firstPass) {
                sqlAppender.appendSql(",");
            }
            translator.render(arg, SqlAstNodeRenderingMode.DEFAULT);
        }
        sqlAppender.appendSql("]");
    }
}
