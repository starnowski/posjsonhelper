package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.SqmFunctionDescriptor;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

public abstract class AbstractConditionalFunctionDescriptorRegister {

    final boolean shouldTryToRegisterFunction;

    protected AbstractConditionalFunctionDescriptorRegister(boolean shouldTryToRegisterFunction) {
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
