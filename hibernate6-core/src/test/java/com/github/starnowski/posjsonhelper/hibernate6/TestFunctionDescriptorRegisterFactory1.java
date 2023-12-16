package com.github.starnowski.posjsonhelper.hibernate6;

import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.descriptor.AbstractConditionalFunctionDescriptorRegister;
import com.github.starnowski.posjsonhelper.hibernate6.descriptor.FunctionDescriptorRegisterFactory;

public class TestFunctionDescriptorRegisterFactory1 implements FunctionDescriptorRegisterFactory {
    @Override
    public AbstractConditionalFunctionDescriptorRegister get(Context context, HibernateContext hibernateContext) {
        return null;
    }
}
