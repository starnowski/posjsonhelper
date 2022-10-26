package com.github.starnowski.posjsonhelper.hibernate5.predicates;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate5.JsonBExtractPath;
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;

public class JsonbAnyArrayStringsExistPredicate extends AbstractJsonbArrayStringsExistPredicate {
    public JsonbAnyArrayStringsExistPredicate(HibernateContext context, CriteriaBuilderImpl criteriaBuilder, JsonBExtractPath jsonBExtractPath, String[] values) {
        super(context, criteriaBuilder, jsonBExtractPath, values);
    }

    @Override
    protected String getFunctionName() {
        return getContext().getJsonbAnyArrayStringsExistOperator();
    }
}