package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.SqmFunctionDescriptor;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

import java.util.Optional;

public abstract class AbstractConditionalFunctionDescriptorRegister {

    final boolean shouldTryToRegisterFunction;

    protected AbstractConditionalFunctionDescriptorRegister(boolean shouldTryToRegisterFunction) {
        this.shouldTryToRegisterFunction = shouldTryToRegisterFunction;
    }

    public SqmFunctionDescriptor registerFunction(NodeBuilder nodeBuilder) {
        SqmFunctionRegistry sqmFunctionRegistry = nodeBuilder.getQueryEngine().getSqmFunctionRegistry();
        SqmFunctionDescriptor functionDescriptor = sqmFunctionRegistry.findFunctionDescriptor(getFunctionName());
        return functionDescriptor == null && isShouldTryToRegisterFunction() ? register(sqmFunctionRegistry) : functionDescriptor;
    }

    protected abstract SqmFunctionDescriptor register(SqmFunctionRegistry registry);

    protected abstract String getFunctionName();

    public boolean isShouldTryToRegisterFunction() {
        return shouldTryToRegisterFunction;
    }
}
