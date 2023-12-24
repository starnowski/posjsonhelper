package com.github.starnowski.posjsonhelper.text.hibernate6.descriptor;

public class WebsearchToTSQueryFunctionDescriptorRegister extends AbstractFunctionWithConfigurationAndTextQueryFunctionDescriptorRegister<WebsearchToTSQueryFunctionDescriptor> {

    public WebsearchToTSQueryFunctionDescriptorRegister(boolean shouldOverrideFunctionIfAlreadyRegistered) {
        super(shouldOverrideFunctionIfAlreadyRegistered, new WebsearchToTSQueryFunctionDescriptor());
    }
}
