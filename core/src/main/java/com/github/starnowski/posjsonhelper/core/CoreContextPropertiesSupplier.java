package com.github.starnowski.posjsonhelper.core;

import static com.github.starnowski.posjsonhelper.core.Constants.JSONB_ALL_ARRAY_STRINGS_EXIST_FUNCTION_NAME_PROPERTY;
import static com.github.starnowski.posjsonhelper.core.Constants.JSONB_ANY_ARRAY_STRINGS_EXIST_FUNCTION_NAME_PROPERTY;

public class CoreContextPropertiesSupplier {

    private final SystemPropertyReader systemPropertyReader;

    public CoreContextPropertiesSupplier() {
        this(new SystemPropertyReader());
    }

    CoreContextPropertiesSupplier(SystemPropertyReader systemPropertyReader) {
        this.systemPropertyReader = systemPropertyReader;
    }

    public Context get(){
        Context.ContextBuilder builder = Context.builder();
        String jsonbAllArrayStringsExistFunctionReference = systemPropertyReader.read(JSONB_ALL_ARRAY_STRINGS_EXIST_FUNCTION_NAME_PROPERTY);
        if (jsonbAllArrayStringsExistFunctionReference != null) {
            builder.withJsonbAllArrayStringsExistFunctionReference(jsonbAllArrayStringsExistFunctionReference);
        }
        String jsonbAnyArrayStringsExistFunctionReference = systemPropertyReader.read(JSONB_ANY_ARRAY_STRINGS_EXIST_FUNCTION_NAME_PROPERTY);
        if (jsonbAnyArrayStringsExistFunctionReference != null) {
            builder.withJsonbAnyArrayStringsExistFunctionReference(jsonbAnyArrayStringsExistFunctionReference);
        }
        return builder.build();
    }

    SystemPropertyReader getSystemPropertyReader() {
        return systemPropertyReader;
    }
}
