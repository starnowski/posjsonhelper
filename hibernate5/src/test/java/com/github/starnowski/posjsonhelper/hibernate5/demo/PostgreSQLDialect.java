package com.github.starnowski.posjsonhelper.hibernate5.demo;

import com.github.starnowski.posjsonhelper.hibernate5.PostgreSQLDialectEnricher;
import org.hibernate.dialect.PostgreSQL82Dialect;

public class PostgreSQLDialect extends PostgreSQL82Dialect {

    public PostgreSQLDialect(){
        PostgreSQLDialectEnricher enricher = new PostgreSQLDialectEnricher();
        enricher.enrich(this);
    }
}
