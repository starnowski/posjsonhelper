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
package com.github.starnowski.posjsonhelper.core.operations.exceptions;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationDatabaseOperationsException extends AbstractDatabaseOperationsException {

    public Map<String, Set<String>> getFailedChecks() {
        return failedChecks;
    }

    private final Map<String, Set<String>> failedChecks;

    public ValidationDatabaseOperationsException(Map<String, Set<String>> failedChecks) {
        super(prepareMessage(failedChecks));
        this.failedChecks = failedChecks;
    }

    private static String prepareMessage(Map<String, Set<String>> failedChecks) {
        Optional<Map.Entry<String, Set<String>>> firstFailedCheck = failedChecks.entrySet().stream().findFirst();
        if (firstFailedCheck.isPresent()) {
            Map.Entry<String, Set<String>> failedCheck = firstFailedCheck.get();
            return String.format("Failed check statements for ddl instruction \"%s\", failed checks %s", failedCheck.getKey(), failedCheck.getValue().stream().map(fc -> "\"" + fc + "\"").collect(Collectors.joining(", ", "[", "]")));
        }
        return "";
    }
}
