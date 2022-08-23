package com.github.starnowski.posjsonhelper.poc.dao

import com.github.starnowski.posjsonhelper.test.utils.TestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import spock.lang.Unroll

import javax.persistence.EntityManager
import java.util.stream.Collectors

import static com.github.starnowski.posjsonhelper.poc.TestUtils.CLEAR_DATABASE_SCRIPT_PATH
import static com.github.starnowski.posjsonhelper.poc.TestUtils.ITEMS_SCRIPT_PATH
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
class NativeSQLItTest extends spock.lang.Specification {

    @Autowired
    EntityManager entityManager

    @Unroll
    @Sql(value = [CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH],
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    def "should return single correct when searching by tags" () {
        given:
            def pattern = "SELECT id FROM item WHERE jsonb_all_array_strings_exist(jsonb_extract_path(jsonb_content, 'top_element_with_set_of_values'), array[%s]) "
            def query = String.format(pattern, tags.stream().map({it -> return "'" + it + "'"}).collect(Collectors.joining(", ")))

        when:
            Long result = TestUtils.selectAndReturnFirstRecordAsLong(entityManager, query);

        then:
            result == expectedId

        where:
            tags    ||  expectedId
            ['TAG1', 'TAG2']      ||  1
            ['TAG11']      ||  1
            ['TAG12']      ||  1
    }
}
