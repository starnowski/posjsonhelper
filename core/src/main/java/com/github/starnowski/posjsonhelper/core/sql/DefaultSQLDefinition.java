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
package com.github.starnowski.posjsonhelper.core.sql;

import java.util.List;

public class DefaultSQLDefinition implements ISQLDefinition {

    private final String createScript;
    private final String dropScript;
    private final List<String> checkingStatements;

    public DefaultSQLDefinition(String createScript, String dropScript, List<String> checkingStatements) {
        this.createScript = createScript;
        this.dropScript = dropScript;
        if (checkingStatements == null) {
            throw new IllegalArgumentException("checkingStatements can not be null");
        }
        this.checkingStatements = checkingStatements;
    }

    @Override
    public String getCreateScript() {
        return createScript;
    }

    @Override
    public String getDropScript() {
        return dropScript;
    }

    @Override
    public List<String> getCheckingStatements() {
        return checkingStatements;
    }
}
