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
package com.github.starnowski.posjsonhelper.json.core.sql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Component that create json array string based on list of objects.
 *
 * For example for below list :
 *
 * <pre>{@code
 *     ["parent", "child", 3, "toy", 1 ]
 * }</pre>
 *
 * component is going to generate:
 *
 * <pre>{@code
 * "{parent,child,3,toy,1}"
 * }</pre>
 *
 * for below list :
 * <pre>{@code
 *     ["payload", "address", "street" ]
 * }</pre>
 *
 * component is going to generate:
 *
 * <pre>{@code
 * "{payload,address,street}"
 * }</pre>
 *
 * @see #toString()
 */
public class JsonTextArray {

    private final List<Object> path;

    JsonTextArray(List<Object> path) {
        this.path = path == null ? new ArrayList<>() : Collections.unmodifiableList(path);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonTextArray that = (JsonTextArray) o;
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }

    public List<Object> getPath() {
        return path;
    }

    /**
     * Returns json array based on list of objects.
     * The result string is going to have "{" bracket at the beginning.
     * And "}" bracket at the end.
     * The elements of array are going to be separated by "," character.
     * @return json array string based on list of objects
     */
    @Override
    public String toString() {
        return "{" + path.stream().map(String::valueOf).collect(Collectors.joining(",")) + "}";
    }
}
