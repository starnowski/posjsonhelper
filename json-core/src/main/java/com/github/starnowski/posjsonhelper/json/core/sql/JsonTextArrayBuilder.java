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
import java.util.List;

/**
 * Builder component for {@link JsonTextArray}.
 */
public class JsonTextArrayBuilder {

    private final List<Object> path = new ArrayList<>();

    public JsonTextArrayBuilder append(String node) {
        if (node == null || node.trim().isEmpty()) {
            throw new IllegalArgumentException("Can not pass null or empty string as path value");
        }
        path.add(node);
        return this;
    }

    public JsonTextArrayBuilder append(Integer node) {
        if (node == null) {
            throw new IllegalArgumentException("Can not pass null as path value");
        }
        path.add(node);
        return this;
    }

    /**
     *
     * @return object of type {@link JsonTextArray}
     */
    public JsonTextArray build() {
        return new JsonTextArray(path);
    }

    /**
     *
     * @return string based on created {@link JsonTextArray} object
     */
    public String buildString() {
        return build().toString();
    }
}
