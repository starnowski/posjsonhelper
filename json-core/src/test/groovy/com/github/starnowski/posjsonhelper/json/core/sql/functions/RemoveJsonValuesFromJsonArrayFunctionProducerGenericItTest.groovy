package com.github.starnowski.posjsonhelper.json.core.sql.functions

import com.github.starnowski.posjsonhelper.core.sql.functions.DefaultFunctionFactoryParameters

class RemoveJsonValuesFromJsonArrayFunctionProducerGenericItTest extends AbstractDefaultFunctionDefinitionFactoryGenericItTest<RemoveJsonValuesFromJsonArrayFunctionProducer, DefaultFunctionFactoryParameters> {

    @Override
    protected RemoveJsonValuesFromJsonArrayFunctionProducer returnTestedObject() {
        new RemoveJsonValuesFromJsonArrayFunctionProducer()
    }

    @Override
    protected DefaultFunctionFactoryParameters returnCorrectParametersSpyObject() {
        Spy(DefaultFunctionFactoryParameters, constructorArgs: ["some_name",
                                                                "public"])
    }
}

