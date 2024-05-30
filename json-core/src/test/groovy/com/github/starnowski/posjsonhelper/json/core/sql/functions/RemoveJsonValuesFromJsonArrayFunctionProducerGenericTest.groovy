package com.github.starnowski.posjsonhelper.json.core.sql.functions

import com.github.starnowski.posjsonhelper.core.sql.functions.DefaultFunctionFactoryParameters
import spock.lang.Unroll

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

class RemoveJsonValuesFromJsonArrayFunctionProducerGenericTest extends AbstractDefaultFunctionDefinitionFactoryGenericTest<RemoveJsonValuesFromJsonArrayFunctionProducer, DefaultFunctionFactoryParameters> {

    private static final String REMOVE_JSON_VALUES_FUNCTION_CREATE_DEF_TEMPLATE;
    static {
        def uri = RemoveJsonValuesFromJsonArrayFunctionProducerGenericTest.class.getClassLoader().getResource("remove_json_values_template.sql").toURI()
        byte[] fileBytes = Files.readAllBytes(Paths.get(uri))
        REMOVE_JSON_VALUES_FUNCTION_CREATE_DEF_TEMPLATE = new String(fileBytes, StandardCharsets.UTF_8)
    }

    @Unroll
    def "should generate statement that creates function '#testFunctionName' for schema '#testSchema'" () {
        expect:
            returnTestedObject().produce(new DefaultFunctionFactoryParameters(testFunctionName, testSchema)).getCreateScript() == expectedStatement

        where:
        testSchema              |   testFunctionName                        || expectedStatement
        null                    |   "jsonb_all_array_strings_exist"         ||  REMOVE_JSON_VALUES_FUNCTION_CREATE_DEF_TEMPLATE.replace("{{function_reference}}", "jsonb_all_array_strings_exist")
        "public"                |   "json_fun_exist"                        ||  REMOVE_JSON_VALUES_FUNCTION_CREATE_DEF_TEMPLATE.replace("{{function_reference}}", "public.json_fun_exist")
        "non_public_schema"     |   "someFun"                               ||  REMOVE_JSON_VALUES_FUNCTION_CREATE_DEF_TEMPLATE.replace("{{function_reference}}", "non_public_schema.someFun")
    }

    @Override
    protected String prepareArgumentsPhrase() {
        "input_json jsonb, values_to_remove jsonb"
    }

    @Override
    protected RemoveJsonValuesFromJsonArrayFunctionProducer returnTestedObject() {
        new RemoveJsonValuesFromJsonArrayFunctionProducer()
    }

    @Override
    protected DefaultFunctionFactoryParameters returnCorrectParametersSpyObject() {
        Spy(DefaultFunctionFactoryParameters, constructorArgs: ["remove_json_values_from_json_array",
                                                                "public"])
    }
}
