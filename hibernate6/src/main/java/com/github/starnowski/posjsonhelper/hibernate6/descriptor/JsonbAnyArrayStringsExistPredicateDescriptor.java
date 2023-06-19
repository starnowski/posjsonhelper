package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPath;
import com.github.starnowski.posjsonhelper.hibernate6.operators.JsonArrayFunction;
import com.github.starnowski.posjsonhelper.hibernate6.predicates.JsonbAnyArrayStringsExistPredicate;
import org.hibernate.query.sqm.NodeBuilder;

public class JsonbAnyArrayStringsExistPredicateDescriptor extends AbstractJsonbArrayStringsExistPredicateDescriptor<JsonbAnyArrayStringsExistPredicate> {
    public JsonbAnyArrayStringsExistPredicateDescriptor(Context context, HibernateContext hibernateContext) {
        super(context.getJsonbAnyArrayStringsExistFunctionReference(), hibernateContext);
    }

    @Override
    protected JsonbAnyArrayStringsExistPredicate generateJsonbArrayStringsExistPredicate(HibernateContext context, NodeBuilder nodeBuilder, JsonBExtractPath jsonBExtractPath, JsonArrayFunction arrayFunction) {
        return new JsonbAnyArrayStringsExistPredicate(context, nodeBuilder, jsonBExtractPath, arrayFunction);
    }

    @Override
    public String getSqmFunction() {
        return this.hibernateContext.getJsonbAnyArrayStringsExistOperator();
    }

}