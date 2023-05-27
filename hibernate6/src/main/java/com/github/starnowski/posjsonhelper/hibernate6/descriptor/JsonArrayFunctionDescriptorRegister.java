package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import org.hibernate.query.sqm.function.SqmFunctionDescriptor;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

public class JsonArrayFunctionDescriptorRegister extends AbstractConditionalFunctionDescriptorRegister {

    private final HibernateContext hibernateContext;

    public JsonArrayFunctionDescriptorRegister(HibernateContext hibernateContext, boolean shouldTryToRegisterFunction) {
        super(shouldTryToRegisterFunction);
        this.hibernateContext = hibernateContext;
    }

    @Override
    protected SqmFunctionDescriptor register(SqmFunctionRegistry registry) {
        return registry.register(getFunctionName(),
                new JsonArrayFunctionDescriptor());
    }

    @Override
    protected String getFunctionName() {
        return hibernateContext.getJsonFunctionJsonArrayOperator();
    }
}
