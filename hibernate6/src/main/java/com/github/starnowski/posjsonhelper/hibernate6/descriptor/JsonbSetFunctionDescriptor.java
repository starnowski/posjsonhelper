package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.functions.JsonbSetFunction;
import org.hibernate.query.ReturnableType;
import org.hibernate.query.spi.QueryEngine;
import org.hibernate.query.sqm.function.NamedSqmFunctionDescriptor;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.tree.SqmTypedNode;

import java.util.List;

public class JsonbSetFunctionDescriptor extends NamedSqmFunctionDescriptor {
    protected final HibernateContext hibernateContext;

    public JsonbSetFunctionDescriptor(HibernateContext hibernateContext) {
        super("jsonb_set", false, null, null);
        this.hibernateContext = hibernateContext;
    }

    @Override
    protected <T> SelfRenderingSqmFunction<T> generateSqmFunctionExpression(List<? extends SqmTypedNode<?>> arguments, ReturnableType<T> impliedResultType, QueryEngine queryEngine) {
        return (SelfRenderingSqmFunction<T>) new JsonbSetFunction(queryEngine.getCriteriaBuilder(), arguments.get(0), arguments.get(1), arguments.get(2), hibernateContext);
    }
}
