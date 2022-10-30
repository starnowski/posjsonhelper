package com.github.starnowski.posjsonhelper.hibernate5.demo;

import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate5.PostgreSQLDialectEnricher;
import org.hibernate.dialect.PostgreSQL95Dialect;

public class PostgreSQLDialectWithDifferentSchema extends PostgreSQL95Dialect {

    public PostgreSQLDialectWithDifferentSchema() {
        PostgreSQLDialectEnricher enricher = new PostgreSQLDialectEnricher();
        enricher.enrich(this, Context.builder()
                .withSchema("non_public_schema")
                .withJsonbAnyArrayStringsExistFunctionReference("poshelper_json_array_any_string")
                .withJsonbAllArrayStringsExistFunctionReference("poshelper_json_array_all_string")
                .build(), HibernateContext.builder()
                .withJsonFunctionJsonArrayOperator("array_fun")
                .withJsonbAnyArrayStringsExistOperator("any_string_in_json")
                .withJsonbAllArrayStringsExistOperator("all_string_in_json").build());
    }
}
