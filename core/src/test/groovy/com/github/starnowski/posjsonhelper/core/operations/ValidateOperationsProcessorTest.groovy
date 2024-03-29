package com.github.starnowski.posjsonhelper.core.operations

import com.github.starnowski.posjsonhelper.core.operations.exceptions.ValidationDatabaseOperationsException
import com.github.starnowski.posjsonhelper.core.operations.util.SQLUtil
import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinition
import org.mockito.Mockito
import spock.lang.Specification
import spock.lang.Unroll

import javax.sql.DataSource
import java.sql.Connection
import java.sql.SQLClientInfoException
import java.sql.SQLException

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
            def sqlUtil = Mock(SQLUtil)
            def tested = new ValidateOperationsProcessor(sqlUtil)
            def dataSource = Mock(DataSource)
            def connection = Mock(Connection)
            dataSource.getConnection() >> connection
            checkQueriersRestuls.entrySet().forEach({ it ->
                sqlUtil.returnLongResultForQuery(connection, it.getKey()) >> it.getValue()
            })

        when:
            tested.run(dataSource, definitions)

        then:
            def ex = thrown(ValidationDatabaseOperationsException)
            ex.failedChecks == expectedInvalidChecks

        where:
            definitions |   checkQueriersRestuls    ||  expectedInvalidChecks
            [sqlDef("cre1", ["check1", "analyst"]), sqlDef("cre2", ["check", "check15"])]   |   [check1:1, analyst:15, check:-1,check15:3]  || ["cre2":new HashSet<>(Arrays.asList("check"))]
            [sqlDef("creX", ["c1", "analyst"]), sqlDef("creY", ["check", "check15"]), sqlDef("creX", ["checkX1", "checkX2"])]   |   [check1:1, analyst:15, check:-1,check15:3,checkX1:-9,checkX2:0, c1:-10]  || ["creY":new HashSet<>(Arrays.asList("check")), "creX":new HashSet<>(Arrays.asList("checkX1", "checkX2", "c1"))]
    }

    @Unroll
    def "should throw an exception when some checks fails #checkQueriersRestuls, expected error massege started with #errorMessageStart"(){
        given:
            def sqlUtil = Mock(SQLUtil)
            def tested = new ValidateOperationsProcessor(sqlUtil)
            def dataSource = Mock(DataSource)
            def connection = Mock(Connection)
            dataSource.getConnection() >> connection
            checkQueriersRestuls.entrySet().forEach({ it ->
                sqlUtil.returnLongResultForQuery(connection, it.getKey()) >> it.getValue()
            })

        when:
            tested.run(dataSource, definitions)

        then:
            def ex = thrown(ValidationDatabaseOperationsException)
            ex.message == errorMessageStart

        where:
            definitions |   checkQueriersRestuls    ||  errorMessageStart
            [sqlDef("cre1", ["check1", "analyst"]), sqlDef("cre2", ["check", "check15"])]   |   [check1:1, analyst:15, check:-1,check15:3]  || "Failed check statements for ddl instruction \"cre2\", failed checks [\"check\"]"
            [sqlDef("creX", ["c1", "analyst"]), sqlDef("creY", ["check", "check15"]), sqlDef("creX", ["checkX1", "checkX2"])]   |   [check1:1, analyst:15, check:-1,check15:0,checkX1:-9,checkX2:0, c1:13]  || "Failed check statements for ddl instruction \"creY\", failed checks [\"check15\", \"check\"]"
    }

    @Unroll
    def "should not throw any exception when all checks passed #checkQueriersRestuls"(){
        given:
            def sqlUtil = Mock(SQLUtil)
            def tested = new ValidateOperationsProcessor(sqlUtil)
            def dataSource = Mock(DataSource)
            def connection = Mock(Connection)
            dataSource.getConnection() >> connection
            checkQueriersRestuls.entrySet().forEach({ it ->
                sqlUtil.returnLongResultForQuery(connection, it.getKey()) >> it.getValue()
            })

        when:
            tested.run(dataSource, definitions)

        then:
            noExceptionThrown()

        where:
            definitions                                                                                                         |   checkQueriersRestuls
            [sqlDef("cre1", ["check1", "analyst"]), sqlDef("cre2", ["check", "check15"])]                                       |   [check1:1, analyst:15, check:6,check15:3]
            [sqlDef("creX", ["c1", "analyst"]), sqlDef("creY", ["check", "check15"]), sqlDef("creX", ["checkX1", "checkX2"])]   |   [check1:1, analyst:15, check:9,check15:3,checkX1:7,checkX2:1, c1:1]
    }

    @Unroll
    def "should throw an exception when there is some sql exception occurred"(){
        given:
            def sqlUtil = Mock(SQLUtil)
            def tested = new ValidateOperationsProcessor(sqlUtil)
            def dataSource = Mock(DataSource)
            def connection = Mock(Connection)
            dataSource.getConnection() >> connection
            def sqlException = new SQLException()
            checkQueriersRestuls.entrySet().forEach({ it ->
                sqlUtil.returnLongResultForQuery(connection, it.getKey()) >> { throw sqlException}
            })

        when:
            tested.run(dataSource, definitions)

        then:
            def ex = thrown(RuntimeException)
            ex.getCause().is(sqlException)

        where:
            definitions                                                                                                         |   checkQueriersRestuls
            [sqlDef("cre1", ["check1", "analyst"]), sqlDef("cre2", ["check", "check15"])]                                       |   [check1:1, analyst:15, check:6,check15:3]
            [sqlDef("creX", ["c1", "analyst"]), sqlDef("creY", ["check", "check15"]), sqlDef("creX", ["checkX1", "checkX2"])]   |   [check1:1, analyst:15, check:9,check15:3,checkX1:7,checkX2:1, c1:1]
    }

    private static ISQLDefinition sqlDef(String createScript, List<String> checkScripts){
        ISQLDefinition definition = Mockito.mock(ISQLDefinition)
        Mockito.when(definition.getCreateScript()).thenReturn(createScript)
        Mockito.when(definition.getCheckingStatements()).thenReturn(checkScripts)
        definition
    }
}
