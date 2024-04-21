package com.github.starnowski.posjsonhelper.json.core.sql;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefaultJsonUpdateStatementOperationSort implements JsonUpdateStatementConfigurationBuilder.JsonUpdateStatementOperationSort {
    @Override
    public List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> sort(List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> operations) {
        return operations.stream().sorted((o1, o2) -> {
            int size1 = o1.getJsonTextArray().getPath().size();
            int size2 = o2.getJsonTextArray().getPath().size();
            if (size1 == size2) {
                for (int i = 0; i < size1; i++) {
                    Object ob1 = o1.getJsonTextArray().getPath().get(i);
                    Object ob2 = o2.getJsonTextArray().getPath().get(i);
                    if (Objects.equals(ob1, ob2)) {
                        continue;
                    }
                    if (ob1.getClass() == ob2.getClass() && ob1 instanceof Comparable) {
                        Comparable c1 = (Comparable) ob1;
                        return c1.compareTo(ob2);
                    } else {
                        String ob1String = ob1.toString();
                        String ob2String = ob2.toString();
                        return ob1String.compareTo(ob2String);
                    }
                }
                return 0;
            } else if (size1 < size2) {
                return -1;
            } else {
                return 1;
            }
        }).collect(Collectors.toList());
    }
}
