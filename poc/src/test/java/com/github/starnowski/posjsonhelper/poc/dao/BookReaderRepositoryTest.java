package com.github.starnowski.posjsonhelper.poc.dao;

import com.github.starnowski.posjsonhelper.poc.model.BookReader;
import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.starnowski.posjsonhelper.poc.TestUtils.BOOKS_READERS_WITH_MULTI_AUTHORS_SCRIPT_PATH;
import static com.github.starnowski.posjsonhelper.poc.TestUtils.BOOKS_READERS_WITH_PLACES_OF_BIRTH_SCRIPT_PATH;
import static com.github.starnowski.posjsonhelper.poc.TestUtils.CLEAR_DATABASE_SCRIPT_PATH;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
        config = @SqlConfig(transactionMode = ISOLATED),
        executionPhase = BEFORE_TEST_METHOD)
@Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
        config = @SqlConfig(transactionMode = ISOLATED),
        executionPhase = AFTER_TEST_METHOD)
public class BookReaderRepositoryTest {

    @Autowired
    private BookReaderRepository tested;

    @Test
    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, BOOKS_READERS_WITH_MULTI_AUTHORS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    public void shouldReturnCorrectBookReadersByFavouriteAuthors()
    {
        // when
        List<BookReader> results = tested.listBookReadersByFavouriteAuthors("William Shakespeare", "Stephen King");

        // then
        Assertions.assertThat(results).isNotEmpty().hasSize(2);
        Assertions.assertThat(results.stream().map(BookReader::getId).collect(Collectors.toList())).containsExactly(2l, 3l);
    }

    @Test
    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, BOOKS_READERS_WITH_PLACES_OF_BIRTH_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    public void shouldReturnCorrectBookReadersByPlacesOfBirth()
    {
        // when
        List<BookReader> results = tested.listBookReadersByPlacesOfBirth("Warsaw", "New York");

        // then
        Assertions.assertThat(results).isNotEmpty().hasSize(2);
        Assertions.assertThat(results.stream().map(BookReader::getId).collect(Collectors.toList())).containsExactly(1l, 3l);
    }

    @Ignore
    @Test
    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, BOOKS_READERS_WITH_MULTI_AUTHORS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    public void shouldReturnCorrectResultsByNativeQuery()
    {
        // when
        List<BookReader> results = tested.returnBookForAuthorsByNativeQuery("William Shakespeare", "Stephen King");

        // then
        Assertions.assertThat(results).isNotEmpty().hasSize(2);
        Assertions.assertThat(results.stream().map(BookReader::getId).collect(Collectors.toList())).containsExactly(2l, 3l);
    }
}