package com.github.starnowski.posjsonhelper.core.sql.functions


import spock.lang.Unroll

class JsonbAnyArrayStringsExistFunctionProducerGenericTest extends AbstractDefaultFunctionDefinitionFactoryGenericTest<JsonbAnyArrayStringsExistFunctionProducer, DefaultFunctionFactoryParameters> {

    @Unroll
    def "should generate statement that creates function '#testFunctionName' for schema '#testSchema'" () {
        expect:
        returnTestedObject().produce(new DefaultFunctionFactoryParameters(testFunctionName, testSchema)).getCreateScript() == expectedStatement

        where:
        testSchema              |   testFunctionName                        || expectedStatement
        null                    |   "jsonb_all_array_strings_exist"         ||  "CREATE OR REPLACE FUNCTION jsonb_all_array_strings_exist(jsonb, text[]) RETURNS boolean AS \$\$\nSELECT \$1 ?| \$2;\n\$\$ LANGUAGE SQL;"
        "public"                |   "json_fun_exist"                        ||  "CREATE OR REPLACE FUNCTION public.json_fun_exist(jsonb, text[]) RETURNS boolean AS \$\$\nSELECT \$1 ?| \$2;\n\$\$ LANGUAGE SQL;"
        "non_public_schema"     |   "someFun"                               ||  "CREATE OR REPLACE FUNCTION non_public_schema.someFun(jsonb, text[]) RETURNS boolean AS \$\$\nSELECT \$1 ?| \$2;\n\$\$ LANGUAGE SQL;"
    }

    @Override
    protected String prepareArgumentsPhrase() {
        "jsonb, text[]"
    }

    @Override
    protected JsonbAnyArrayStringsExistFunctionProducer returnTestedObject() {
        new JsonbAnyArrayStringsExistFunctionProducer()
    }

    @Override
    protected DefaultFunctionFactoryParameters returnCorrectParametersSpyObject() {
        Spy(DefaultFunctionFactoryParameters, constructorArgs: ["jsonb_any_array_strings_exist",
                                                                "public"])
    }
}