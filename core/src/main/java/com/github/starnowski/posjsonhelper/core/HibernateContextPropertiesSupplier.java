package com.github.starnowski.posjsonhelper.core;

import static com.github.starnowski.posjsonhelper.core.Constants.*;

public class HibernateContextPropertiesSupplier {

    private final SystemPropertyReader systemPropertyReader;

    public HibernateContextPropertiesSupplier() {
        this(new SystemPropertyReader());
    }

    HibernateContextPropertiesSupplier(SystemPropertyReader systemPropertyReader) {
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
        String schema = systemPropertyReader.read(SCHEMA_PROPERTY);
        if (schema != null) {
            builder.withSchema(schema);
        }
        return builder.build();
    }

    SystemPropertyReader getSystemPropertyReader() {
        return systemPropertyReader;
    }
}
