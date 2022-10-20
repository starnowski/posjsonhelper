package com.github.starnowski.posjsonhelper.core


import com.github.starnowski.posjsonhelper.core.operations.exceptions.ValidationDatabaseOperationsException
import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinition
import com.github.starnowski.posjsonhelper.core.sql.SQLDefinitionFactoryFacade
import spock.lang.Specification
import spock.lang.Unroll

import javax.sql.DataSource
import java.sql.SQLException

import static com.github.starnowski.posjsonhelper.core.DatabaseOperationType.*

class DatabaseOperationExecutorFacadeTest extends Specification {

    @Unroll
    def"should invoke correct components and pass arguments, database operation #databaseOperation"()
    {
        given:
            def databaseOperationExecutor = Mock(DatabaseOperationExecutor)
            def sqlDefinitionFactoryFacade = Mock(SQLDefinitionFactoryFacade)
            def tested = new DatabaseOperationExecutorFacade(databaseOperationExecutor, sqlDefinitionFactoryFacade)
            DataSource dataSource = Mock(DataSource)
            Context context = Mock(Context)
            List<ISQLDefinition> sqlDefinitions = Mock(List)


        when:
            tested.execute(dataSource, context, databaseOperation)

        then:
            1 * sqlDefinitionFactoryFacade.build(context) >> sqlDefinitions
            1 * databaseOperationExecutor.execute(dataSource, sqlDefinitions, databaseOperation)

        where:
            databaseOperation   << [CREATE, LOG_ALL, DROP, VALIDATE]
    }

    @Unroll
    def"should rethrow exception [#exception] thrown by database operation processor #operation"()
    {
        given:
            def databaseOperationExecutor = Mock(DatabaseOperationExecutor)
            def sqlDefinitionFactoryFacade = Mock(SQLDefinitionFactoryFacade)
            def tested = new DatabaseOperationExecutorFacade(databaseOperationExecutor, sqlDefinitionFactoryFacade)
            DataSource dataSource = Mock(DataSource)
            Context context = Mock(Context)
            List<ISQLDefinition> sqlDefinitions = Mock(List)


        when:
            tested.execute(dataSource, context, operation)

        then:
            1 * sqlDefinitionFactoryFacade.build(context) >> sqlDefinitions
            1 * databaseOperationExecutor.execute(dataSource, sqlDefinitions, operation) >> { throw exception }
            def ex = thrown(exception.getClass())
            ex.is(exception)

        where:
            exception                                                                       | operation
            new SQLException()                                                              | CREATE
            new SQLException()                                                              | LOG_ALL
            new SQLException()                                                              | DROP
            new SQLException()                                                              | VALIDATE
            new ValidationDatabaseOperationsException(new HashMap<String, Set<String>>())   | CREATE
            new ValidationDatabaseOperationsException(new HashMap<String, Set<String>>())   | LOG_ALL
            new ValidationDatabaseOperationsException(new HashMap<String, Set<String>>())   | DROP
            new ValidationDatabaseOperationsException(new HashMap<String, Set<String>>())   | VALIDATE
    }
}
