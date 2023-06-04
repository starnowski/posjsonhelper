package com.github.starnowski.posjsonhelper.hibernate6.descriptor

import org.hibernate.query.sqm.function.SqmFunctionDescriptor
import org.hibernate.query.sqm.function.SqmFunctionRegistry
import org.hibernate.query.sqm.produce.function.NamedFunctionDescriptorBuilder
import spock.lang.Specification
import spock.lang.Unroll

class FunctionByNameRegisterTest extends Specification {

    @Unroll
    def "should register function if function is not registered"() {
        given:
            FunctionByNameRegister tested = new FunctionByNameRegister(hqlFunction, sqlFunction, true)
            SqmFunctionRegistry sqmFunctionRegistry = Mock(SqmFunctionRegistry)
            NamedFunctionDescriptorBuilder namedFunctionDescriptorBuilder = Mock(NamedFunctionDescriptorBuilder)
            SqmFunctionDescriptor descriptor = Mock(SqmFunctionDescriptor)

        when:
            def result = tested.registerFunction(sqmFunctionRegistry)

        then:
            1 * sqmFunctionRegistry.findFunctionDescriptor(hqlFunction) >> null
            1 * sqmFunctionRegistry.namedDescriptorBuilder(hqlFunction, sqlFunction) >> namedFunctionDescriptorBuilder
            1 * namedFunctionDescriptorBuilder.register() >> descriptor

        where:
            hqlFunction | sqlFunction
            "test1"     |   "somefunction"
            "hqlFun"    |   "jsonb_path_"
    }

    @Unroll
    def "should not register function if function is registered and override flag is false"() {
        given:
            FunctionByNameRegister tested = new FunctionByNameRegister(hqlFunction, sqlFunction, false)
            SqmFunctionRegistry sqmFunctionRegistry = Mock(SqmFunctionRegistry)
            SqmFunctionDescriptor descriptor = Mock(SqmFunctionDescriptor)

        when:
            def result = tested.registerFunction(sqmFunctionRegistry)

        then:
            1 * sqmFunctionRegistry.findFunctionDescriptor(hqlFunction) >> descriptor
            0 * sqmFunctionRegistry._

        where:
            hqlFunction | sqlFunction
            "test1"     |   "somefunction"
            "hqlFun"    |   "jsonb_path_"
    }

    @Unroll
    def "should register function when override flag is true and even if function is registered"() {
        given:
            FunctionByNameRegister tested = new FunctionByNameRegister(hqlFunction, sqlFunction, true)
            SqmFunctionRegistry sqmFunctionRegistry = Mock(SqmFunctionRegistry)
            NamedFunctionDescriptorBuilder namedFunctionDescriptorBuilder = Mock(NamedFunctionDescriptorBuilder)
            SqmFunctionDescriptor descriptor = Mock(SqmFunctionDescriptor)

        when:
            def result = tested.registerFunction(sqmFunctionRegistry)

        then:
            1 * sqmFunctionRegistry.findFunctionDescriptor(hqlFunction) >> descriptor
            1 * sqmFunctionRegistry.namedDescriptorBuilder(hqlFunction, sqlFunction) >> namedFunctionDescriptorBuilder
            1 * namedFunctionDescriptorBuilder.register() >> descriptor

        where:
            hqlFunction | sqlFunction
            "test1"     |   "somefunction"
            "hqlFun"    |   "jsonb_path_"
    }
}
