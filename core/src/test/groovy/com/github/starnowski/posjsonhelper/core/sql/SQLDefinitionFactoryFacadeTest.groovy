package com.github.starnowski.posjsonhelper.core.sql

import com.github.starnowski.posjsonhelper.core.Context
import org.mockito.Mockito
import spock.lang.Specification
import spock.lang.Unroll

import static java.util.stream.Collectors.toList
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

class SQLDefinitionFactoryFacadeTest extends Specification {

    @Unroll
    def "should return expected definitions with creation scripts (#creationScripts)"(){
        given:
            def context = Mock(Context)
            def tested = new SQLDefinitionFactoryFacade(factories)

        when:
            def results = tested.build(context)

        then:
            results.stream().map({it -> it.getCreateScript()}).collect(toList()) == creationScripts

        where:
            factories   |   creationScripts
            [mockSQLDefinitionContextFactoryWithCreationScript("Script1")]  |   ["Script1"]
            [mockSQLDefinitionContextFactoryWithCreationScript("Script1"), mockSQLDefinitionContextFactoryThatReturnsNullDefinition(), mockSQLDefinitionContextFactoryWithCreationScript("Script2")]  |   ["Script1", "Script2"]
            [mockSQLDefinitionContextFactoryThatReturnsNullDefinition(), mockSQLDefinitionContextFactoryWithCreationScript("ScriptX"), mockSQLDefinitionContextFactoryThatReturnsNullDefinition(), mockSQLDefinitionContextFactoryWithCreationScript("Script11")]  |   ["ScriptX", "Script11"]
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
