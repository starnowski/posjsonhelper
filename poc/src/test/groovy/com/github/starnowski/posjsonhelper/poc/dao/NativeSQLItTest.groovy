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
    def "should return single correct id #expectedId when searching by all matching tags [#tags]" () {
        given:
            def pattern = "SELECT id FROM item WHERE jsonb_all_array_strings_exist(jsonb_extract_path(jsonb_content, 'top_element_with_set_of_values'), array[%s]) "
            def query = String.format(pattern, tags.stream().map({it -> return "'" + it + "'"}).collect(Collectors.joining(", ")))

        when:
            Long result = TestUtils.selectAndReturnFirstRecordAsLong(entityManager, query)

        then:
            result == expectedId

        where:
            tags                    ||  expectedId
            ['TAG1', 'TAG2']        ||  1
            ['TAG11']               ||  1
            ['TAG12']               ||  1
    }

    @Unroll
    @Sql(value = [CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH],
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    def "should return correct id #expectedIds when searching by all matching tags [#tags]" () {
        given:
            def pattern = "SELECT id FROM item WHERE jsonb_all_array_strings_exist(jsonb_extract_path(jsonb_content, 'top_element_with_set_of_values'), array[%s]) "
            def query = String.format(pattern, tags.stream().map({it -> return "'" + it + "'"}).collect(Collectors.joining(", ")))

        when:
            def result = TestUtils.selectAndReturnSetOfLongObjects(entityManager, query)

        then:
            result == expectedIds

        where:
            tags                            ||  expectedIds
            ['TAG1', 'TAG2']                ||  [1].toSet()
            ['TAG3']                        ||  [3, 2].toSet()
            ['TAG21', 'TAG22']              ||  [1, 4].toSet()
    }

    @Unroll
    @Sql(value = [CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH],
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    def "should return correct id #expectedIds when searching by any matching tags [#tags]" () {
            given:
            def pattern = "SELECT id FROM item WHERE jsonb_any_array_strings_exist(jsonb_extract_path(jsonb_content, 'top_element_with_set_of_values'), array[%s]) "
            def query = String.format(pattern, tags.stream().map({it -> return "'" + it + "'"}).collect(Collectors.joining(", ")))

        when:
            def result = TestUtils.selectAndReturnSetOfLongObjects(entityManager, query)

        then:
            result == expectedIds

        where:
            tags                            ||  expectedIds
            ['TAG1', 'TAG2']                ||  [1, 3].toSet()
            ['TAG3']                        ||  [3, 2].toSet()
            ['TAG1', 'TAG32']               ||  [1, 3, 5].toSet()
    }

    @Unroll
    @Sql(value = [CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH],
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    def "should return correct id #expectedIds when searching by #operator operator to compare integer value #value" () {
        given:
            def pattern = "SELECT id FROM item WHERE jsonb_extract_path_text(jsonb_content, 'integer_value')\\:\\:int %s %d"
            def query = String.format(pattern, operator, value)

        when:
            def result = TestUtils.selectAndReturnSetOfLongObjects(entityManager, query)

        then:
            result == expectedIds

        where:
            operator    |   value                           ||  expectedIds
            "="         |   562                             ||  [8].toSet()
            "="         |   731                             ||  [].toSet()
            "="         |   132                             ||  [7].toSet()
            ">="        |   562                             ||  [8, 9].toSet()
            ">="        |   2000                            ||  [].toSet()
            ">"         |   562                             ||  [9].toSet()
            "<="        |   562                             ||  [8, 7].toSet()
            "<"         |   562                             ||  [7].toSet()
            "<"         |   1322                            ||  [7, 8].toSet()
    }

    @Unroll
    @Sql(value = [CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH],
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    def "should return correct id #expectedIds when searching by #operator operator to compare double value #value" () {
        given:
            def pattern = "SELECT id FROM item WHERE cast(jsonb_extract_path_text(jsonb_content, 'double_value') AS numeric) %s %.2f"
            def query = String.format(Locale.US, pattern, operator, value)

        when:
            def result = TestUtils.selectAndReturnSetOfLongObjects(entityManager, query)

        then:
            result == expectedIds

        where:
            operator    |   value       ||  expectedIds
            "="         |   -1137.98    ||  [11].toSet()
            "="         |   353.01      ||  [10].toSet()
            ">="        |   -1137.98    ||  [10, 11, 12].toSet()
            ">"         |   -1137.98    ||  [10, 12].toSet()
            "<="        |   -1137.98    ||  [11].toSet()
            "<"         |   -1137.98    ||  [].toSet()
            "<"         |   20490.04    ||  [10, 11].toSet()
    }

    @Unroll
    @Sql(value = [CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH],
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    def "should return correct id #expectedIds when searching by IN operator to compare enum value #values" () {
        given:
            def pattern = "SELECT id FROM item WHERE jsonb_extract_path_text(jsonb_content, 'enum_value') IN (%s) "
            def query = String.format(pattern, values.stream().map({it -> return "'" + it + "'"}).collect(Collectors.joining(", ")))

        when:
            def result = TestUtils.selectAndReturnSetOfLongObjects(entityManager, query)

        then:
            result == expectedIds

        where:
            values                              ||  expectedIds
            ['SUPER', 'USER']                   ||  [14, 13].toSet()
            ['SUPER']                           ||  [13].toSet()
            ['ANONYMOUS', 'SUPER']              ||  [15, 13].toSet()
    }

    @Unroll
    @Sql(value = [CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH],
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    def "should return correct id #expectedIds when searching by LIKE operator with #expresion" () {
        given:
            def pattern = "SELECT id FROM item WHERE jsonb_extract_path_text(jsonb_content, 'string_value') LIKE '%s' "
            def query = String.format(pattern, expresion)

        when:
            def result = TestUtils.selectAndReturnSetOfLongObjects(entityManager, query)

        then:
            result == expectedIds

        where:
            expresion                           ||  expectedIds
            'this is full sentence'             ||  [16].toSet()
            'this is '                          ||  [].toSet()
            'this is %'                         ||  [16, 17].toSet()
            'end of'                            ||  [].toSet()
            'end of%'                           ||  [].toSet()
            '%end of%'                          ||  [18].toSet()
    }
}
