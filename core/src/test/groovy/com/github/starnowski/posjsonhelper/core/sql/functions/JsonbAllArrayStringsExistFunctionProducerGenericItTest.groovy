package com.github.starnowski.posjsonhelper.core.sql.functions

class JsonbAllArrayStringsExistFunctionProducerGenericItTest extends AbstractDefaultFunctionDefinitionFactoryGenericItTest<JsonbAllArrayStringsExistFunctionProducer, DefaultFunctionFactoryParameters> {

    @Override
    protected JsonbAllArrayStringsExistFunctionProducer returnTestedObject() {
        new JsonbAllArrayStringsExistFunctionProducer()
    }

    @Override
    protected DefaultFunctionFactoryParameters returnCorrectParametersSpyObject() {
        Spy(DefaultFunctionFactoryParameters, constructorArgs: ["some_name",
                                                                "public"])
    }
}
