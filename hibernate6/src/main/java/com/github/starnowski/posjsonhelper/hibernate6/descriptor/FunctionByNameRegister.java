package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import org.hibernate.query.sqm.function.SqmFunctionDescriptor;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

public class FunctionByNameRegister extends AbstractConditionalFunctionDescriptorRegister {

    private final String hqlFunctionName;
    private final String sqlFunctionName;

    public FunctionByNameRegister(String hqlFunctionName, boolean shouldTryToRegisterFunction, String sqlFunctionName) {
        super(shouldTryToRegisterFunction);
        this.hqlFunctionName = hqlFunctionName;
        this.sqlFunctionName = sqlFunctionName;
    }

    @Override
    protected SqmFunctionDescriptor register(SqmFunctionRegistry registry) {
        return registry.namedDescriptorBuilder(hqlFunctionName, sqlFunctionName).register();
    }

    @Override
    protected String getHqlFunctionName() {
        return hqlFunctionName;
    }
}
