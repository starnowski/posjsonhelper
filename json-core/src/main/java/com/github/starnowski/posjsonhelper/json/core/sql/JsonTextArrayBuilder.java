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
