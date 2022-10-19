package com.github.starnowski.posjsonhelper.core.operations


import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinition
import nl.altindag.log.LogCaptor
import org.mockito.Mockito
import spock.lang.Specification
import spock.lang.Unroll

class DatabaseOperationsLoggerProcessorTest extends Specification {

    @Unroll
    def "should log all scripts #expectedCreateScriptsMessages, #expectedDropScriptsMessages, #expectedCheckScriptsMessages"(){
        given:
            LogCaptor logCaptor = LogCaptor.forClass(DatabaseOperationsLoggerProcessor)
            def tested = new DatabaseOperationsLoggerProcessor()
            def expectedInfoLogs = new ArrayList()
            expectedInfoLogs.add("Creation scripts")
            expectedInfoLogs.addAll(expectedCreateScriptsMessages)
            expectedInfoLogs.add("Drop scripts")
            expectedInfoLogs.add(expectedDropScriptsMessages)
            expectedInfoLogs.add("Validation scripts")
            expectedInfoLogs.add(expectedCheckScriptsMessages)

        when:
            tested.run(null, definitions)

        then:
            logCaptor.getInfoLogs() == expectedInfoLogs

        where:
            definitions    ||  expectedCreateScriptsMessages   |  expectedDropScriptsMessages |  expectedCheckScriptsMessages
            [sqlDef("cre1", "DROPX", ["check1", "analyst"]), sqlDef("cre2", "drop All", ["check", "check15"])]  ||  ["cre1", "cre2"]    |   ["DROPX", "drop All"]   |   ["check1", "analyst", "check", "check15"]
    }

    private static ISQLDefinition sqlDef(String createScript, String dropScript, List<String> checkScripts){
        ISQLDefinition definition = Mockito.mock(ISQLDefinition)
        Mockito.when(definition.getCreateScript()).thenReturn(createScript)
        Mockito.when(definition.getDropScript()).thenReturn(dropScript)
        Mockito.when(definition.getCheckingStatements()).thenReturn(checkScripts)
        definition
    }
}


