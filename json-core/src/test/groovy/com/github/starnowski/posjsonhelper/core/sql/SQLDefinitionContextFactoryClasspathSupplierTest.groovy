package com.github.starnowski.posjsonhelper.core.sql

import com.github.starnowski.posjsonhelper.json.core.sql.JsonbAllArrayStringsExistFunctionContextFactory
import com.github.starnowski.posjsonhelper.json.core.sql.JsonbAnyArrayStringsExistFunctionContextFactory
import spock.lang.Specification

import java.util.stream.Collectors

class SQLDefinitionContextFactoryClasspathSupplierTest extends Specification {

    def "should return expected list of factories" (){
        given:
            def tested = new SQLDefinitionContextFactoryClasspathSupplier()

        when:
            def results = tested.get()

        then:
            results.stream().map({it -> it.getClass()}).collect(Collectors.toSet()) == new HashSet([JsonbAllArrayStringsExistFunctionContextFactory.class, JsonbAnyArrayStringsExistFunctionContextFactory.class])
    }
}
