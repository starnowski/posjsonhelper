package com.github.starnowski.posjsonhelper.hibernate6.descriptor

import com.github.starnowski.posjsonhelper.core.Context
import com.github.starnowski.posjsonhelper.core.HibernateContext
import org.hibernate.query.sqm.function.SqmFunctionRegistry
import spock.lang.Specification
import spock.lang.Unroll

class JsonArrayFunctionDescriptorRegisterFactoryTest extends Specification {

    def "should return correct function descriptor register"()
    {
        given:
            def tested = new JsonArrayFunctionDescriptorRegisterFactory()

        when:
            def result = tested.get(Context.builder().build(), HibernateContext.builder().build())

        then:
            result.class == JsonArrayFunctionDescriptorRegister
    }

    @Unroll
    def "function descriptor register should register correct function with key #expectedFunction"()
    {
        given:
            def tested = new JsonArrayFunctionDescriptorRegisterFactory()
            def functionRegister = tested.get(Context.builder().build(), hibernateContext)
            def sqmFunctionRegistry = new SqmFunctionRegistry()

        when:
            functionRegister.registerFunction(sqmFunctionRegistry)

        then:
            def result = sqmFunctionRegistry.findFunctionDescriptor(expectedFunction)
            result.class == JsonArrayFunctionDescriptor

        where:
            hibernateContext || expectedFunction
            HibernateContext.builder().build()  ||  "json_function_json_array"
            HibernateContext.builder().withJsonFunctionJsonArrayOperator("json_function_xxxx").build()  ||  "json_function_xxxx"
    }
}
