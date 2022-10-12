package com.github.starnowski.posjsonhelper.core.operations

import com.github.starnowski.posjsonhelper.core.operations.exceptions.ValidationDatabaseOperationsException
import com.github.starnowski.posjsonhelper.core.operations.util.SQLUtil
import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinition
import org.mockito.Mockito
import spock.lang.Specification
import spock.lang.Unroll

import javax.sql.DataSource

class ValidateOperationsProcessorTest extends Specification {

    def "should return default instance type"(){
        given:
            def tested = new ValidateOperationsProcessor()

        when:
            def result = tested.getSqlUtil()

        then:
        result.getClass() == SQLUtil.class
    }

    @Unroll
    def "should throw an exception when some checks fails #checkQueriersRestuls, expected failed checks map #expectedInvalidChecks"(){
        given:
            def tested = new ValidateOperationsProcessor()
            def dataSource = Mock(DataSource)

        when:
            tested.run(dataSource, null)

        then:
            def ex = thrown(ValidationDatabaseOperationsException)

        where:
            definitions |   checkQueriersRestuls    ||  expectedInvalidChecks
            [sqlDef("cre1", ["check1", "analyst"]), sqlDef("cre2", ["check", "check15"])]   |   [check1:1, analyst:15, check:-1,check15:3]  || ["cre2":[new HashSet<>(Arrays.asList("check"))]]
    }

    private static ISQLDefinition sqlDef(String createScript, List<String> checkScripts){
        ISQLDefinition definition = Mockito.mock(ISQLDefinition)
        Mockito.when(definition.getCreateScript()).thenReturn(createScript)
        Mockito.when(definition.getCheckingStatements()).thenReturn(checkScripts)
        definition
    }
}
