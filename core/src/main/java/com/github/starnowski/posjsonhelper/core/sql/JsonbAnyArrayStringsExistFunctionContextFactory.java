package com.github.starnowski.posjsonhelper.core.sql;

import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.core.sql.functions.JsonbAnyArrayStringsExistFunctionProducer;

public class JsonbAnyArrayStringsExistFunctionContextFactory implements ISQLDefinitionContextFactory {

    private final JsonbAnyArrayStringsExistFunctionProducer factory;

    public JsonbAnyArrayStringsExistFunctionContextFactory(){
        this(new JsonbAnyArrayStringsExistFunctionProducer());
    }

    JsonbAnyArrayStringsExistFunctionContextFactory(JsonbAnyArrayStringsExistFunctionProducer factory) {
        this.factory = factory;
    }

    @Override
    public ISQLDefinition build(Context context) {
        return null;
    }
}
