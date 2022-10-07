package com.github.starnowski.posjsonhelper.core.sql;

import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.core.sql.functions.JsonbAllArrayStringsExistFunctionProducer;

public class JsonbAllArrayStringsExistFunctionContextFactory implements ISQLDefinitionContextFactory {

    private final JsonbAllArrayStringsExistFunctionProducer factory;

    public JsonbAllArrayStringsExistFunctionContextFactory() {
        this(new JsonbAllArrayStringsExistFunctionProducer());
    }

    JsonbAllArrayStringsExistFunctionContextFactory(JsonbAllArrayStringsExistFunctionProducer factory) {
        this.factory = factory;
    }

    @Override
    public ISQLDefinition build(Context context) {
        return null;
    }
}
