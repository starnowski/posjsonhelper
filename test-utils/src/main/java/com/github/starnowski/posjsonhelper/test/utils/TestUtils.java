package com.github.starnowski.posjsonhelper.test.utils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.stream.Collectors;

public class TestUtils {

    public static boolean isAnyRecordExists(Statement statement, final String sql) throws SQLException {
        return statement.executeQuery(sql).isBeforeFirst();
    }

    public static String selectAndReturnFirstRecordAsString(Statement statement, final String sql) throws SQLException {
        ResultSet rs = statement.executeQuery(sql);
        rs.next();
        return rs.getString(1);
    }

    public static Long selectAndReturnFirstRecordAsLong(EntityManager entityManager, final String sql) throws SQLException {
        Query query = entityManager.createNativeQuery(sql);
        return ((BigInteger) query.getSingleResult()).longValue();
    }

    public static Set<Long> selectAndReturnSetOfLongObjects(EntityManager entityManager, final String sql) throws SQLException {
        Query query = entityManager.createNativeQuery(sql);
        return (Set<Long>) query.getResultList().stream().map(ob -> ((BigInteger)ob).longValue()).collect(Collectors.toSet());
    }

    public static boolean isFunctionExists(Statement statement, String functionName, String schema) throws SQLException {
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
        return isAnyRecordExists(statement, sb.toString());
    }
}
