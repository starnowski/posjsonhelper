package com.github.starnowski.posjsonhelper.core;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCallback;

public class TestCoreUtils {

    public static boolean isAnyRecordExists(JdbcTemplate jdbcTemplate, final String sql) {
        return jdbcTemplate.execute((StatementCallback<Boolean>) statement -> {
            return statement.executeQuery(sql).isBeforeFirst();
        });
    }

    public static boolean isFunctionExists(JdbcTemplate jdbcTemplate, String functionName, String schema) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT 1 FROM pg_proc pg, pg_catalog.pg_namespace pgn WHERE ");
        sb.append("pg.proname = '");
        sb.append(functionName);
        sb.append("' AND ");
        if (schema == null) {
            sb.append("pgn.nspname = 'public'");
        } else {
            sb.append("pgn.nspname = '");
            sb.append(schema);
            sb.append("'");
        }
        sb.append(" AND ");
        sb.append("pg.pronamespace =  pgn.oid");
        return isAnyRecordExists(jdbcTemplate, sb.toString());
    }
}
