package com.github.starnowski.posjsonhelper.text.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.hibernate6.descriptor.AbstractConditionalFunctionDescriptorRegister;
import org.hibernate.query.sqm.function.SqmFunctionDescriptor;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

import static com.github.starnowski.posjsonhelper.core.Constants.TO_TSVECTOR_FUNCTION_NAME;

public class TSVectorFunctionDescriptorRegister extends AbstractConditionalFunctionDescriptorRegister {

    /**
     * @param shouldOverrideFunctionIfAlreadyRegistered value of property {@link #shouldOverrideFunctionIfAlreadyRegistered}
     */
    protected TSVectorFunctionDescriptorRegister(boolean shouldOverrideFunctionIfAlreadyRegistered) {
        super(shouldOverrideFunctionIfAlreadyRegistered);
    }

    @Override
    protected SqmFunctionDescriptor register(SqmFunctionRegistry registry) {
        return registry.register(getHqlFunctionName(),
                new TSVectorFunctionDescriptor());
    }

    @Override
    protected String getHqlFunctionName() {
        return TO_TSVECTOR_FUNCTION_NAME;
    }
}
