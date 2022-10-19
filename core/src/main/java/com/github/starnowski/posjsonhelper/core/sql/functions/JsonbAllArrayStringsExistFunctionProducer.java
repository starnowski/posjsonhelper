package com.github.starnowski.posjsonhelper.core.sql.functions;

import java.util.Arrays;
import java.util.List;

import static com.github.starnowski.posjsonhelper.core.sql.functions.DefaultFunctionArgument.ofType;

public class JsonbAllArrayStringsExistFunctionProducer extends AbstractDefaultFunctionDefinitionFactory{

    @Override
    protected List<IFunctionArgument> prepareFunctionArguments(DefaultFunctionFactoryParameters parameters) {
        return Arrays.asList(ofType("jsonb"), ofType("text[]"));
    }

    @Override
    protected String buildBody(DefaultFunctionFactoryParameters parameters) {
        return "SELECT $1 ?& $2;";
    }

    @Override
    protected String prepareReturnType(DefaultFunctionFactoryParameters parameters) {
        return "boolean";
    }
}
