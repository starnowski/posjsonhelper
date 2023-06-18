package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPath;
import com.github.starnowski.posjsonhelper.hibernate6.predicates.AbstractJsonbArrayStringsExistPredicate;
import com.github.starnowski.posjsonhelper.hibernate6.predicates.JsonbAllArrayStringsExistPredicate;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.sql.ast.SqlAstTranslator;
import org.hibernate.sql.ast.spi.SqlAppender;
import org.hibernate.sql.ast.tree.SqlAstNode;

import java.util.List;

public class JsonbAllArrayStringsExistPredicateDescriptor extends AbstractJsonbArrayStringsExistPredicateDescriptor {
    public JsonbAllArrayStringsExistPredicateDescriptor(Context context, HibernateContext hibernateContext) {
        super(context.getJsonbAllArrayStringsExistFunctionReference(), hibernateContext);
    }

    @Override
    protected AbstractJsonbArrayStringsExistPredicate generateJsonbArrayStringsExistPredicate(HibernateContext context, NodeBuilder nodeBuilder, JsonBExtractPath jsonBExtractPath, String[] values) {
        return new JsonbAllArrayStringsExistPredicate(context, nodeBuilder, jsonBExtractPath, values);
    }

    @Override
    public String getSqmFunction() {
        return this.hibernateContext.getJsonbAllArrayStringsExistOperator();
    }

}
