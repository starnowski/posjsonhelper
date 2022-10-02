package com.github.starnowski.posjsonhelper.core.sql.functions

class JsonbAllArrayStringsExistFunctionProducerGenericTest extends AbstractDefaultFunctionDefinitionFactoryGenericTest<JsonbAllArrayStringsExistFunctionProducer, DefaultFunctionFactoryParameters> {
    @Override
    protected JsonbAllArrayStringsExistFunctionProducer returnTestedObject() {
        new JsonbAllArrayStringsExistFunctionProducer()
    }

    @Override
    protected DefaultFunctionFactoryParameters returnCorrectParametersSpyObject() {
        Spy(DefaultFunctionFactoryParameters, constructorArgs: ["jsonb_all_arrays_string_exists",
                                                                                            "public"])
    }
}
