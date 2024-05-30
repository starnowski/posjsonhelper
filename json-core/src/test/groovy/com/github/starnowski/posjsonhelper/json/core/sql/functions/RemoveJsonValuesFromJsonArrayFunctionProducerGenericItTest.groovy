package com.github.starnowski.posjsonhelper.json.core.sql.functions

import com.github.starnowski.posjsonhelper.core.sql.functions.DefaultFunctionFactoryParameters
import spock.lang.Unroll

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

import static com.github.starnowski.posjsonhelper.test.utils.TestUtils.normalizeLineEndings

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

