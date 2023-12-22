package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import org.hibernate.query.sqm.function.SqmFunctionDescriptor;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

public class CastOperatorFunctionDescriptorRegister extends AbstractConditionalFunctionDescriptorRegister {

    private final HibernateContext hibernateContext;

    /**
     * @param shouldOverrideFunctionIfAlreadyRegistered value of property {@link #shouldOverrideFunctionIfAlreadyRegistered}
     * @param hibernateContext
     */
    protected CastOperatorFunctionDescriptorRegister(boolean shouldOverrideFunctionIfAlreadyRegistered, HibernateContext hibernateContext) {
        super(shouldOverrideFunctionIfAlreadyRegistered);
        this.hibernateContext = hibernateContext;
    }

    @Override
    protected SqmFunctionDescriptor register(SqmFunctionRegistry registry) {
        return registry.register(getHqlFunctionName(),
                new CastOperatorFunctionDescriptor(hibernateContext));
    }

    @Override
    protected String getHqlFunctionName() {
        return hibernateContext.getCastFunctionOperator();
    }
}
