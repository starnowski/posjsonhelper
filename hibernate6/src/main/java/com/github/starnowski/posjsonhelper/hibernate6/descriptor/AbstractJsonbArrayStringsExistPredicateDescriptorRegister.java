package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.core.HibernateContext;
import org.hibernate.query.sqm.function.SqmFunctionDescriptor;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

public class AbstractJsonbArrayStringsExistPredicateDescriptorRegister extends AbstractConditionalFunctionDescriptorRegister {

    private AbstractJsonbArrayStringsExistPredicateDescriptor abstractJsonbArrayStringsExistPredicateDescriptor;

    public AbstractJsonbArrayStringsExistPredicateDescriptorRegister(boolean shouldOverrideFunctionIfAlreadyRegistered, AbstractJsonbArrayStringsExistPredicateDescriptor abstractJsonbArrayStringsExistPredicateDescriptor) {
        super(shouldOverrideFunctionIfAlreadyRegistered);
        this.abstractJsonbArrayStringsExistPredicateDescriptor = abstractJsonbArrayStringsExistPredicateDescriptor;
    }

    @Override
    protected SqmFunctionDescriptor register(SqmFunctionRegistry registry) {
        return registry.register(getHqlFunctionName(), abstractJsonbArrayStringsExistPredicateDescriptor);
    }

    @Override
    protected String getHqlFunctionName() {
        return abstractJsonbArrayStringsExistPredicateDescriptor.getSqmFunction();
    }
}
