package com.github.starnowski.posjsonhelper.json.core.sql;

import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinition;
import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinitionContextFactory;
import com.github.starnowski.posjsonhelper.core.sql.functions.DefaultFunctionFactoryParameters;
import com.github.starnowski.posjsonhelper.json.core.sql.functions.RemoveJsonValuesFromJsonArrayFunctionProducer;

public class RemoveJsonValuesFromJsonArrayFunctionContextFactory implements ISQLDefinitionContextFactory {

    private final RemoveJsonValuesFromJsonArrayFunctionProducer producer;

    public RemoveJsonValuesFromJsonArrayFunctionContextFactory() {
        this(new RemoveJsonValuesFromJsonArrayFunctionProducer());
    }
    RemoveJsonValuesFromJsonArrayFunctionContextFactory(RemoveJsonValuesFromJsonArrayFunctionProducer producer) {
        this.producer = producer;
    }

    @Override
    public ISQLDefinition build(Context context) {
        return producer.produce(new DefaultFunctionFactoryParameters(context.getRemoveValuesFromJsonArrayFunctionReference(), context.getSchema()));
    }
}
