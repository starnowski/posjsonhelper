package com.github.starnowski.posjsonhelper.json.core.sql;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link com.github.starnowski.posjsonhelper.json.core.sql.JsonUpdateStatementConfigurationBuilder.JsonUpdateStatementOperationSort}.
 * Sorting of two operation based on below criteria:
 * <li>
 * <ul>if the {@link JsonTextArray#getPath()} operation list sizes are not the same, then the component with the smaller operation list size comes first</ul>
 * <ul>if the {@link JsonTextArray#getPath()} operation lists are the same, then individual path fragments are compared in the loop</ul>
 * <ul>First, it is checked whether parts of paths with the same index are the same using the {@link Objects#equals(Object, Object)} method</ul>
 * <ul>If the parts are equal then the next element is being checked</ul>
 * <ul>If the parts are not equal then :</ul>
 * <li>
 *     <ul>If part elements are the same type and type is instance of {@link Comparable} interface then </ul>TODO
 * </li>
 * </li>
 */
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
