package com.github.starnowski.posjsonhelper.poc;

public class PostgreSQLDialectWithJsonbSupport extends  org.hibernate.dialect.PostgreSQL82Dialect{
    public PostgreSQLDialectWithJsonbSupport() {
        registerFunction("json_function_all_strings_at_top_level", new AllStringAtTopLevelJsonFunction());
        registerFunction("json_all", new AllStringAtTopLevelJsonFunction());
        registerFunction("json_function_get_json_element", new GetJsonElementSqlFunction());
        registerFunction("json_function_json_array", new JsonArrayFunction());
    }
}
