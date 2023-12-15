package com.github.starnowski.posjsonhelper.core.sql

import spock.lang.Specification

import java.util.stream.Collectors

class SQLDefinitionContextFactoryClasspathSupplierTest extends Specification {

    def "should return expected list of factories" (){
        given:
            def tested = new SQLDefinitionContextFactoryClasspathSupplier()

        when:
            def results = tested.get()

        then:
            results.stream().map({it -> it.getClass()}).collect(Collectors.toSet()) == new HashSet([TestClasspathSQLDefinitionContextFactory1.class, TestClasspathSQLDefinitionContextFactory2.class])
    }
}
