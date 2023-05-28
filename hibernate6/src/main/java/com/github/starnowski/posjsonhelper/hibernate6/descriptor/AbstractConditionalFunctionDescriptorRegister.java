package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.SqmFunctionDescriptor;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

public abstract class AbstractConditionalFunctionDescriptorRegister {

    final boolean shouldTryToRegisterFunction;

    protected AbstractConditionalFunctionDescriptorRegister(boolean shouldTryToRegisterFunction) {
        this.shouldTryToRegisterFunction = shouldTryToRegisterFunction;
    }

    public SqmFunctionDescriptor registerFunction(NodeBuilder nodeBuilder) {
        return registerFunction(nodeBuilder.getQueryEngine().getSqmFunctionRegistry());
    }

    public SqmFunctionDescriptor registerFunction(SqmFunctionRegistry sqmFunctionRegistry) {
        SqmFunctionDescriptor functionDescriptor = sqmFunctionRegistry.findFunctionDescriptor(getHqlFunctionName());
        return functionDescriptor == null && isShouldTryToRegisterFunction() ? register(sqmFunctionRegistry) : functionDescriptor;
    }

    protected abstract SqmFunctionDescriptor register(SqmFunctionRegistry registry);

    protected abstract String getHqlFunctionName();

    public boolean isShouldTryToRegisterFunction() {
        return shouldTryToRegisterFunction;
    }
}
