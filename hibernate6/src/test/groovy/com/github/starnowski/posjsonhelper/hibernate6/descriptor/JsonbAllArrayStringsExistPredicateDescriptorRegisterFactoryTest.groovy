package com.github.starnowski.posjsonhelper.hibernate6.descriptor

import com.github.starnowski.posjsonhelper.core.Context
import com.github.starnowski.posjsonhelper.core.HibernateContext
import org.hibernate.query.sqm.function.SqmFunctionRegistry
import spock.lang.Specification
import spock.lang.Unroll

class JsonbAllArrayStringsExistPredicateDescriptorRegisterFactoryTest extends Specification {

    def "should return correct function descriptor register"()
    {
        given:
            def tested = new JsonbAllArrayStringsExistPredicateDescriptorRegisterFactory()

        when:
            def result = tested.get(Context.builder().build(), HibernateContext.builder().build())

        then:
            result.class == AbstractJsonbArrayStringsExistPredicateDescriptorRegister
    }

    @Unroll
    def "function descriptor register should register correct function with key #expectedFunction"()
    {
        given:
            def tested = new JsonbAllArrayStringsExistPredicateDescriptorRegisterFactory()
            def functionRegister = tested.get(Context.builder().build(), hibernateContext)
            def sqmFunctionRegistry = new SqmFunctionRegistry()

        when:
            functionRegister.registerFunction(sqmFunctionRegistry)

        then:
            def result = sqmFunctionRegistry.findFunctionDescriptor(expectedFunction)
            result.class == JsonbAllArrayStringsExistPredicateDescriptor

        where:
            hibernateContext || expectedFunction
            HibernateContext.builder().build()  ||  "jsonb_all_array_strings_exist"
            HibernateContext.builder().withJsonbAllArrayStringsExistOperator("json_111_function").build()  ||  "json_111_function"
    }
}
