package com.github.starnowski.posjsonhelper.hibernate6.demo.dao;

import com.github.starnowski.posjsonhelper.hibernate6.demo.model.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.starnowski.posjsonhelper.hibernate6.demo.Application.CLEAR_DATABASE_SCRIPT_PATH;
import static com.github.starnowski.posjsonhelper.hibernate6.demo.Application.ITEMS_SCRIPT_PATH;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
        config = @SqlConfig(transactionMode = ISOLATED),
        executionPhase = BEFORE_TEST_METHOD)
@Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
        config = @SqlConfig(transactionMode = ISOLATED),
        executionPhase = AFTER_TEST_METHOD)
public class ItemDaoTest {

    private static final Set<Long> ALL_ITEMS_IDS = new HashSet<>(asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L));

    private static Stream<Arguments> provideShouldReturnSingleCorrectIdExpectedIdWhenSearchingByAllMatchingTags() {
        return Stream.of(
                Arguments.of(asList("TAG1", "TAG2"), 1L),
                Arguments.of(List.of("TAG11"), 1L),
                Arguments.of(List.of("TAG12"), 1L)
        );
    }


    @Autowired
    private ItemDao tested;

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return single correct id #expectedId when searching by all matching tags [#tags]")
    @ParameterizedTest
    @MethodSource("provideShouldReturnSingleCorrectIdExpectedIdWhenSearchingByAllMatchingTags")
    public void shouldReturnSingleCorrectIdExpectedIdWhenSearchingByAllMatchingTags(List<String> tags, Long expectedId) {

        // when
        List<Item> results = tested.findAllByAllMatchingTags(new HashSet<>(tags));

        // then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getId()).isEqualTo(expectedId);
    }

    private static Stream<Arguments> provideShouldReturnCorrectIdExpectedIdsWhenSearchingByAllMatchingTags() {
        return Stream.of(
                Arguments.of(asList("TAG1", "TAG2"), new HashSet<>(Arrays.asList(1L))),
                Arguments.of(asList("TAG3"), new HashSet<>(Arrays.asList(3L, 2L))),
                Arguments.of(asList("TAG21", "TAG22"), new HashSet<>(Arrays.asList(1L, 4L)))
        );
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return correct id #expectedIds when searching by all matching tags [#tags]")
    @ParameterizedTest
    @MethodSource("provideShouldReturnCorrectIdExpectedIdsWhenSearchingByAllMatchingTags")
    public void shouldReturnCorrectIdExpectedIdsWhenSearchingByAllMatchingTags(List<String> tags, Set<Long> expectedIds) {

        // when
        List<Item> results = tested.findAllByAllMatchingTags(new HashSet<>(tags));

        // then
        assertThat(results).hasSize(expectedIds.size());
        assertThat(results.stream().map(r -> r.getId()).collect(Collectors.toSet())).isEqualTo(expectedIds);
    }

    private static Stream<Arguments> provideShouldReturnCorrectIdExceptExpectedIdsWhenSearchingItemThatDoNotMatchByAllMatchingTags() {
        return Stream.of(
                Arguments.of(asList("TAG1", "TAG2"), new HashSet<>(Arrays.asList(1L))),
                Arguments.of(asList("TAG3"), new HashSet<>(Arrays.asList(3L, 2L))),
                Arguments.of(asList("TAG21", "TAG22"), new HashSet<>(Arrays.asList(1L, 4L)))
        );
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return correct id except #expectedIds when searching item that do not match by all matching tags [#tags]")
    @ParameterizedTest
    @MethodSource("provideShouldReturnCorrectIdExceptExpectedIdsWhenSearchingItemThatDoNotMatchByAllMatchingTags")
    public void shouldReturnCorrectIdExceptExpectedIdsWhenSearchingItemThatDoNotMatchByAllMatchingTags(List<String> tags, Set<Long> nonIncludedIds) {

        // when
        List<Item> results = tested.findAllThatDoNotMatchByAllMatchingTags(new HashSet<>(tags));

        // then
        Set<Long> ids = results.stream().map(it -> it.getId()).collect(Collectors.toSet());
        Set<Long> expectedIds = ALL_ITEMS_IDS.stream().filter(id -> !nonIncludedIds.contains(id)).collect(Collectors.toSet());
        assertThat(ids).hasSize(expectedIds.size());
        assertThat(ids).isEqualTo(expectedIds);
    }

    private static Stream<Arguments> provideShouldReturnCorrectIdExpectedIdsWhenSearchingByAnyMatchingTags() {
        return Stream.of(
                Arguments.of(asList("TAG1", "TAG2"), new HashSet<>(Arrays.asList(1L, 3L))),
                Arguments.of(asList("TAG3"), new HashSet<>(Arrays.asList(3L, 2L))),
                Arguments.of(asList("TAG1", "TAG32"), new HashSet<>(Arrays.asList(1L, 3L, 5L)))
        );
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return correct id #expectedIds when searching by any matching tags [#tags]")
    @ParameterizedTest
    @MethodSource("provideShouldReturnCorrectIdExpectedIdsWhenSearchingByAnyMatchingTags")
    public void shouldReturnCorrectIdExpectedIdsWhenSearchingByAnyMatchingTags(List<String> tags, Set<Long> expectedIds) {

        // when
        List<Item> results = tested.findAllByAnyMatchingTags(new HashSet<>(tags));

        // then
        Set<Long> ids = results.stream().map(it -> it.getId()).collect(Collectors.toSet());
        assertThat(ids).hasSize(expectedIds.size());
        assertThat(ids).isEqualTo(expectedIds);
    }


//    @Unroll
//    @Sql(value = [CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH],
//            config = @SqlConfig(transactionMode = ISOLATED),
//            executionPhase = BEFORE_TEST_METHOD)
//    def "should return correct id #expectedIds when searching by #operator operator to compare double value #value" () {
//        when:
//        def results = tested.findAllByNumericValue(value, operator)
//        //https://www.cinqict.nl/blog/how-to-run-spock-with-the-newest-java-versions-without
//        //https://stackoverflow.com/questions/63265708/how-to-configure-spock-in-spring-boot-with-gradle-6-and-java-11
//        //https://spockframework.org/spock/docs/1.2/release_notes.html
//
//        then:
//        results.stream().map({it.getId()}).collect(Collectors.toSet()) == expectedIds
//
//        where:
//        operator    |   value       ||  expectedIds
//        EQ          |   -1137.98    ||  [11].toSet()
//        EQ          |   353.01      ||  [10].toSet()
//        GE          |   -1137.98    ||  [10, 11, 12].toSet()
//        GT          |   -1137.98    ||  [10, 12].toSet()
//        LE          |   -1137.98    ||  [11].toSet()
//        LT          |   -1137.98    ||  [].toSet()
//        LT          |   20490.04    ||  [10, 11].toSet()
//    }

    private static Stream<Arguments> provideShouldReturnCorrectIdExpectedIdsWhenSearchingByInOperatorToCompareEnumValue() {
        return Stream.of(
                Arguments.of(asList("SUPER", "USER"), new HashSet<>(Arrays.asList(14L, 13L))),
                Arguments.of(asList("SUPER"), new HashSet<>(Arrays.asList(13L))),
                Arguments.of(asList("ANONYMOUS", "SUPER"), new HashSet<>(Arrays.asList(13L, 15L)))
        );
    }

    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    @DisplayName("should return correct id #expectedIds when searching by IN operator to compare enum value #values")
    @ParameterizedTest
    @MethodSource("provideShouldReturnCorrectIdExpectedIdsWhenSearchingByInOperatorToCompareEnumValue")
    public void shouldReturnCorrectIdExpectedIdsWhenSearchingByInOperatorToCompareEnumValue(List<String> values, Set<Long> expectedIds) {

        // when
        List<Item> results = tested.findAllByStringThatMatchInValues(values);

        // then
        Set<Long> ids = results.stream().map(it -> it.getId()).collect(Collectors.toSet());
        assertThat(ids).hasSize(expectedIds.size());
        assertThat(ids).isEqualTo(expectedIds);
    }

    //    @Unroll
//    @Sql(value = [CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH],
//            config = @SqlConfig(transactionMode = ISOLATED),
//            executionPhase = BEFORE_TEST_METHOD)
//    def "should return correct id #expectedIds when searching by LIKE operator with #expresion" () {
//        when:
//        def results = tested.findAllByStringValueAndLikeOperator(expresion)
//
//        then:
//        results.stream().map({it.getId()}).collect(Collectors.toSet()) == expectedIds
//
//        where:
//        expresion                           ||  expectedIds
//        'this is full sentence'             ||  [16].toSet()
//        'this is '                          ||  [].toSet()
//        'this is %'                         ||  [16, 17].toSet()
//        'end of'                            ||  [].toSet()
//        'end of%'                           ||  [].toSet()
//        '%end of%'                          ||  [18].toSet()
//    }
}