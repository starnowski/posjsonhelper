package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.core.HibernateContext;

public class JsonArrayFunctionDescriptorRegisterFactory implements FunctionDescriptorRegisterFactory{
    @Override
    public FunctionDescriptorRegister get(Context context, HibernateContext hibernateContext) {
        return new JsonArrayFunctionDescriptorRegister(hibernateContext, true);
    }
}
