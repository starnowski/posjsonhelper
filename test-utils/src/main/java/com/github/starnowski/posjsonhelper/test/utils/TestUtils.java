package com.github.starnowski.posjsonhelper.test.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestUtils {

    public static boolean isAnyRecordExists(Statement statement, final String sql) throws SQLException {
        return statement.executeQuery(sql).isBeforeFirst();
    }

    public static String selectAndReturnFirstRecordAsString(Statement statement, final String sql) throws SQLException {
        ResultSet rs = statement.executeQuery(sql);
        rs.next();
        return rs.getString(1);
    }

    public static Long selectAndReturnFirstRecordAsLong(Statement statement, final String sql) throws SQLException {
        ResultSet rs = statement.executeQuery(sql);
        rs.next();
        return rs.getLong(1);
    }
}
