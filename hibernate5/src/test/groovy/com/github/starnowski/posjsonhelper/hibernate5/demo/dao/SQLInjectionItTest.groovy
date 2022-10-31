package com.github.starnowski.posjsonhelper.hibernate5.demo.dao

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posjsonhelper.hibernate5.demo.Application.CLEAR_DATABASE_SCRIPT_PATH
import static com.github.starnowski.posjsonhelper.hibernate5.demo.Application.ITEMS_SCRIPT_PATH
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlGroup([
        @Sql(value = [CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH],
                config = @SqlConfig(transactionMode = ISOLATED),
                executionPhase = BEFORE_TEST_METHOD),
        @Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
                config = @SqlConfig(transactionMode = ISOLATED),
                executionPhase = AFTER_TEST_METHOD)
])
class SQLInjectionItTest extends Specification {

    private static final String JSON_ALL_STATEMENT_PATTERN = "SELECT id FROM item WHERE jsonb_all_array_strings_exist(jsonb_extract_path(jsonb_content, %s), array[%s]) "

    @Autowired
    JdbcTemplate jdbcTemplate

    @Unroll
    def "should not modify current configuration property #property with expected value #value when executing correct statement"(){
        given:
            jdbcTemplate.execute(String.format("SELECT set_config('%s', '%s', false);", property, value))

        when:
            def result = jdbcTemplate.queryForObject(String.format(JSON_ALL_STATEMENT_PATTERN, "'top_element_with_set_of_values'", "'TAG1', 'TAG2'"), Integer)

        then:
            def currentValue = jdbcTemplate.queryForObject(String.format("SELECT current_setting('%s')", property), String)
            currentValue == value

        and: "result should match"
            result == 1

        where:
            property    |   value
            "c.prop1"   |   "some value"
            "prop.value"   |   "this is a test"
    }

    @Unroll
    def "should modify current configuration property #property and change current value #value with expected #expected when using statements jsonPath #jsonPath, array elements #array"(){
        given:
            jdbcTemplate.execute(String.format("SELECT set_config('%s', '%s', false);", property, value))
            def query = String.format(JSON_ALL_STATEMENT_PATTERN, jsonPath, array)
            System.out.println("Testing query : " + query)

        when:
            jdbcTemplate.execute(String.format(JSON_ALL_STATEMENT_PATTERN, jsonPath, array))

        then:
            def currentValue = jdbcTemplate.queryForObject(String.format("SELECT current_setting('%s')", property), String)
            currentValue == expected

        // https://portswigger.net/web-security/sql-injection
        where:
        property    |   value   | jsonPath  |   array   ||   expected
        "c.prop1"   |   "some value"    |   "'top_element_with_set_of_values'), array['X1']); SELECT set_config('c.prop1', 'SECURITY FAILED', false)--"  |   "'TAG1', 'TAG2'"  ||  "SECURITY FAILED"
        "conf.value"   |   "some value"    |   "'some_element'), array['sss']); SELECT set_config('conf.value', 'New value', false)--"  |   "'Val1'"  ||  "New value"
        "prop.value"   |   "this is a test" |   "'top_element_with_set_of_values'"  |   "'some val']); SELECT set_config('prop.value', 'WARNING', false);--"  || "WARNING"
    }
}
