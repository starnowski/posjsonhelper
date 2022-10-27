package com.github.starnowski.posjsonhelper.hibernate5.dialects;

import com.github.starnowski.posjsonhelper.hibernate5.PostgreSQLDialectEnricher;
import org.hibernate.dialect.PostgreSQL95Dialect;

public class PostgreSQL95DialectWrapper extends PostgreSQL95Dialect {

    private final PostgreSQLDialectEnricher enricher;

    public PostgreSQL95DialectWrapper() {
        this(new PostgreSQLDialectEnricher());
    }

    PostgreSQL95DialectWrapper(PostgreSQLDialectEnricher enricher) {
        this.enricher = enricher;
        this.enricher.enrich(this);
    }

    PostgreSQLDialectEnricher getEnricher() {
        return enricher;
    }
}
