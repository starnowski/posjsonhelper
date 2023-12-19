package com.github.starnowski.posjsonhelper.text.hibernate6.descriptor;

public class PlainToTSQueryFunctionDescriptorRegister extends AbstractFunctionWithConfigurationAndTextQueryFunctionDescriptorRegister<PlainToTSQueryFunctionDescriptor>{
    public PlainToTSQueryFunctionDescriptorRegister(boolean shouldOverrideFunctionIfAlreadyRegistered) {
        super(shouldOverrideFunctionIfAlreadyRegistered, new PlainToTSQueryFunctionDescriptor());
    }
}
