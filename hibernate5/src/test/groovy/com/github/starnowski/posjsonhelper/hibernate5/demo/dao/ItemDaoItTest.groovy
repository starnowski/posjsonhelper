package com.github.starnowski.posjsonhelper.hibernate5.demo.dao

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import spock.lang.Specification
import spock.lang.Unroll

import java.util.stream.Collectors

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED
import static com.github.starnowski.posjsonhelper.hibernate5.demo.Application.CLEAR_DATABASE_SCRIPT_PATH
import static com.github.starnowski.posjsonhelper.hibernate5.demo.Application.ITEMS_SCRIPT_PATH

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlGroup([
        @Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
                config = @SqlConfig(transactionMode = ISOLATED),
                executionPhase = BEFORE_TEST_METHOD),
        @Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
                config = @SqlConfig(transactionMode = ISOLATED),
                executionPhase = AFTER_TEST_METHOD)
])
class ItemDaoItTest extends Specification {

    private static Set<Long> ALL_ITEMS_IDS = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L).toSet()

    @Autowired
    private ItemDao tested

    @Unroll
    @Sql(value = [CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH],
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    def "should return single correct id #expectedId when searching by all matching tags [#tags]" () {
        when:
            def results = tested.findAllByAllMatchingTags(new HashSet<String>(tags))

        then:
            results.stream().map({it.getId()}).collect(Collectors.toSet()) == new HashSet([ expectedId ])

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
        when:
            def results = tested.findAllByAllMatchingTags(new HashSet<String>(tags))

        then:
            results.stream().map({it.getId()}).collect(Collectors.toSet()) == expectedIds

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
    def "should return correct id except #expectedIds when searching item that do not match by all matching tags [#tags]" () {
        when:
            def results = tested.findAllThatDoNotMatchByAllMatchingTags(new HashSet<String>(tags))

        then:
            results.stream().map({it.getId()}).collect(Collectors.toSet()) == ALL_ITEMS_IDS.stream().filter({id -> !expectedIds.contains(id)}).collect(Collectors.toSet())

        where:
            tags                            ||  expectedIds
            ['TAG1', 'TAG2']                ||  [1L].toSet()
            ['TAG3']                        ||  [3L, 2L].toSet()
            ['TAG21', 'TAG22']              ||  [1L, 4L].toSet()
    }

    @Unroll
    @Sql(value = [CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH],
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    def "should return correct id #expectedIds when searching by any matching tags [#tags]" () {
        when:
            def results = tested.findAllByAnyMatchingTags(new HashSet<String>(tags))

        then:
            results.stream().map({it.getId()}).collect(Collectors.toSet()) == expectedIds

        where:
            tags                            ||  expectedIds
            ['TAG1', 'TAG2']                ||  [1, 3].toSet()
            ['TAG3']                        ||  [3, 2].toSet()
            ['TAG1', 'TAG32']               ||  [1, 3, 5].toSet()
    }
}
