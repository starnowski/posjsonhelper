package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import org.hibernate.query.sqm.function.SqmFunctionDescriptor;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

public class FunctionByNameRegister extends AbstractConditionalFunctionDescriptorRegister {

    private final String hqlFunctionName;
    public FunctionByNameRegister(String hqlFunctionName, boolean shouldTryToRegisterFunction) {
        super(shouldTryToRegisterFunction);
        this.hqlFunctionName = hqlFunctionName;
    }

    @Override
    protected SqmFunctionDescriptor register(SqmFunctionRegistry registry) {
        return registry.registerNamed(hqlFunctionName);
    }

    @Override
    protected String getHqlFunctionName() {
        return hqlFunctionName;
    }
}
