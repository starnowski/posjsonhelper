package com.github.starnowski.posjsonhelper.core.sql;

import java.util.List;

public class DefaultSQLDefinition implements SQLDefinition {

    private final String createScript;
    private final String dropScript;
    private final List<String> checkingStatements;

    public DefaultSQLDefinition(String createScript, String dropScript) {
        this(createScript, dropScript, null);
    }

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
