package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPath;
import com.github.starnowski.posjsonhelper.hibernate6.functions.RemoveJsonValuesFromJsonArrayFunction;
import com.github.starnowski.posjsonhelper.hibernate6.operators.ConcatenateJsonbOperator;
import com.github.starnowski.posjsonhelper.hibernate6.operators.JsonArrayFunction;
import org.hibernate.query.ReturnableType;
import org.hibernate.query.spi.QueryEngine;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.tree.SqmTypedNode;

import java.util.List;

public class RemoveJsonValuesFromJsonArrayFunctionDescriptor extends NamedSqmFunctionWithSchemaReferenceDescriptor {

    protected final HibernateContext hibernateContext;

    public RemoveJsonValuesFromJsonArrayFunctionDescriptor(Context context, HibernateContext hibernateContext) {
        super(context.getRemoveValuesFromJsonArrayFunctionReference(), context);
        this.hibernateContext = hibernateContext;
    }

    @Override
    protected <T> SelfRenderingSqmFunction<T> generateSqmFunctionExpression(List<? extends SqmTypedNode<?>> arguments, ReturnableType<T> impliedResultType, QueryEngine queryEngine) {
        return (SelfRenderingSqmFunction<T>) new RemoveJsonValuesFromJsonArrayFunction(queryEngine.getCriteriaBuilder(), arguments.get(0), arguments.get(1), hibernateContext);
    }
}