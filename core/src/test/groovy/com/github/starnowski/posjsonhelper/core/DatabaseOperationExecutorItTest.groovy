package com.github.starnowski.posjsonhelper.core

import com.github.starnowski.posjsonhelper.core.operations.exceptions.AbstractDatabaseOperationsException
import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinition
import com.github.starnowski.posjsonhelper.core.sql.SQLDefinitionFactoryFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Shared
import spock.lang.Specification

import javax.sql.DataSource

@SpringBootTest(classes = [TestApplication.class])
class DatabaseOperationExecutorItTest extends Specification {

    def tested = new DatabaseOperationExecutor()
    @Shared
    def context = Context.builder().build()
    @Autowired
    DataSource dataSource
    @Shared
    def sqlDefinitionFactoryFacade = new SQLDefinitionFactoryFacade()
    List<ISQLDefinition> definitions = sqlDefinitionFactoryFacade.build(context)

    def "test 1: check statements should failed"()
    {
        when:
            tested.execute(dataSource, definitions, DatabaseOperationType.VALIDATE)

        then:
            thrown(AbstractDatabaseOperationsException)
    }

    def "test 2: execution of creation scripts should be successful"()
    {
        when:
            tested.execute(dataSource, definitions, DatabaseOperationType.CREATE)

        then:
            noExceptionThrown()
    }

    def "test 3: check statements should pass"()
    {
        when:
            tested.execute(dataSource, definitions, DatabaseOperationType.VALIDATE)

        then:
            noExceptionThrown()
    }

    def "test 4: logging should pass"()
    {
        when:
            tested.execute(dataSource, definitions, DatabaseOperationType.LOG_ALL)

        then:
            noExceptionThrown()
    }

    def "test 5: drop scripts should pass"()
    {
        when:
            tested.execute(dataSource, definitions, DatabaseOperationType.DROP)

        then:
            noExceptionThrown()
    }

    def "test 6: check statements should failed"()
    {
        when:
            tested.execute(dataSource, definitions, DatabaseOperationType.VALIDATE)

        then:
            thrown(AbstractDatabaseOperationsException)
    }
}