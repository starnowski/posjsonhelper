package com.github.starnowski.posjsonhelper.json.core.sql.functions


import com.github.starnowski.posjsonhelper.core.sql.functions.DefaultFunctionFactoryParameters

class JsonbAnyArrayStringsExistFunctionProducerGenericItTest extends AbstractDefaultFunctionDefinitionFactoryGenericItTest<JsonbAnyArrayStringsExistFunctionProducer, DefaultFunctionFactoryParameters> {

    @Override
    protected JsonbAnyArrayStringsExistFunctionProducer returnTestedObject() {
        new JsonbAnyArrayStringsExistFunctionProducer()
    }

    @Override
    protected DefaultFunctionFactoryParameters returnCorrectParametersSpyObject() {
        Spy(DefaultFunctionFactoryParameters, constructorArgs: ["some_name_for_fun",
                                                                "public"])
    }
}
