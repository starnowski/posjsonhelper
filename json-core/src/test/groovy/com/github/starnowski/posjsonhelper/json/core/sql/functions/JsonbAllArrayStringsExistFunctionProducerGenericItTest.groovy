package com.github.starnowski.posjsonhelper.json.core.sql.functions

import com.github.starnowski.posjsonhelper.core.sql.functions.DefaultFunctionFactoryParameters

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
