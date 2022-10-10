package com.github.starnowski.posjsonhelper.core

import com.github.starnowski.posjsonhelper.core.operations.IDatabaseOperationsProcessor
import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinition
import spock.lang.Specification

import javax.sql.DataSource

class DatabaseOperationExecutorTest extends Specification {

    def"should invoke correct operation processor for CREATE operation"()
    {
        given:
            def createOperationProcessor = Mock(IDatabaseOperationsProcessor)
            def dropOperationProcessor = Mock(IDatabaseOperationsProcessor)
            def validateOperationProcessor = Mock(IDatabaseOperationsProcessor)
            Map<DatabaseOperationType, IDatabaseOperationsProcessor> operationsProcessorMap = new HashMap<>();
            operationsProcessorMap.put(DatabaseOperationType.DROP, dropOperationProcessor)
            operationsProcessorMap.put(DatabaseOperationType.CREATE, createOperationProcessor)
            operationsProcessorMap.put(DatabaseOperationType.VALIDATE, validateOperationProcessor)
            def tested = new DatabaseOperationExecutor(operationsProcessorMap)
            DataSource dataSource = Mock(DataSource)
            List<ISQLDefinition> sqlDefinitions = new ArrayList<>()

        when:
            tested.execute(dataSource, sqlDefinitions, DatabaseOperationType.CREATE)

        then:
            1 * createOperationProcessor.run(dataSource, sqlDefinitions)

        and: "no other processor should be invoked"
            0 * dropOperationProcessor.run(dataSource, sqlDefinitions)
            0 * validateOperationProcessor.run(dataSource, sqlDefinitions)
    }
}
