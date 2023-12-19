package com.github.starnowski.posjsonhelper.text.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.descriptor.FunctionDescriptorRegister;
import com.github.starnowski.posjsonhelper.hibernate6.descriptor.FunctionDescriptorRegisterFactory;

public class PlainToTSQueryFunctionDescriptorRegisterFactory implements FunctionDescriptorRegisterFactory {
    @Override
    public FunctionDescriptorRegister get(Context context, HibernateContext hibernateContext) {
        return new PlainToTSQueryFunctionDescriptorRegister(true);
    }
}
