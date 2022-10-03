package com.github.starnowski.posjsonhelper.core.sql.functions;

import java.util.Arrays;
import java.util.List;

import static com.github.starnowski.posjsonhelper.core.sql.functions.DefaultFunctionArgument.ofType;

public class JsonbAllArrayStringsExistFunctionProducer extends AbstractDefaultFunctionDefinitionFactory{
    @Override
    protected String produceStatement(DefaultFunctionFactoryParameters parameters) {
        return null;
    }

    @Override
    protected List<IFunctionArgument> prepareFunctionArguments(DefaultFunctionFactoryParameters parameters) {
        return Arrays.asList(ofType("jsonb"), ofType("text[]"));
    }
}
