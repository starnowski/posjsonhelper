package com.github.starnowski.posjsonhelper.hibernate5.predicates;

import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.hibernate5.JsonBExtractPath;
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;

public class JsonbAnyArrayStringsExistPredicate extends AbstractJsonbArrayStringsExistPredicate {
    public JsonbAnyArrayStringsExistPredicate(Context context, CriteriaBuilderImpl criteriaBuilder, JsonBExtractPath jsonBExtractPath, String[] values) {
        super(context, criteriaBuilder, jsonBExtractPath, values);
    }

    @Override
    protected String getFunctionName() {
        return getContext().getJsonbAnyArrayStringsExistFunctionReference();
    }
}