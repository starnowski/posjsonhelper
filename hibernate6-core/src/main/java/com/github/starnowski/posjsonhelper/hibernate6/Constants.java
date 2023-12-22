package com.github.starnowski.posjsonhelper.hibernate6;

public class Constants {
    /**
     * System property that stores list of {@link com.github.starnowski.posjsonhelper.hibernate6.descriptor.FunctionDescriptorRegisterFactoriesSupplier} types that should be loaded.
     * Instead of loading types that can be found on the classpath for package "com.github.starnowski.posjsonhelper".
     * Types on the list are separated by comma character ".".
     */
    public static final String FUNCTIONDESCRIPTORREGISTERFACTORY_TYPES_PROPERTY = "com.github.starnowski.posjsonhelper.hibernate6.functiondescriptorregisterfactory.types";
    /**
     * System property that stores list of {@link com.github.starnowski.posjsonhelper.hibernate6.descriptor.FunctionDescriptorRegisterFactoriesSupplier} types that should be excluded from loading.
     * If {@link #FUNCTIONDESCRIPTORREGISTERFACTORY_TYPES_PROPERTY} property is also specified then {@link #FUNCTIONDESCRIPTORREGISTERFACTORY_TYPES_EXCLUDED_PROPERTY} has higher priority.
     * Types on the list are separated by comma character ".".
     */
    public static final String FUNCTIONDESCRIPTORREGISTERFACTORY_TYPES_EXCLUDED_PROPERTY = "com.github.starnowski.posjsonhelper.hibernate6.functiondescriptorregisterfactory.types.excluded";
}
