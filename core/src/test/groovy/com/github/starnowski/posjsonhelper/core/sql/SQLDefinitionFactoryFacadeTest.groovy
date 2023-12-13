package com.github.starnowski.posjsonhelper.core.sql

import com.github.starnowski.posjsonhelper.core.Context
import org.mockito.Mockito
import org.mockito.internal.verification.Times
import spock.lang.Specification
import spock.lang.Unroll

import java.util.stream.Collectors

import static java.util.stream.Collectors.toList
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

class SQLDefinitionFactoryFacadeTest extends Specification {

    @Unroll
    def "should return expected definitions with creation scripts (#creationScripts)"(){
        given:
            def context = Mock(Context)
            def factoriesSupplier = Mock(SQLDefinitionContextFactoryClasspathSupplier)
            factoriesSupplier.get() >> factories
            def tested = new SQLDefinitionFactoryFacade(factoriesSupplier)

        when:
            def results = tested.build(context)

        then:
            results.stream().map({it -> it.getCreateScript()}).collect(toList()) == creationScripts

        and:
            factories.forEach({factory ->
                Mockito.verify(factory, new Times(1)).build(context)
            })

        where:
            factories   |   creationScripts
            [mockSQLDefinitionContextFactoryWithCreationScript("Script1")]  |   ["Script1"]
            [mockSQLDefinitionContextFactoryWithCreationScript("Script1"), mockSQLDefinitionContextFactoryThatReturnsNullDefinition(), mockSQLDefinitionContextFactoryWithCreationScript("Script2")]  |   ["Script1", "Script2"]
            [mockSQLDefinitionContextFactoryThatReturnsNullDefinition(), mockSQLDefinitionContextFactoryWithCreationScript("ScriptX"), mockSQLDefinitionContextFactoryThatReturnsNullDefinition(), mockSQLDefinitionContextFactoryWithCreationScript("Script11")]  |   ["ScriptX", "Script11"]
    }

    def "should have return empty list of factories by default because core module does not contains factories implementaiotn" (){
        given:
            def tested = new SQLDefinitionFactoryFacade()

        when:
            def results = tested.getFactoriesCopy()

        then:
            results.stream().map({it -> it.getClass()}).collect(Collectors.toList()) == []
    }

    private static ISQLDefinitionContextFactory mockSQLDefinitionContextFactoryWithCreationScript(String creationScript)
    {
        ISQLDefinitionContextFactory factory = mock(ISQLDefinitionContextFactory.class)
        ISQLDefinition definition = mock(ISQLDefinition.class)
        when(definition.getCreateScript()).thenReturn(creationScript)
        when(factory.build(Mockito.any())).thenReturn(definition)
        factory
    }

    private static ISQLDefinitionContextFactory mockSQLDefinitionContextFactoryThatReturnsNullDefinition()
    {
        ISQLDefinitionContextFactory factory = mock(ISQLDefinitionContextFactory.class)
        when(factory.build(Mockito.any())).thenReturn(null)
        factory
    }
}
