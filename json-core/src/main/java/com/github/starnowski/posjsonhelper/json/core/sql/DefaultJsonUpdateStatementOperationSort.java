package com.github.starnowski.posjsonhelper.json.core.sql;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link com.github.starnowski.posjsonhelper.json.core.sql.JsonUpdateStatementConfigurationBuilder.JsonUpdateStatementOperationSort}.
 * Sorting of two operation based on below criteria:
 * <ul>
 *  <li>if the {@link JsonTextArray#getPath()} operation list sizes are not the same, then the component with the smaller operation list size comes first</li>
 *  <li>if the {@link JsonTextArray#getPath()} operation lists are the same, then individual path fragments are compared in the loop</li>
 *  <li>First, it is checked whether parts of paths with the same index are the same using the {@link Objects#equals(Object, Object)} method</li>
 *  <li>If the parts are equal then the next element is being checked</li>
 *  <li>If the parts are not equal then :</li>
 *
 *  <li><ul>
 *     <li>If part elements are the same type and type is instance of {@link Comparable} interface then the part paths are compare by {@link Comparable#compareTo(Object)} method</li>
 *     <li>If part elements are not the same type then the part paths are converted to string by executing {@link Object#toString()} and compare its values by {@link Comparable#compareTo(Object)} method</li>
 *     <li>Then the result of the {@link Comparable#compareTo(Object)} method for one of this case is examinated</li>
 *     <li>if the result is equal to zero then the next element from loop is being checked</li>
 *     <li>if the result is not equal to zero then:</li>
 *     <li><ul>
 *         <li>if it is below zero, then the left element comes first</li>
 *         <li>if it is above zero, then right element comes first</li>
 *     </ul></li>
 *  </ul></li>
 * </ul>
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
                    int cr;
                    if (ob1.getClass() == ob2.getClass() && ob1 instanceof Comparable) {
                        Comparable c1 = (Comparable) ob1;
                        cr = c1.compareTo(ob2);
                    } else {
                        String ob1String = ob1.toString();
                        String ob2String = ob2.toString();
                        cr = ob1String.compareTo(ob2String);
                    }
                    if (cr != 0) {
                        return cr;
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
