package com.github.starnowski.posjsonhelper.text.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.hibernate6.descriptor.AbstractConditionalFunctionDescriptorRegister;
import org.hibernate.query.sqm.function.SqmFunctionDescriptor;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

public abstract class AbstractFunctionWithConfigurationAndTextQueryFunctionDescriptorRegister<T extends AbstractFunctionWithConfigurationAndTextQueryFunctionDescriptor> extends AbstractConditionalFunctionDescriptorRegister {

    private final T descriptor;

    public AbstractFunctionWithConfigurationAndTextQueryFunctionDescriptorRegister(boolean shouldOverrideFunctionIfAlreadyRegistered, T descriptor) {
        super(shouldOverrideFunctionIfAlreadyRegistered);
        this.descriptor = descriptor;
    }

    @Override
    protected SqmFunctionDescriptor register(SqmFunctionRegistry registry) {
        return registry.register(getHqlFunctionName(), descriptor);
    }

    @Override
    protected String getHqlFunctionName() {
        return descriptor.getName();
    }
}
