package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import org.hibernate.query.sqm.function.SqmFunctionDescriptor;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

import static com.github.starnowski.posjsonhelper.core.Constants.JSONB_SET_FUNCTION_NAME;

public class JsonbSetFunctionDescriptorRegister extends AbstractConditionalFunctionDescriptorRegister {

    public JsonbSetFunctionDescriptorRegister(HibernateContext hibernateContext, boolean shouldTryToRegisterFunction) {
        super(shouldTryToRegisterFunction);
    }

    @Override
    protected SqmFunctionDescriptor register(SqmFunctionRegistry registry) {
        return registry.register(getHqlFunctionName(),
                new JsonbSetFunctionDescriptor());
    }

    @Override
    protected String getHqlFunctionName() {
        return JSONB_SET_FUNCTION_NAME;
    }
}