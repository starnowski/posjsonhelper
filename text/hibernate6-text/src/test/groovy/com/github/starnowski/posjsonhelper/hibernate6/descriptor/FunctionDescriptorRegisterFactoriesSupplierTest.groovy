package com.github.starnowski.posjsonhelper.hibernate6.descriptor

import com.github.starnowski.posjsonhelper.text.hibernate6.descriptor.PhraseToTSQueryFunctionDescriptorRegisterFactory
import com.github.starnowski.posjsonhelper.text.hibernate6.descriptor.PlainToTSQueryFunctionDescriptorRegisterFactory
import com.github.starnowski.posjsonhelper.text.hibernate6.descriptor.TSVectorFunctionDescriptorRegisterFactory
import com.github.starnowski.posjsonhelper.text.hibernate6.descriptor.TextOperatorFunctionDescriptorRegisterFactory
import spock.lang.Specification

import java.util.stream.Collectors

class FunctionDescriptorRegisterFactoriesSupplierTest extends Specification {

    def "should return expected list of factories" (){
        given:
        def tested = new FunctionDescriptorRegisterFactoriesSupplier()

        when:
        def results = tested.get()

        then:
        results.stream().map({it -> it.getClass()}).collect(Collectors.toSet()) == new HashSet([PhraseToTSQueryFunctionDescriptorRegisterFactory.class,
                                                                                                PlainToTSQueryFunctionDescriptorRegisterFactory.class,
                                                                                                TextOperatorFunctionDescriptorRegisterFactory.class,
                                                                                                TSVectorFunctionDescriptorRegisterFactory.class,
                                                                                                CastOperatorFunctionDescriptorRegisterFactory.class])
    }

}
