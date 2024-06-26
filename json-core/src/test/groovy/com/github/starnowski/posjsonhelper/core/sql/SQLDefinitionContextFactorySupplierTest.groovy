package com.github.starnowski.posjsonhelper.core.sql

import com.github.starnowski.posjsonhelper.json.core.sql.JsonbAllArrayStringsExistFunctionContextFactory
import com.github.starnowski.posjsonhelper.json.core.sql.JsonbAnyArrayStringsExistFunctionContextFactory
import com.github.starnowski.posjsonhelper.json.core.sql.RemoveJsonValuesFromJsonArrayFunctionContextFactory
import spock.lang.Specification

import java.util.stream.Collectors

class SQLDefinitionContextFactorySupplierTest extends Specification {

    def "should return expected list of factories" (){
        given:
            def tested = new SQLDefinitionContextFactorySupplier()

        when:
            def results = tested.get()

        then:
            results.stream().map({it -> it.getClass()}).collect(Collectors.toSet()) == new HashSet([JsonbAllArrayStringsExistFunctionContextFactory.class, JsonbAnyArrayStringsExistFunctionContextFactory.class, RemoveJsonValuesFromJsonArrayFunctionContextFactory.class])
    }
}
