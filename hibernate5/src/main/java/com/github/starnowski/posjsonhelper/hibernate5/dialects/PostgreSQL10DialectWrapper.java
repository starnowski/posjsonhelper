/**
 *     Posjsonhelper library is an open-source project that adds support of
 *     Hibernate query for https://www.postgresql.org/docs/10/functions-json.html)
 *
 *     Copyright (C) 2023  Szymon Tarnowski
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 */
package com.github.starnowski.posjsonhelper.hibernate5.dialects;

import com.github.starnowski.posjsonhelper.hibernate5.PostgreSQLDialectEnricher;
import org.hibernate.dialect.PostgreSQL10Dialect;

/**
 * The type that extends {@link PostgreSQL10Dialect} and uses the  {@link PostgreSQLDialectEnricher} to adjust changes to
 * dialect.
 */
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
