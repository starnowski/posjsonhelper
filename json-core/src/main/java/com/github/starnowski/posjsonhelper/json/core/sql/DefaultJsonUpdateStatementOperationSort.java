/**
 * Posjsonhelper library is an open-source project that adds support of
 * Hibernate query for https://www.postgresql.org/docs/10/functions-json.html)
 * <p>
 * Copyright (C) 2023  Szymon Tarnowski
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
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
 *  Each type of operation has its weight. If the weight for two operations is not the same then:</li>
 *  <li>If weight has a lower value for one operation, then the operation should be first in order.</li>
 *  <li>If weight has a higher value for one operation, then the operation should be second in order.</li>
 *  <li>When weights are the same then process is continued</li>
 *  <li> {@link JsonUpdateStatementOperationType#DELETE_BY_SPECIFIC_PATH} has value 9 </li>
 *  <li> {@link JsonUpdateStatementOperationType#JSONB_SET} has value 10 </li>
 *  <li> {@link JsonUpdateStatementOperationType#REMOVE_ARRAY_ITEMS} has value 12 </li>
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
public class DefaultJsonUpdateStatementOperationSort<T> implements JsonUpdateStatementConfigurationBuilder.JsonUpdateStatementOperationSort<T> {

    private static final Map<JsonUpdateStatementOperationType, Integer> operationTypesOrderWeight = Arrays.asList(new Pair<>(JsonUpdateStatementOperationType.DELETE_BY_SPECIFIC_PATH, 9),
            new Pair<>(JsonUpdateStatementOperationType.JSONB_SET, 10),
            new Pair<>(JsonUpdateStatementOperationType.REMOVE_ARRAY_ITEMS, 12)).stream().collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));

    @Override
    public List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation<T>> sort(List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation<T>> operations) {
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
