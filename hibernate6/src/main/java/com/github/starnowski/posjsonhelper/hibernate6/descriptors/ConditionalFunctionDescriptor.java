package com.github.starnowski.posjsonhelper.hibernate6.descriptors;

import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.SqmFunctionDescriptor;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

public abstract class ConditionalFunctionDescriptor {

    final boolean shouldTryToRegisterFunction;

    protected ConditionalFunctionDescriptor(boolean shouldTryToRegisterFunction) {
        this.shouldTryToRegisterFunction = shouldTryToRegisterFunction;
    }

    public NodeBuilder registerFunction(NodeBuilder nodeBuilder) {
        if (isShouldTryToRegisterFunction()) {
            SqmFunctionRegistry sqmFunctionRegistry = nodeBuilder.getQueryEngine().getSqmFunctionRegistry();
            SqmFunctionDescriptor functionDescriptor = sqmFunctionRegistry.findFunctionDescriptor(getFunctionName());
            if (functionDescriptor == null) {
                register(sqmFunctionRegistry);
            }
        }
        return nodeBuilder;
    }

    protected abstract void register(SqmFunctionRegistry registry);

    protected abstract String getFunctionName();

    public boolean isShouldTryToRegisterFunction() {
        return shouldTryToRegisterFunction;
    }
}
