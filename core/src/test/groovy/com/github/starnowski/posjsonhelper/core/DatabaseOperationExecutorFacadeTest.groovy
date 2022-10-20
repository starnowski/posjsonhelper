package com.github.starnowski.posjsonhelper.core


import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinition
import com.github.starnowski.posjsonhelper.core.sql.SQLDefinitionFactoryFacade
import spock.lang.Specification
import spock.lang.Unroll

import javax.sql.DataSource

import static com.github.starnowski.posjsonhelper.core.DatabaseOperationType.CREATE
import static com.github.starnowski.posjsonhelper.core.DatabaseOperationType.DROP
import static com.github.starnowski.posjsonhelper.core.DatabaseOperationType.LOG_ALL
import static com.github.starnowski.posjsonhelper.core.DatabaseOperationType.VALIDATE

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
}
