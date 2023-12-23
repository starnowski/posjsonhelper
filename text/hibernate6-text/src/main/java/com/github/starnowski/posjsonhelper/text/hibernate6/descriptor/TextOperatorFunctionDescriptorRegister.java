package com.github.starnowski.posjsonhelper.text.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.descriptor.AbstractConditionalFunctionDescriptorRegister;
import org.hibernate.query.sqm.function.SqmFunctionDescriptor;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

/**
 * Type extends {@link  AbstractConditionalFunctionDescriptorRegister} type.
 * Responsible for register of hql function that is going to be rendered to the postgres ARRAY.
 * It uses component of type {@link TextOperatorFunctionDescriptor} for rendering.
 * As key the component use {@link HibernateContext#getTextFunctionOperator()} ()}
 */
public class TextOperatorFunctionDescriptorRegister extends AbstractConditionalFunctionDescriptorRegister {

    private final HibernateContext hibernateContext;

    public TextOperatorFunctionDescriptorRegister(HibernateContext hibernateContext, boolean shouldTryToRegisterFunction) {
        super(shouldTryToRegisterFunction);
        this.hibernateContext = hibernateContext;
    }

    @Override
    protected SqmFunctionDescriptor register(SqmFunctionRegistry registry) {
        return registry.register(getHqlFunctionName(),
                new TextOperatorFunctionDescriptor(hibernateContext));
    }

    @Override
    protected String getHqlFunctionName() {
        return hibernateContext.getTextFunctionOperator();
    }
}