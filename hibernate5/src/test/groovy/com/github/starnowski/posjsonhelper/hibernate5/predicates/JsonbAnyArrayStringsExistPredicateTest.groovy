package com.github.starnowski.posjsonhelper.hibernate5.predicates

import com.github.starnowski.posjsonhelper.core.HibernateContext
import com.github.starnowski.posjsonhelper.hibernate5.JsonBExtractPath
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl

class JsonbAnyArrayStringsExistPredicateTest extends AbstractJsonbArrayStringsExistPredicateTest<JsonbAnyArrayStringsExistPredicate> {

    @Override
    protected HibernateContext.ContextBuilder enrichHibernateContextWithExpectedFunctionName(String functionName, HibernateContext.ContextBuilder builder) {
        builder.withJsonbAnyArrayStringsExistOperator(functionName)
    }

    @Override
    protected JsonbAnyArrayStringsExistPredicate getTested(HibernateContext context, CriteriaBuilderImpl criteriaBuilder, JsonBExtractPath jsonBExtractPath, List<String> values) {
        new JsonbAnyArrayStringsExistPredicate(context, criteriaBuilder, jsonBExtractPath, values.toArray(new String[0]))
    }
}
