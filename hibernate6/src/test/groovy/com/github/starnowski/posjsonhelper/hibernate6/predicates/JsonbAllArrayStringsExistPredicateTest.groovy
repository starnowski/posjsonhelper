package com.github.starnowski.posjsonhelper.hibernate6.predicates

import com.github.starnowski.posjsonhelper.core.HibernateContext
import com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPath
import org.hibernate.query.sqm.NodeBuilder
import static com.github.starnowski.posjsonhelper.core.Constants.DEFAULT_JSONB_ALL_ARRAY_STRINGS_EXIST_HIBERNATE_OPERATOR

class JsonbAllArrayStringsExistPredicateTest extends AbstractJsonbArrayStringsExistPredicateTest<JsonbAllArrayStringsExistPredicate> {
    @Override
    protected JsonbAllArrayStringsExistPredicate prepareTestObject(HibernateContext context, NodeBuilder nodeBuilder, JsonBExtractPath jsonBExtractPath, String[] values) {
        new JsonbAllArrayStringsExistPredicate(context, nodeBuilder, jsonBExtractPath, values)
    }

    @Override
    protected String expectedFunctionName() {
        DEFAULT_JSONB_ALL_ARRAY_STRINGS_EXIST_HIBERNATE_OPERATOR
    }
}
