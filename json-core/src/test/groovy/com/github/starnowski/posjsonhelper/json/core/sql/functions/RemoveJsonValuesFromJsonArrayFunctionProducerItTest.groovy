package com.github.starnowski.posjsonhelper.json.core.sql.functions

import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinition
import com.github.starnowski.posjsonhelper.core.sql.functions.DefaultFunctionFactoryParameters
import com.github.starnowski.posjsonhelper.json.core.TestApplication
import com.github.starnowski.posjsonhelper.test.utils.TestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import spock.lang.Specification
import spock.lang.Unroll

import java.util.stream.Collectors

import static com.github.starnowski.posjsonhelper.json.core.TestApplication.CLEAR_DATABASE_SCRIPT_PATH
import static com.github.starnowski.posjsonhelper.json.core.TestApplication.CLEAR_DATABASE_SCRIPT_PATH
import static com.github.starnowski.posjsonhelper.json.core.TestApplication.ITEMS_SCRIPT_PATH
import static com.github.starnowski.posjsonhelper.test.utils.TestUtils.functionReference
import static com.github.starnowski.posjsonhelper.test.utils.TestUtils.isFunctionExists
import static com.github.starnowski.posjsonhelper.test.utils.TestUtils.selectAndReturnFirstRecordAsString
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertEquals
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED

@SpringBootTest(classes = [TestApplication.class])
@SqlGroup([
        @Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
                config = @SqlConfig(transactionMode = ISOLATED),
                executionPhase = BEFORE_TEST_METHOD),
        @Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
                config = @SqlConfig(transactionMode = ISOLATED),
                executionPhase = AFTER_TEST_METHOD)
])
class RemoveJsonValuesFromJsonArrayFunctionProducerItTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate

    def tested = new RemoveJsonValuesFromJsonArrayFunctionProducer()

    ISQLDefinition definition

    String schema
    String name

    @Unroll
    @Sql(value = [CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH],
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    def "should create function with name #functionName, schema '#functionSchema' and return expected values (#expectedJsonArray) for json input #inputJson and values to remove #valuesToRemove" () {
        given:
            schema = functionSchema
            name = functionName
            definition = tested.produce(new DefaultFunctionFactoryParameters(name, schema))
            jdbcTemplate.execute(definition.getCreateScript())
            assertEquals(true, isFunctionExists(jdbcTemplate, functionName, schema))

        when:
            def result = selectAndReturnFirstRecordAsString(jdbcTemplate, selectStatement(functionReference(name, schema), inputJson, valuesToRemove))

        then:
            result == expectedJsonArray

        where:
            functionName                        |   functionSchema      |   inputJson                               |       valuesToRemove  ||  expectedJsonArray
            "remove_json_values"                |  null                 |   '["a", "b", "c", "d", "e"]'             |       '["b", "d"]'    ||  '["a", "c", "e"]'
            "remove_json_values"                |  "public"             |   '["a", "b", "c", "d", "e"]'             |       '["b", "d"]'    ||  '["a", "c", "e"]'
            "remove_j_vals_____"                |  "public"             |   '["a", "b", "c", "1", "1"]'             |       '["b", "d"]'    ||  '["a", "c", "1", "1"]'
            "remove_json_array_values"          |  "non_public_schema"  |   '["a", "b", "c", "1", "1"]'             |       '["b", "d"]'    ||  '["a", "c", "1", "1"]'
            "delete_json_array"                 |  "non_public_schema"  |   '["a", "b", "c", "1", "1"]'             |       '["a"]'         ||  '["b", "c", "1", "1"]'
            "delete_json_array"                 |  "non_public_schema"  |   '["a", "b", "c", "d", "e"]'             |       '[]'            ||  '["a", "b", "c", "d", "e"]'
    }

    def selectStatement(String functionReference, String inputJson, String valuesToRemove) {
        def pattern = "SELECT %s('%s'::jsonb, '%s'::jsonb)"
        String.format(pattern, functionReference, inputJson, valuesToRemove)
    }

    def cleanup() {
        jdbcTemplate.execute(definition.dropScript)
        assertEquals(false, isFunctionExists(jdbcTemplate, name, schema))
    }
}
