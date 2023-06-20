package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.hibernate6.AbstractJsonBExtractPath;
import org.hibernate.query.sqm.function.SqmFunctionDescriptor;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

/**
 * Type extends {@link  AbstractConditionalFunctionDescriptorRegister} type.
 * Responsible for register of hql function for any child type of {@link  AbstractJsonBExtractPath}.
 * It uses component of type {@link AbstractJsonBExtractPathDescriptor} for rendering.
 */
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
