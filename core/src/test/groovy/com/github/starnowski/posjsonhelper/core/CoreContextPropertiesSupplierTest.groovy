package com.github.starnowski.posjsonhelper.core


import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posjsonhelper.core.Constants.DEFAULT_JSONB_ALL_ARRAY_STRINGS_EXIST_FUNCTION_NAME
import static com.github.starnowski.posjsonhelper.core.Constants.DEFAULT_JSONB_ANY_ARRAY_STRINGS_EXIST_FUNCTION_NAME

class CoreContextPropertiesSupplierTest extends Specification {

    @Unroll
    def "should set jsonb_all_array_strings_exist function name based on system property 'com.github.starnowski.posjsonhelper.core.functions.jsonb_all_array_strings_exist', expected value #expectedFunctionName"(){
        given:
            def spr = Mock(SystemPropertyReader)
            def tested = new CoreContextPropertiesSupplier(spr)

        when:
            def result = tested.get()

        then:
            1 * spr.read("com.github.starnowski.posjsonhelper.core.functions.jsonb_all_array_strings_exist") >> expectedFunctionName
            result.getJsonbAllArrayStringsExistFunctionReference() == expectedFunctionName

        and: "should not set other properties"
            result.getJsonbAnyArrayStringsExistFunctionReference() == DEFAULT_JSONB_ANY_ARRAY_STRINGS_EXIST_FUNCTION_NAME
            result.getSchema() == null

        where:
            expectedFunctionName << ["some_fun", "this_is_right_function"]
    }

    @Unroll
    def "should set jsonb_any_array_strings_exist function name based on system property 'com.github.starnowski.posjsonhelper.core.functions.jsonb_any_array_strings_exist', expected value #expectedFunctionName"(){
        given:
            def spr = Mock(SystemPropertyReader)
            def tested = new CoreContextPropertiesSupplier(spr)

        when:
            def result = tested.get()

        then:
            1 * spr.read("com.github.starnowski.posjsonhelper.core.functions.jsonb_any_array_strings_exist") >> expectedFunctionName
            result.getJsonbAnyArrayStringsExistFunctionReference() == expectedFunctionName

        and: "should not set other properties"
            result.getJsonbAllArrayStringsExistFunctionReference() == DEFAULT_JSONB_ALL_ARRAY_STRINGS_EXIST_FUNCTION_NAME
            result.getSchema() == null

        where:
            expectedFunctionName << ["some_fun", "this_is_right_function"]
    }

    @Unroll
    def "should set schema name based on system property 'com.github.starnowski.posjsonhelper.core.schema', expected value #expectedSchemaName"(){
        given:
            def spr = Mock(SystemPropertyReader)
            def tested = new CoreContextPropertiesSupplier(spr)

        when:
            def result = tested.get()

        then:
            1 * spr.read("com.github.starnowski.posjsonhelper.core.schema") >> expectedSchemaName
            result.getSchema() == expectedSchemaName

        and: "should not set other properties"
            result.getJsonbAnyArrayStringsExistFunctionReference() == DEFAULT_JSONB_ANY_ARRAY_STRINGS_EXIST_FUNCTION_NAME
            result.getJsonbAllArrayStringsExistFunctionReference() == DEFAULT_JSONB_ALL_ARRAY_STRINGS_EXIST_FUNCTION_NAME

        where:
            expectedSchemaName << ["non_public", "public"]
    }

    def "should have expected components initialized" (){
        given:
            def tested = new CoreContextPropertiesSupplier()

        when:
            def systemPropertyReader = tested.getSystemPropertyReader()

        then:
            systemPropertyReader.getClass() == SystemPropertyReader
    }
}
