package com.github.starnowski.posjsonhelper.json.core.sql;

import java.util.ArrayList;
import java.util.List;

public class JsonTextArrayBuilder {

    private final List<Object> path = new ArrayList<>();

    public JsonTextArrayBuilder append(String node) {
        if (node == null) {
            throw new IllegalArgumentException("Can not pass null as path value");
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

    public JsonTextArray build() {
        return new JsonTextArray(path);
    }

    public String buildString() {
        return build().toString();
    }
}
