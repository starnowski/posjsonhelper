package com.github.starnowski.posjsonhelper.poc.dao;

import com.github.starnowski.posjsonhelper.test.utils.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.sql.SQLException;

import static com.github.starnowski.posjsonhelper.poc.TestUtils.CLEAR_DATABASE_SCRIPT_PATH;
import static com.github.starnowski.posjsonhelper.poc.TestUtils.ITEMS_SCRIPT_PATH;
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
public class NativeSQLTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, ITEMS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    public void shouldReturnCorrectNumberOrResults() throws SQLException {
        // when
        Long result = TestUtils.selectAndReturnFirstRecordAsLong(null, "");

        // then
        Assertions.assertThat(result).isEqualTo(1);
    }
}
