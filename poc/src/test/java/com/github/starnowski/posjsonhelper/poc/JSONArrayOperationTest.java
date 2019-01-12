package com.github.starnowski.posjsonhelper.poc;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.github.starnowski.posjsonhelper.poc.TestUtils.CLEAR_DATABASE_SCRIPT_PATH;
import static com.github.starnowski.posjsonhelper.poc.TestUtils.EMPLOYEES_WITH_PAYMENTS_SCRIPT_PATH;
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
public class JSONArrayOperationTest {

    private static final int HIGH_SALARY = 5000;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, EMPLOYEES_WITH_PAYMENTS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    public void shouldReturnCorrectIdsOfEmployeesWithHighSalaryInDecember()
    {
        //TODO
        // when
        List<Long> results = TestUtils.selectIds(jdbcTemplate, "SELECT id FROM employee e WHERE e.payment_jsonb_data-> ");

        // then
        Assertions.assertThat(results).isNotEmpty().hasSize(1);
        Assertions.assertThat(results).containsExactly(2L);
    }

    @Test
    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, EMPLOYEES_WITH_PAYMENTS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    public void shouldReturnEmptyIdsListWhenQueryByNonExistJsonAttribute() {
        // when
        List<Long> results = TestUtils.selectIds(jdbcTemplate, "SELECT id FROM employee e WHERE jsonb_top_level_exist(e.payment_jsonb_data, 'custom11')");

        // then
        Assertions.assertThat(results).isEmpty();
    }

    @Test
    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, EMPLOYEES_WITH_PAYMENTS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    public void shouldReturnEmptyIdsListWhenQueryByNonExistJsonAttributeAndUsingNumericComparator() {
        // when
        List<Long> results = TestUtils.selectIds(jdbcTemplate, "SELECT id FROM employee e WHERE jsonb_top_level_exist(e.payment_jsonb_data, 'custom11') AND (e.payment_jsonb_data->>'custom11')::integer > 0");

        // then
        Assertions.assertThat(results).isEmpty();
    }

    @Test
    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, EMPLOYEES_WITH_PAYMENTS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    public void shouldReturnEmptyIdsListWhenQueryByNonExistJsonAttributeAndUsingNumericComparatorWithoutCheckingIfAttributeExist() {
        // when
        List<Long> results = TestUtils.selectIds(jdbcTemplate, "SELECT id FROM employee e WHERE (e.payment_jsonb_data->>'custom11')::integer > 0");

        // then
        Assertions.assertThat(results).isEmpty();
    }

    @Test
    @Sql(value = {CLEAR_DATABASE_SCRIPT_PATH, EMPLOYEES_WITH_PAYMENTS_SCRIPT_PATH},
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD)
    public void shouldReturnIdsForRecoudWhichContainsCustom33Attribute() {

        // when
        List<Long> results = TestUtils.selectIds(jdbcTemplate, "SELECT id FROM employee e WHERE jsonb_top_level_exist(e.payment_jsonb_data, 'custom33')");

        // then
        Assertions.assertThat(results).isNotEmpty().hasSize(1);
        Assertions.assertThat(results).containsExactly(4L);
    }
}
