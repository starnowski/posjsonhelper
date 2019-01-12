package com.github.starnowski.posjsonhelper.poc;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCallback;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {
    public static final String CLEAR_DATABASE_SCRIPT_PATH = "/com/github/starnowski/posjsonhelper/poc/clean-database.sql";
    public static final String BOOKS_READERS_WITH_MULTI_AUTHORS_SCRIPT_PATH = "/com/github/starnowski/posjsonhelper/poc/books-with-multi-authors.sql";
    public static final String BOOKS_READERS_WITH_PLACES_OF_BIRTH_SCRIPT_PATH = "/com/github/starnowski/posjsonhelper/poc/book-readers-with-places-of-birth.sql";
    public static final String EMPLOYEES_WITH_PAYMENTS_SCRIPT_PATH = "employees-with-payments.sql";

    public static int count(JdbcTemplate jdbcTemplate, String query)
    {
        return jdbcTemplate.execute(new StatementCallback<Integer>() {
            @Override
            public Integer doInStatement(Statement statement) throws SQLException, DataAccessException {
                ResultSet rs = statement.executeQuery(query);rs.next();
                return rs.getInt(1);
            }
        });
    }

    public static List<Long> selectIds(JdbcTemplate jdbcTemplate, String query)
    {
        return jdbcTemplate.execute(new StatementCallback<List<Long>>() {
            @Override
            public List<Long> doInStatement(Statement statement) throws SQLException, DataAccessException {
                ResultSet rs = statement.executeQuery(query);rs.next();
                List<Long> result = new ArrayList<>();
                while (true)
                {
                    result.add(rs.getLong(1));
                    if (rs.isLast())
                    {
                        break;
                    }
                }
                return result;
            }
        });
    }

}
