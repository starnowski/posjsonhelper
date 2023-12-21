package com.github.starnowski.posjsonhelper.text.hibernate6.descriptor;

public class PhraseToTSQueryFunctionDescriptorRegister extends AbstractFunctionWithConfigurationAndTextQueryFunctionDescriptorRegister<PhraseToTSQueryFunctionDescriptor>{
    public PhraseToTSQueryFunctionDescriptorRegister(boolean shouldOverrideFunctionIfAlreadyRegistered) {
        super(shouldOverrideFunctionIfAlreadyRegistered, new PhraseToTSQueryFunctionDescriptor());
    }
}
