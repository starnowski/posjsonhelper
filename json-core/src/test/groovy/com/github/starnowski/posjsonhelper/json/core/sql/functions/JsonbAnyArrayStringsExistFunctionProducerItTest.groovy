package com.github.starnowski.posjsonhelper.json.core.sql.functions

import com.github.starnowski.posjsonhelper.core.TestApplication
import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinition
import com.github.starnowski.posjsonhelper.core.sql.functions.DefaultFunctionFactoryParameters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import spock.lang.Specification
import spock.lang.Unroll

import java.util.stream.Collectors

import static com.github.starnowski.posjsonhelper.core.TestApplication.CLEAR_DATABASE_SCRIPT_PATH
import static com.github.starnowski.posjsonhelper.core.TestApplication.ITEMS_SCRIPT_PATH
import static com.github.starnowski.posjsonhelper.core.TestCoreUtils.*
import static org.junit.Assert.assertEquals
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD
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
class JsonbAnyArrayStringsExistFunctionProducerItTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate

    def tested = new JsonbAnyArrayStringsExistFunctionProducer()

    ISQLDefinition definition

    String schema
    String name

    @Unroll
    @Sql(value = [CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH],
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    def "should create function with name #functionName, schema '#functionSchema' and return expected values (#expectedIds)" () {
        given:
            schema = functionSchema
            name = functionName
            definition = tested.produce(new DefaultFunctionFactoryParameters(name, schema))
            jdbcTemplate.execute(definition.getCreateScript())
            assertEquals(true, isFunctionExists(jdbcTemplate, functionName, schema))

        when:
            def results =  selectAndReturnSetOfLongObjects(jdbcTemplate, selectStatement(functionReference(name, schema), tags))

        then:
            results == expectedIds

        where:
            functionName                        |   functionSchema      |   tags                            ||  expectedIds
            "jsonb_all_array_strings_exist"     |  null                 |   ['TAG1', 'TAG2']                ||  [1, 3].toSet()
            "jsonb_all_array_strings_exist"     |  "public"             |   ['TAG3']                        ||  [3, 2].toSet()
            "fun_22"                            |  "public"             |   ['TAG1', 'TAG32']               ||  [1, 3, 5].toSet()
            "some_fun"                          |  "non_public_schema"  |   ['TAG1', 'TAG2']                ||  [1, 3].toSet()
    }

    def selectStatement(String functionReference, List<String> tags) {
        def pattern = "SELECT id FROM item WHERE %s(jsonb_extract_path(jsonb_content, 'top_element_with_set_of_values'), array[%s]) "
        String.format(pattern, functionReference, tags.stream().map({it -> return "'" + it + "'"}).collect(Collectors.joining(", ")))
    }

    def cleanup() {
        jdbcTemplate.execute(definition.dropScript)
        assertEquals(false, isFunctionExists(jdbcTemplate, name, schema))
    }
}
