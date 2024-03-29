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
/**
 * Set of tests that check if used components are vulnerable to SQL injection attack.
 * Test scenario:
 * At the beginning of the test, the session configuration property is set with a specific value.
 * Then test tries to pass injected SQL code that modifies the session configuration property with the new value.
 * In the end, tests check if the value was changed.
 * If the value was changed by injected code, then it means that the code is vulnerable to SQL injection attacks.
 * Information about SQL injection attack: https://portswigger.net/web-security/sql-injection
 */
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
    private static final String JSON_ANY_STATEMENT_PATTERN = "SELECT id FROM item WHERE jsonb_any_array_strings_exist(jsonb_extract_path(jsonb_content, %s), array[%s]) "
    private static final String SETTING_CONFIGURATION_PROPERTY_PATTERN = "SELECT set_config('%s', '%s', false);"
    private static final String GETTING_CONFIGURATION_PROPERTY_PATTERN = "SELECT current_setting('%s');"

    @Autowired
    JdbcTemplate jdbcTemplate
    @Autowired
    ItemDao tested

    @Unroll
    def "should not modify current configuration property #property with expected value #value when executing correct statement, sql pattern #sqlPattern, jsonPathInput #jsonPathInput, array input #arrayInput"(){
        given:
            jdbcTemplate.execute(String.format(SETTING_CONFIGURATION_PROPERTY_PATTERN, property, value))
            def query = String.format(sqlPattern, jsonPathInput, arrayInput)
            System.out.println("Testing query : " + query)

        when:
            def results = jdbcTemplate.queryForList(query, Integer)

        then:
            def currentValue = jdbcTemplate.queryForObject(String.format(GETTING_CONFIGURATION_PROPERTY_PATTERN, property), String)
            currentValue == value

        and: "result should match"
            new HashSet<>(results) == expectedQueryResults

        where:
            property    |   value           |   sqlPattern  |   jsonPathInput   |   arrayInput ||  expectedQueryResults
            "c.prop1"   |   "some value"        | JSON_ALL_STATEMENT_PATTERN | "'top_element_with_set_of_values'" | "'TAG1', 'TAG2'" ||  new HashSet<>(Arrays.asList(1))
            "prop.value"   |   "this is a test" | JSON_ALL_STATEMENT_PATTERN | "'top_element_with_set_of_values'" | "'TAG1', 'TAG2'" ||  new HashSet<>(Arrays.asList(1))
            "prop.value"   |   "this is a test" | JSON_ANY_STATEMENT_PATTERN | "'top_element_with_set_of_values'" | "'TAG1', 'TAG2'" ||  new HashSet<>(Arrays.asList(1, 3))
    }

    @Unroll
    def "should modify current configuration property #property and change current value #value with expected #expected when using statements jsonPath #jsonPath, array elements #array, sql statement #sqlPattern"(){
        given:
            jdbcTemplate.execute(String.format(SETTING_CONFIGURATION_PROPERTY_PATTERN, property, value))
            def query = String.format(JSON_ALL_STATEMENT_PATTERN, jsonPath, array)
            System.out.println("Testing query : " + query)

        when:
            jdbcTemplate.execute(String.format(sqlPattern, jsonPath, array))

        then:
            def currentValue = jdbcTemplate.queryForObject(String.format(GETTING_CONFIGURATION_PROPERTY_PATTERN, property), String)
            currentValue == expected

        // https://portswigger.net/web-security/sql-injection
        where:
            property    |   value   | sqlPattern | jsonPath  |   array   ||   expected
            "c.prop1"   |   "some value"    | JSON_ALL_STATEMENT_PATTERN |   "'top_element_with_set_of_values'), array['X1']); SELECT set_config('c.prop1', 'SECURITY FAILED', false)--"  |   "'TAG1', 'TAG2'"  ||  "SECURITY FAILED"
            "conf.value"   |   "some value"    | JSON_ALL_STATEMENT_PATTERN |   "'some_element'), array['sss']); SELECT set_config('conf.value', 'New value', false)--"  |   "'Val1'"  ||  "New value"
            "prop.value"   |   "this is a test" | JSON_ALL_STATEMENT_PATTERN |   "'top_element_with_set_of_values'"  |   "'some val']); SELECT set_config('prop.value', 'WARNING', false);--"  || "WARNING"
            "prop.value"   |   "this is a test" | JSON_ANY_STATEMENT_PATTERN |   "'top_element_with_set_of_values'"  |   "'some val']); SELECT set_config('prop.value', 'WARNING', false);--"  || "WARNING"
            "conf.value"   |   "some value"    | JSON_ANY_STATEMENT_PATTERN |   "'some_element'), array['sss']); SELECT set_config('conf.value', 'New value', false)--"  |   "'Val1'"  ||  "New value"
    }

    @Unroll
    def "should not modify current configuration property #property and change current value #value when using predicate JsonbAllArrayStringsExistPredicate with array elements #tags as passed arguments"(){
        given:
            jdbcTemplate.execute(String.format(SETTING_CONFIGURATION_PROPERTY_PATTERN, property, value))

        when:
            def results = tested.findAllByAllMatchingTags(new HashSet<String>(Arrays.asList(tags)))

        then:
            results.isEmpty()

        then:
        def currentValue = jdbcTemplate.queryForObject(String.format(GETTING_CONFIGURATION_PROPERTY_PATTERN, property), String)
        currentValue == value

        and: "result is empty"
            results.isEmpty()

        // https://portswigger.net/web-security/sql-injection
        where:
            property    |   value | tags
            "prop.value"   |   "this is a test" |  "'some val']); SELECT set_config('prop.value', 'WARNING', false);--"
    }

    @Unroll
    def "should not modify current configuration property #property and change current value #value when using predicate JsonbAnyArrayStringsExistPredicate with array elements #tags as passed arguments"(){
        given:
            jdbcTemplate.execute(String.format(SETTING_CONFIGURATION_PROPERTY_PATTERN, property, value))

        when:
            def results = tested.findAllByAnyMatchingTags(new HashSet<String>(Arrays.asList(tags)))

        then:
            results.isEmpty()

        then:
            def currentValue = jdbcTemplate.queryForObject(String.format(GETTING_CONFIGURATION_PROPERTY_PATTERN, property), String)
            currentValue == value

        and: "result is empty"
            results.isEmpty()

        // https://portswigger.net/web-security/sql-injection
        where:
            property    |   value | tags
            "prop.value"   |   "this is a test" |  "'some val']); SELECT set_config('prop.value', 'WARNING', false);--"
    }
}
