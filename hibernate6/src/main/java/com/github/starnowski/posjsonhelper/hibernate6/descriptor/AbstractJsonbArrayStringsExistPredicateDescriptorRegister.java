package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.predicates.AbstractJsonbArrayStringsExistPredicate;
import org.hibernate.query.sqm.function.SqmFunctionDescriptor;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

/**
 * Type extends {@link  AbstractConditionalFunctionDescriptorRegister} type.
 * Responsible for register of hql function for any child type of {@link  AbstractJsonbArrayStringsExistPredicate}.
 * It uses component of type {@link AbstractJsonbArrayStringsExistPredicateDescriptor} for rendering.
 */
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
