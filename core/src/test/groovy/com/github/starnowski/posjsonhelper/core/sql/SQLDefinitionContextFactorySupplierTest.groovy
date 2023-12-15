package com.github.starnowski.posjsonhelper.core.sql

import spock.lang.Specification

import java.util.stream.Collectors

class SQLDefinitionContextFactorySupplierTest extends Specification {

    def "should return expected list of factories" (){
        given:
            def tested = new SQLDefinitionContextFactorySupplier()

        when:
            def results = tested.get()

        then:
            results.stream().map({it -> it.getClass()}).collect(Collectors.toSet()) == new HashSet([TestClasspathSQLDefinitionContextFactory1.class, TestClasspathSQLDefinitionContextFactory2.class])
    }


    //def factoriesSupplier = Mock(SQLDefinitionContextFactoryClasspathSupplier)
}
