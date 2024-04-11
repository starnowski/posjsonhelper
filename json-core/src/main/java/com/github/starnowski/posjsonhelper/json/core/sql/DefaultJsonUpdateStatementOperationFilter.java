package com.github.starnowski.posjsonhelper.json.core.sql;

import java.util.List;

public class DefaultJsonUpdateStatementOperationFilter implements JsonUpdateStatementConfigurationBuilder.JsonUpdateStatementOperationFilter {
    @Override
    public List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> filter(List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> operations) {
        // Process/Gather information about operations objects
        // Filter operations objects based on context
        return operations;
    }
}
