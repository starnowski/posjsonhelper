package com.github.starnowski.posjsonhelper.text.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.text.hibernate6.functions.TSVectorFunction;
import jakarta.persistence.criteria.Path;
import org.hibernate.query.ReturnableType;
import org.hibernate.query.spi.QueryEngine;
import org.hibernate.query.sqm.function.NamedSqmFunctionDescriptor;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.tree.SqmTypedNode;
import org.hibernate.type.spi.TypeConfiguration;

import java.util.List;

import static com.github.starnowski.posjsonhelper.core.Constants.TO_TSVECTOR_FUNCTION_NAME;

public class TSVectorFunctionDescriptor extends NamedSqmFunctionDescriptor {
    public TSVectorFunctionDescriptor() {
        super(TO_TSVECTOR_FUNCTION_NAME, false, null, null);
    }

    @Override
    protected <T> SelfRenderingSqmFunction<T> generateSqmFunctionExpression(List<? extends SqmTypedNode<?>> arguments, ReturnableType<T> impliedResultType, QueryEngine queryEngine, TypeConfiguration typeConfiguration) {
        //TODO Check arguments size
        return (SelfRenderingSqmFunction<T>) new TSVectorFunction((Path) arguments.get(0), queryEngine.getCriteriaBuilder());
    }
}
