package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.core.HibernateContext;
import org.hibernate.query.sqm.function.SqmFunctionDescriptor;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

public class RemoveJsonValuesFromJsonArrayFunctionDescriptorRegister extends AbstractConditionalFunctionDescriptorRegister {
    private final HibernateContext hibernateContext;
    private final Context context;

    /**
     *
     * @param hibernateContext posjsonhelper hibernate core context
     * @param context posjsonhelper core context
     * @param shouldOverrideFunctionIfAlreadyRegistered value of property {@link #shouldOverrideFunctionIfAlreadyRegistered}
     */
    protected RemoveJsonValuesFromJsonArrayFunctionDescriptorRegister(HibernateContext hibernateContext, Context context, boolean shouldOverrideFunctionIfAlreadyRegistered) {
        super(shouldOverrideFunctionIfAlreadyRegistered);
        this.hibernateContext = hibernateContext;
        this.context = context;
    }

    @Override
    protected SqmFunctionDescriptor register(SqmFunctionRegistry registry) {
        return registry.register(getHqlFunctionName(),
                new RemoveJsonValuesFromJsonArrayFunctionDescriptor(context, hibernateContext));
    }

    @Override
    protected String getHqlFunctionName() {
        return hibernateContext.getRemoveJsonValuesFromJsonArrayFunction();
    }
}
