package com.github.starnowski.posjsonhelper.json.core.sql;

import com.github.starnowski.posjsonhelper.core.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Default implementation of {@link com.github.starnowski.posjsonhelper.json.core.sql.JsonUpdateStatementConfigurationBuilder.JsonUpdateStatementOperationSort}.
 * Sorting of two operation based on below criteria:
 *
 * <ul>
 *  <li>At the beginning, operations are compared based on the result for {@link JsonUpdateStatementConfiguration.JsonUpdateStatementOperation#getOperation()} method.
 *  Each type of operation has its weight. If the weight for two operations is not the same then:
 *  <ul>
 *      <li>If weight has a lower value for one operation, then the operation should be first in order.</li>
 *      <li>If weight has a higher value for one operation, then the operation should be second in order.</li>
 *  </ul>
 *  <li>When weights are the same then process is continued</<li>
 *  <ul>
 *     <li> {@link JsonUpdateStatementOperationType#DELETE_BY_SPECIFIC_PATH} has value 9 </li>
 *     <li> {@link JsonUpdateStatementOperationType#JSONB_SET} has value 10 </li>
 *  </ul>
 *  </li>
 *  <li>if the {@link JsonTextArray#getPath()} operation list sizes are not the same, then the component with the smaller operation list size comes first</li>
 *  <li>if the {@link JsonTextArray#getPath()} operation lists are the same, then individual path fragments are compared in the loop</li>
 *  <li>First, it is checked whether parts of paths with the same index are the same using the {@link Objects#equals(Object, Object)} method</li>
 *  <li>If the parts are equal then the next element is being checked</li>
 *  <li>If the parts are not equal then :</li>
 *
 * <li><ul>
 * <li>If part elements are the same type and type is instance of {@link Comparable} interface then the part paths are compare by {@link Comparable#compareTo(Object)} method</li>
 * <li>If part elements are not the same type then the part paths are converted to string by executing {@link Object#toString()} and compare its values by {@link Comparable#compareTo(Object)} method</li>
 * <li>Then the result of the {@link Comparable#compareTo(Object)} method for one of this case is examinated</li>
 * <li>if the result is equal to zero then the next element from loop is being checked</li>
 * <li>if the result is not equal to zero then:</li>
 * <li><ul>
 * <li>if it is below zero, then the left element comes first</li>
 * <li>if it is above zero, then right element comes first</li>
 * </ul></li>
 * </ul></li>
 * </ul>
 */
public class DefaultJsonUpdateStatementOperationSort implements JsonUpdateStatementConfigurationBuilder.JsonUpdateStatementOperationSort {

    private static final Map<JsonUpdateStatementOperationType, Integer> operationTypesOrderWeight = Arrays.asList(new Pair<>(JsonUpdateStatementOperationType.DELETE_BY_SPECIFIC_PATH, 9),
            new Pair<>(JsonUpdateStatementOperationType.JSONB_SET, 10)).stream().collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));

    @Override
    public List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> sort(List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> operations) {
        return operations.stream().sorted((o1, o2) -> {
            int operation1Weight = operationTypesOrderWeight.get(o1.getOperation());
            int operation2Weight = operationTypesOrderWeight.get(o2.getOperation());
            if (operation1Weight != operation2Weight) {
                return operation1Weight < operation2Weight ? -1 : 1;
            }
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
            } else {
                return size1 < size2 ? -1 : 1;
            }
        }).collect(toList());
    }
}
