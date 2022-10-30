package com.github.starnowski.posjsonhelper.hibernate5.demo.dao

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posjsonhelper.hibernate5.demo.Application.CLEAR_DATABASE_SCRIPT_PATH
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlGroup([
        @Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
                config = @SqlConfig(transactionMode = ISOLATED),
                executionPhase = BEFORE_TEST_METHOD),
        @Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
                config = @SqlConfig(transactionMode = ISOLATED),
                executionPhase = AFTER_TEST_METHOD)
])
class SQLInjectionItTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate

    @Unroll
    @Transactional(readOnly = true)
    def "should set configuration property #property with expected value #value"(){
        given:
            jdbcTemplate.execute(String.format("SELECT set_config('%s', '%s', false);", property, value))

        when:
            def result = jdbcTemplate.queryForObject(String.format("SELECT current_setting('%s')", property), String)

        then:
            result == value

        where:
            property    |   value
            "c.prop1"   |   "some value"
    }
}
