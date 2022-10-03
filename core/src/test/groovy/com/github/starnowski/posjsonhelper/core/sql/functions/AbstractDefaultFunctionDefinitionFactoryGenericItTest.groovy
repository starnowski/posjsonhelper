package com.github.starnowski.posjsonhelper.core.sql.functions

import com.github.starnowski.posjsonhelper.core.TestApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification
import spock.lang.Unroll

import javax.sql.DataSource

import static com.github.starnowski.posjsonhelper.test.utils.TestUtils.isFunctionExists
import static org.junit.Assert.assertEquals

@SpringBootTest(classes = [TestApplication.class])
abstract class AbstractDefaultFunctionDefinitionFactoryGenericItTest <T extends AbstractDefaultFunctionDefinitionFactory, P extends IFunctionFactoryParameters> extends Specification {

    @Autowired
    DataSource dataSource

    @Autowired
    JdbcTemplate jdbcTemplate

    @Unroll
    def "test 1: should create function with name #name for schema #schema"()
    {
        given:
            assertEquals(false, isFunctionExists(dataSource.getConnection().createStatement(), name, schema))
            P parameters = returnCorrectParametersSpyObject()
            parameters.getSchema() >> schema
            parameters.getFunctionName() >> name
            T tested = returnTestedObject()
            def functionDefinition = tested.produce(parameters)

        when:
            jdbcTemplate.execute(functionDefinition.getCreateScript())

        then:
            isFunctionExists(dataSource.getConnection().createStatement(), name, schema)

        where:
            schema                  |   name
            null                    |   "jsonb_all_array_strings_exist"
            "public"                |   "json_fun_exist"
            "non_public_schema"     |   "someFun"

    }

    protected abstract T returnTestedObject()

    protected abstract P returnCorrectParametersSpyObject();
}
