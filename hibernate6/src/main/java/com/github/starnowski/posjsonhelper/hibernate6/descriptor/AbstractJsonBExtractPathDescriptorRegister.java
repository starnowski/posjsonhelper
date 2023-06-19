package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import org.hibernate.query.sqm.function.SqmFunctionDescriptor;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

public class AbstractJsonBExtractPathDescriptorRegister extends AbstractConditionalFunctionDescriptorRegister {

    private final AbstractJsonBExtractPathDescriptor abstractJsonBExtractPathDescriptor;

    /**
     * @param shouldOverrideFunctionIfAlreadyRegistered value of property {@link #shouldOverrideFunctionIfAlreadyRegistered}
     */
    public AbstractJsonBExtractPathDescriptorRegister(AbstractJsonBExtractPathDescriptor abstractJsonBExtractPathDescriptor, boolean shouldOverrideFunctionIfAlreadyRegistered) {
        super(shouldOverrideFunctionIfAlreadyRegistered);
        this.abstractJsonBExtractPathDescriptor = abstractJsonBExtractPathDescriptor;
    }

    @Override
    protected SqmFunctionDescriptor register(SqmFunctionRegistry registry) {
        return registry.register(abstractJsonBExtractPathDescriptor.getName(), abstractJsonBExtractPathDescriptor);
    }

    @Override
    protected String getHqlFunctionName() {
        return abstractJsonBExtractPathDescriptor.getName();
    }
}
