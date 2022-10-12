package com.github.starnowski.posjsonhelper.core.operations.exceptions;

import java.util.Map;
import java.util.Set;

public class ValidationDatabaseOperationsException extends AbstractDatabaseOperationsException {

    private final Map<String, Set<String>> failedChecks;

    public ValidationDatabaseOperationsException(Map<String, Set<String>> failedChecks) {
        this.failedChecks = failedChecks;
    }
}
