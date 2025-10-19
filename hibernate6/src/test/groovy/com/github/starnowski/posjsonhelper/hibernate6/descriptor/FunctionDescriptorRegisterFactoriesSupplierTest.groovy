package com.github.starnowski.posjsonhelper.hibernate6.descriptor

import spock.lang.Specification

import java.util.stream.Collectors

class FunctionDescriptorRegisterFactoriesSupplierTest extends Specification {

    def "should return expected list of factories" (){
        given:
            def tested = new FunctionDescriptorRegisterFactoriesSupplier()

        when:
            def results = tested.get()

        then:
            results.stream().map({it -> it.getClass()}).collect(Collectors.toSet()) == new HashSet([JsonArrayFunctionDescriptorRegisterFactory.class,
                                                                                                    JsonbAllArrayStringsExistPredicateDescriptorRegisterFactory.class,
                                                                                                    JsonbAnyArrayStringsExistPredicateDescriptorRegisterFactory.class,
                                                                                                    JsonBExtractPathDescriptorRegisterFactory.class,
                                                                                                    JsonBExtractPathTextDescriptorRegisterFactory.class,
                                                                                                    CastOperatorFunctionDescriptorRegisterFactory.class,
                                                                                                    JsonbSetFunctionDescriptorRegisterFactory.class,
                                                                                                    ConcatenateJsonbOperatorDescriptorRegisterFactory.class,
                                                                                                    DeleteJsonbBySpecifiedPathOperatorDescriptorRegisterFactory.class,
                                                                                                    RemoveJsonValuesFromJsonArrayFunctionDescriptorRegisterFactory.class,
                                                                                                    ArrayFunctionDescriptorRegisterFactory.class])
    }

}
