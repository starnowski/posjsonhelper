package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import org.hibernate.query.sqm.function.SqmFunctionDescriptor;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

public class DeleteJsonbBySpecifiedPathOperatorDescriptorRegister extends AbstractConditionalFunctionDescriptorRegister {

    private final HibernateContext hibernateContext;

    public DeleteJsonbBySpecifiedPathOperatorDescriptorRegister(HibernateContext hibernateContext, boolean shouldTryToRegisterFunction) {
        super(shouldTryToRegisterFunction);
        this.hibernateContext = hibernateContext;
    }

    @Override
    protected SqmFunctionDescriptor register(SqmFunctionRegistry registry) {
        return registry.register(getHqlFunctionName(),
                new DeleteJsonbBySpecifiedPathOperatorDescriptor(hibernateContext));
    }

    @Override
    protected String getHqlFunctionName() {
        return hibernateContext.getDeleteJsonBySpecificPathOperator();
    }
}