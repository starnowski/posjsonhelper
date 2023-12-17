package com.github.starnowski.posjsonhelper.hibernate6.descriptor

import com.github.starnowski.posjsonhelper.core.Context
import com.github.starnowski.posjsonhelper.core.HibernateContext
import org.hibernate.query.sqm.function.SqmFunctionRegistry
import spock.lang.Specification

class JsonBExtractPathDescriptorRegisterFactoryTest extends Specification {

    def "should return correct function descriptor register"()
    {
        given:
        def tested = new JsonBExtractPathDescriptorRegisterFactory()

        when:
        def result = tested.get(Context.builder().build(), HibernateContext.builder().build())

        then:
        result.class == AbstractJsonBExtractPathDescriptorRegister
    }

    def "function descriptor register should register correct function with key"()
    {
        given:
            def tested = new JsonBExtractPathDescriptorRegisterFactory()
            def functionRegister = tested.get(Context.builder().build(), HibernateContext.builder().build())
            def sqmFunctionRegistry = new SqmFunctionRegistry()

        when:
            functionRegister.registerFunction(sqmFunctionRegistry)

        then:
            def result = sqmFunctionRegistry.findFunctionDescriptor("jsonb_extract_path")
            result.class == JsonBExtractPathDescriptor
    }
}
