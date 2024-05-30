package com.github.starnowski.posjsonhelper.json.core.sql.functions;

import com.github.starnowski.posjsonhelper.core.sql.functions.AbstractDefaultFunctionDefinitionFactory;
import com.github.starnowski.posjsonhelper.core.sql.functions.DefaultFunctionFactoryParameters;
import com.github.starnowski.posjsonhelper.core.sql.functions.IFunctionArgument;

import java.util.Arrays;
import java.util.List;

import static com.github.starnowski.posjsonhelper.core.sql.functions.DefaultFunctionArgument.withNameAndType;

public class RemoveJsonValuesFromJsonArrayFunctionProducer extends AbstractDefaultFunctionDefinitionFactory {
    @Override
    protected List<IFunctionArgument> prepareFunctionArguments(DefaultFunctionFactoryParameters parameters) {
        return Arrays.asList(withNameAndType("input_json", "jsonb"), withNameAndType("values_to_remove", "jsonb"));
    }

    @Override
    protected String buildBody(DefaultFunctionFactoryParameters parameters) {
        return null;
    }

    @Override
    protected String prepareReturnType(DefaultFunctionFactoryParameters parameters) {
        return "jsonb";
    }
}
