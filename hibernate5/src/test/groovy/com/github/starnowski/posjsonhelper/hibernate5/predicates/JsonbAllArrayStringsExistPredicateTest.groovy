package com.github.starnowski.posjsonhelper.hibernate5.predicates

import com.github.starnowski.posjsonhelper.core.HibernateContext
import com.github.starnowski.posjsonhelper.hibernate5.JsonBExtractPath
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl

class JsonbAllArrayStringsExistPredicateTest extends AbstractJsonbArrayStringsExistPredicateTest<JsonbAllArrayStringsExistPredicate> {

    @Override
    protected HibernateContext.ContextBuilder enrichHibernateContextWithExpectedFunctionName(String functionName, HibernateContext.ContextBuilder builder) {
        builder.withJsonbAllArrayStringsExistOperator(functionName)
    }

    @Override
    protected JsonbAllArrayStringsExistPredicate getTested(HibernateContext context, CriteriaBuilderImpl criteriaBuilder, JsonBExtractPath jsonBExtractPath, List<String> values) {
        new JsonbAllArrayStringsExistPredicate(context, criteriaBuilder, jsonBExtractPath, values.toArray(new String[0]))
    }
}
