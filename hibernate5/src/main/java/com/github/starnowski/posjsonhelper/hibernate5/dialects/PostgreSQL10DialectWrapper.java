package com.github.starnowski.posjsonhelper.hibernate5.dialects;

import com.github.starnowski.posjsonhelper.hibernate5.PostgreSQLDialectEnricher;
import org.hibernate.dialect.PostgreSQL10Dialect;

public class PostgreSQL10DialectWrapper extends PostgreSQL10Dialect {

    private final PostgreSQLDialectEnricher enricher;

    public PostgreSQL10DialectWrapper() {
        this(new PostgreSQLDialectEnricher());
    }

    PostgreSQL10DialectWrapper(PostgreSQLDialectEnricher enricher) {
        this.enricher = enricher;
        this.enricher.enrich(this);
    }

    PostgreSQLDialectEnricher getEnricher() {
        return enricher;
    }
}
