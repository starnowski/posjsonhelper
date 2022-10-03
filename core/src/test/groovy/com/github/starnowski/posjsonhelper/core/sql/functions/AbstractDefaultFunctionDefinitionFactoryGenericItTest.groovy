package com.github.starnowski.posjsonhelper.core.sql.functions

import com.github.starnowski.posjsonhelper.core.TestApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posjsonhelper.core.TestCoreUtils.isFunctionExists
import static org.junit.Assert.assertEquals

@SpringBootTest(classes = [TestApplication.class])
abstract class AbstractDefaultFunctionDefinitionFactoryGenericItTest <T extends AbstractDefaultFunctionDefinitionFactory, P extends IFunctionFactoryParameters> extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate

    @Unroll
    def "test 1: should create function with name #name for schema #schema"()
    {
        given:
            assertEquals(false, isFunctionExists(jdbcTemplate, name, schema))
            P parameters = returnCorrectParametersSpyObject()
            parameters.getSchema() >> schema
            parameters.getFunctionName() >> name
            T tested = returnTestedObject()
            def functionDefinition = tested.produce(parameters)

        when:
            jdbcTemplate.execute(functionDefinition.getCreateScript())

        then:
            isFunctionExists(jdbcTemplate, name, schema)

        where:
            schema                  |   name
            null                    |   "jsonb_all_array_strings_exist"
            "public"                |   "json_fun_exist"
            "non_public_schema"     |   "some_fun"

    }

    @Unroll
    def "test 2: should delete function with name #name for schema #schema"()
    {
        given:
            assertEquals(true, isFunctionExists(jdbcTemplate, name, schema))
            P parameters = returnCorrectParametersSpyObject()
            parameters.getSchema() >> schema
            parameters.getFunctionName() >> name
            T tested = returnTestedObject()
            def functionDefinition = tested.produce(parameters)

        when:
            jdbcTemplate.execute(functionDefinition.getDropScript())

        then:
            !isFunctionExists(jdbcTemplate, name, schema)

        where:
            schema                  |   name
            null                    |   "jsonb_all_array_strings_exist"
            "public"                |   "json_fun_exist"
            "non_public_schema"     |   "some_fun"

    }

    protected abstract T returnTestedObject()

    protected abstract P returnCorrectParametersSpyObject();
}
