package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.core.HibernateContext;

public interface FunctionDescriptorRegisterFactory {

    FunctionDescriptorRegister get(Context context, HibernateContext hibernateContext);
}
