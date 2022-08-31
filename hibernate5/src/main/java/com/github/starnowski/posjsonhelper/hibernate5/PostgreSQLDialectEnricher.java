package com.github.starnowski.posjsonhelper.hibernate5;

import com.github.starnowski.posjsonhelper.hibernate5.functions.JsonArrayFunction;
import org.hibernate.dialect.PostgreSQL81Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class PostgreSQLDialectEnricher {

    public void enrich(PostgreSQL81Dialect postgreSQL81Dialect) {
        postgreSQL81Dialect.getFunctions().put("json_function_json_array", new JsonArrayFunction());
        postgreSQL81Dialect.getFunctions().put("jsonb_all_array_strings_exist", new StandardSQLFunction("jsonb_all_array_strings_exist", StandardBasicTypes.BOOLEAN));
        postgreSQL81Dialect.getFunctions().put("jsonb_any_array_strings_exist", new StandardSQLFunction("jsonb_any_array_strings_exist", StandardBasicTypes.BOOLEAN));
    }
}
