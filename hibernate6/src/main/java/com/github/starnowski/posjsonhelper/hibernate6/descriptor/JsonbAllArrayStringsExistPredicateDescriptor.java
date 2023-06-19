package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPath;
import com.github.starnowski.posjsonhelper.hibernate6.operators.JsonArrayFunction;
import com.github.starnowski.posjsonhelper.hibernate6.predicates.AbstractJsonbArrayStringsExistPredicate;
import com.github.starnowski.posjsonhelper.hibernate6.predicates.JsonbAllArrayStringsExistPredicate;
import org.hibernate.query.sqm.NodeBuilder;

public class JsonbAllArrayStringsExistPredicateDescriptor extends AbstractJsonbArrayStringsExistPredicateDescriptor<JsonbAllArrayStringsExistPredicate> {
    public JsonbAllArrayStringsExistPredicateDescriptor(Context context, HibernateContext hibernateContext) {
        super(context.getJsonbAllArrayStringsExistFunctionReference(), hibernateContext);
    }

    @Override
    protected JsonbAllArrayStringsExistPredicate generateJsonbArrayStringsExistPredicate(HibernateContext context, NodeBuilder nodeBuilder, JsonBExtractPath jsonBExtractPath, JsonArrayFunction arrayFunction) {
        return new JsonbAllArrayStringsExistPredicate(context, nodeBuilder, jsonBExtractPath, arrayFunction);
    }

    @Override
    public String getSqmFunction() {
        return this.hibernateContext.getJsonbAllArrayStringsExistOperator();
    }

}
