package com.github.starnowski.posjsonhelper.text.hibernate6.operators;

import org.hibernate.query.ReturnableType;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.FunctionRenderingSupport;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.function.SqmFunctionDescriptor;
import org.hibernate.query.sqm.produce.function.ArgumentsValidator;
import org.hibernate.query.sqm.produce.function.FunctionReturnTypeResolver;
import org.hibernate.query.sqm.tree.SqmTypedNode;

import java.io.Serializable;
import java.util.List;

public class TextOperatorFunction extends SelfRenderingSqmFunction<String> implements Serializable {
    public TextOperatorFunction(SqmFunctionDescriptor descriptor, FunctionRenderingSupport renderingSupport, List<? extends SqmTypedNode<?>> arguments, ReturnableType<String> impliedResultType, ArgumentsValidator argumentsValidator, FunctionReturnTypeResolver returnTypeResolver, NodeBuilder nodeBuilder, String name) {
        super(descriptor, renderingSupport, arguments, impliedResultType, argumentsValidator, returnTypeResolver, nodeBuilder, name);
    }
}
