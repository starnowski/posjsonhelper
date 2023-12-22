package com.github.starnowski.posjsonhelper.hibernate6.descriptor

import com.github.starnowski.posjsonhelper.core.SystemPropertyReader
import com.github.starnowski.posjsonhelper.hibernate6.TestFunctionDescriptorRegisterFactory1
import com.github.starnowski.posjsonhelper.hibernate6.TestFunctionDescriptorRegisterFactory2
import spock.lang.Specification
import spock.lang.Unroll

import java.util.stream.Collectors

import static com.github.starnowski.posjsonhelper.hibernate6.Constants.FUNCTIONDESCRIPTORREGISTERFACTORY_TYPES_PROPERTY
import static com.github.starnowski.posjsonhelper.hibernate6.Constants.FUNCTIONDESCRIPTORREGISTERFACTORY_TYPES_EXCLUDED_PROPERTY

class FunctionDescriptorRegisterFactoriesSupplierTest extends Specification {

    def "should return expected list of factories" (){
        given:
            def tested = new FunctionDescriptorRegisterFactoriesSupplier()

        when:
            def results = tested.get()

        then:
            results.stream().map({it -> it.getClass()}).collect(Collectors.toSet()) == new HashSet([TestFunctionDescriptorRegisterFactory1.class, TestFunctionDescriptorRegisterFactory2.class, CastOperatorFunctionDescriptorRegisterFactory.class])
    }

    @Unroll
    def "should return expected list of factories #expectedTypes based on classpath when some types should be excluded based on property type #excludedTypes" (){
        given:
            def systemPropertyReader = Mock(SystemPropertyReader)
            def tested = new FunctionDescriptorRegisterFactoriesSupplier(null, systemPropertyReader)

        when:
            def results = tested.get()

        then:
            1 * systemPropertyReader.read(FUNCTIONDESCRIPTORREGISTERFACTORY_TYPES_EXCLUDED_PROPERTY) >> excludedTypes
            1 * systemPropertyReader.read(FUNCTIONDESCRIPTORREGISTERFACTORY_TYPES_PROPERTY) >> value
            results.stream().map({it -> it.getClass()}).collect(Collectors.toSet()) == expectedTypes

        where:
            value   | excludedTypes || expectedTypes
            "com.github.starnowski.posjsonhelper.hibernate6.TestFunctionDescriptorRegisterFactory1,com.github.starnowski.posjsonhelper.hibernate6.TestFunctionDescriptorRegisterFactory2" |   "com.github.starnowski.posjsonhelper.hibernate6.TestFunctionDescriptorRegisterFactory1"      ||  new HashSet([TestFunctionDescriptorRegisterFactory2.class])
            "com.github.starnowski.posjsonhelper.hibernate6.TestFunctionDescriptorRegisterFactory2,com.github.starnowski.posjsonhelper.hibernate6.TestFunctionDescriptorRegisterFactory1" | ""      ||  new HashSet([TestFunctionDescriptorRegisterFactory1.class, TestFunctionDescriptorRegisterFactory2.class])
            "com.github.starnowski.posjsonhelper.hibernate6.TestFunctionDescriptorRegisterFactory1" | "com.github.starnowski.posjsonhelper.hibernate6.TestFunctionDescriptorRegisterFactory1"        ||  new HashSet([])
            "com.github.starnowski.posjsonhelper.hibernate6.TestFunctionDescriptorRegisterFactory2,com.github.starnowski.posjsonhelper.hibernate6.descriptor.CastOperatorFunctionDescriptorRegisterFactory" | "com.github.starnowski.posjsonhelper.hibernate6.descriptor.CastOperatorFunctionDescriptorRegisterFactory"        ||  new HashSet([TestFunctionDescriptorRegisterFactory2.class])
            "com.github.starnowski.posjsonhelper.hibernate6.TestFunctionDescriptorRegisterFactory2,com.github.starnowski.posjsonhelper.hibernate6.descriptor.CastOperatorFunctionDescriptorRegisterFactory" | "com.github.starnowski.posjsonhelper.hibernate6.TestFunctionDescriptorRegisterFactory2"        ||  new HashSet([CastOperatorFunctionDescriptorRegisterFactory.class])
    }

    @Unroll
    def "should return expected list of factories specified by system property value #value" (){
        given:
            def systemPropertyReader = Mock(SystemPropertyReader)
            def tested = new FunctionDescriptorRegisterFactoriesSupplier(null, systemPropertyReader)

        when:
            def results = tested.get()

        then:
            1 * systemPropertyReader.read(FUNCTIONDESCRIPTORREGISTERFACTORY_TYPES_EXCLUDED_PROPERTY) >> null
            1 * systemPropertyReader.read(FUNCTIONDESCRIPTORREGISTERFACTORY_TYPES_PROPERTY) >> value
            results.stream().map({it -> it.getClass()}).collect(Collectors.toSet()) == expectedTypes

        where:
            value   || expectedTypes
            "com.github.starnowski.posjsonhelper.hibernate6.TestFunctionDescriptorRegisterFactory1,com.github.starnowski.posjsonhelper.hibernate6.TestFunctionDescriptorRegisterFactory2"        ||  new HashSet([TestFunctionDescriptorRegisterFactory1.class, TestFunctionDescriptorRegisterFactory2.class])
            "com.github.starnowski.posjsonhelper.hibernate6.TestFunctionDescriptorRegisterFactory2,com.github.starnowski.posjsonhelper.hibernate6.TestFunctionDescriptorRegisterFactory1"        ||  new HashSet([TestFunctionDescriptorRegisterFactory1.class, TestFunctionDescriptorRegisterFactory2.class])
            "com.github.starnowski.posjsonhelper.hibernate6.TestFunctionDescriptorRegisterFactory1"        ||  new HashSet([TestFunctionDescriptorRegisterFactory1.class])
            "com.github.starnowski.posjsonhelper.hibernate6.TestFunctionDescriptorRegisterFactory2"        ||  new HashSet([TestFunctionDescriptorRegisterFactory2.class])
    }
}
