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

    public HibernateContext get(){
        HibernateContext.ContextBuilder builder = HibernateContext.builder();
        String jsonbAllArrayStringsExist = systemPropertyReader.read(JSONB_ALL_ARRAY_STRINGS_EXIST_HIBERNATE_OPERATOR_PROPERTY);
        if (jsonbAllArrayStringsExist != null) {
            builder.withJsonbAllArrayStringsExistOperator(jsonbAllArrayStringsExist);
        }
        String jsonbAnyArrayStringsExist = systemPropertyReader.read(JSONB_ANY_ARRAY_STRINGS_EXIST_HIBERNATE_OPERATOR_PROPERTY);
        if (jsonbAnyArrayStringsExist != null) {
            builder.withJsonbAnyArrayStringsExistOperator(jsonbAnyArrayStringsExist);
        }
        String jsonFunctionJsonArrayOperator = systemPropertyReader.read(JSON_FUNCTION_JSON_ARRAY_HIBERNATE_OPERATOR_PROPERTY);
        if (jsonFunctionJsonArrayOperator != null) {
            builder.withJsonFunctionJsonArrayOperator(jsonFunctionJsonArrayOperator);
        }
        return builder.build();
    }

    SystemPropertyReader getSystemPropertyReader() {
        return systemPropertyReader;
    }
}
