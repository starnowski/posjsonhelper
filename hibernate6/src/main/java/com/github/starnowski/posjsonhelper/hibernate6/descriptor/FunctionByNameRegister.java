package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import org.hibernate.query.sqm.function.SqmFunctionDescriptor;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

public class FunctionByNameRegister extends AbstractConditionalFunctionDescriptorRegister {

    private final String functionName;
    public FunctionByNameRegister(String functionName, boolean shouldTryToRegisterFunction) {
        super(shouldTryToRegisterFunction);
        this.functionName = functionName;
    }

    @Override
    protected SqmFunctionDescriptor register(SqmFunctionRegistry registry) {
        return registry.registerNamed(functionName);
    }

    @Override
    protected String getFunctionName() {
        return functionName;
    }
}
