package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import org.hibernate.query.sqm.function.SqmFunctionDescriptor;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

/**
 * Type, responsible for registration of custom functions.
 *
 */
public interface FunctionDescriptorRegister {

    /**
     * Method that register function with {@link SqmFunctionRegistry}.
     * @param sqmFunctionRegistry object of type SqmFunctionRegistry that is going to be used for function registration
     * @return function descriptor
     */
    SqmFunctionDescriptor registerFunction(SqmFunctionRegistry sqmFunctionRegistry);
}
