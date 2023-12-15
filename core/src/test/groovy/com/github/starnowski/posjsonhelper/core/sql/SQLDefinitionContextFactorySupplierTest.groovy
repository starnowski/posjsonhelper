package com.github.starnowski.posjsonhelper.core.sql

import com.github.starnowski.posjsonhelper.core.SystemPropertyReader
import spock.lang.Specification
import spock.lang.Unroll

import java.util.stream.Collectors

import static com.github.starnowski.posjsonhelper.core.Constants.SQLDEFINITIONCONTEXTFACTORY_TYPES_PROPERTY

class SQLDefinitionContextFactorySupplierTest extends Specification {

    def "should return expected list of factories" (){
        given:
            def tested = new SQLDefinitionContextFactorySupplier()

        when:
            def results = tested.get()

        then:
            results.stream().map({it -> it.getClass()}).collect(Collectors.toSet()) == new HashSet([TestClasspathSQLDefinitionContextFactory1.class, TestClasspathSQLDefinitionContextFactory2.class])
    }

    @Unroll
    def "should return expected list of factories specified by system property value #value" (){
        given:
            def systemPropertyReader = Mock(SystemPropertyReader)
            def tested = new SQLDefinitionContextFactorySupplier(null, systemPropertyReader)

        when:
            def results = tested.get()

        then:
            1 * systemPropertyReader.read(SQLDEFINITIONCONTEXTFACTORY_TYPES_PROPERTY) >> value
            results.stream().map({it -> it.getClass()}).collect(Collectors.toSet()) == expectedTypes

        where:
            value   || expectedTypes
            "com.github.starnowski.posjsonhelper.core.sql.TestClasspathSQLDefinitionContextFactory1,com.github.starnowski.posjsonhelper.core.sql.TestClasspathSQLDefinitionContextFactory2"        ||  new HashSet([TestClasspathSQLDefinitionContextFactory1.class, TestClasspathSQLDefinitionContextFactory2.class])
            "com.github.starnowski.posjsonhelper.core.sql.TestClasspathSQLDefinitionContextFactory2,com.github.starnowski.posjsonhelper.core.sql.TestClasspathSQLDefinitionContextFactory1"        ||  new HashSet([TestClasspathSQLDefinitionContextFactory1.class, TestClasspathSQLDefinitionContextFactory2.class])
            "com.github.starnowski.posjsonhelper.core.sql.TestClasspathSQLDefinitionContextFactory1"        ||  new HashSet([TestClasspathSQLDefinitionContextFactory1.class])
            "com.github.starnowski.posjsonhelper.core.sql.TestClasspathSQLDefinitionContextFactory2"        ||  new HashSet([TestClasspathSQLDefinitionContextFactory2.class])
    }

}
