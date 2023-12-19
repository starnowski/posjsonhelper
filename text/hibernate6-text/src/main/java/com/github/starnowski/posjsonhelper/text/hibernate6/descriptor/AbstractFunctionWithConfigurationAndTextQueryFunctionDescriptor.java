package com.github.starnowski.posjsonhelper.text.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.text.hibernate6.functions.AbstractFunctionWithConfigurationAndTextQueryFunction;
import org.hibernate.query.ReturnableType;
import org.hibernate.query.spi.QueryEngine;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.NamedSqmFunctionDescriptor;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.tree.SqmTypedNode;
import org.hibernate.type.spi.TypeConfiguration;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFunctionWithConfigurationAndTextQueryFunctionDescriptor<T extends AbstractFunctionWithConfigurationAndTextQueryFunction> extends NamedSqmFunctionDescriptor {

    public AbstractFunctionWithConfigurationAndTextQueryFunctionDescriptor(String functionName) {
        super(functionName, false, null, null);
    }

    protected abstract T generateAbstractFunctionWithConfigurationAndTextQueryFunctionImpl(List<? extends SqmTypedNode<?>> arguments, NodeBuilder nodeBuilder);

    protected <T> SelfRenderingSqmFunction<T> generateSqmFunctionExpression(List<? extends SqmTypedNode<?>> arguments, ReturnableType<T> impliedResultType, QueryEngine queryEngine, TypeConfiguration typeConfiguration) {
        return (SelfRenderingSqmFunction<T>) generateAbstractFunctionWithConfigurationAndTextQueryFunctionImpl(arguments, queryEngine.getCriteriaBuilder());
    }
}
