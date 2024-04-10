package com.github.starnowski.posjsonhelper.json.core.sql;

import java.util.List;
import java.util.stream.Collectors;

public class DefaultJsonUpdateStatementOperationSort implements JsonUpdateStatementConfigurationBuilder.JsonUpdateStatementOperationSort {
    @Override
    public List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> sort(List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> operations) {
        return operations.stream().sorted((o1, o2) -> {
            int size1 = o1.getJsonTextArray().getPath().size();
            int size2 = o2.getJsonTextArray().getPath().size();
            if (size1 == size2) {
                return 0;
            } else if (size1 < size2) {
                return -1;
            } else {
                return 1;
            }
        }).collect(Collectors.toList());
    }
}
