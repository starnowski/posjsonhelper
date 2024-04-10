package com.github.starnowski.posjsonhelper.json.core.sql;

import java.util.ArrayList;
import java.util.List;

public class JsonTextArrayBuilder {

    private final List<Object> path = new ArrayList<>();

    public JsonTextArrayBuilder append(String node) {
        path.add(node);
        return this;
    }

    public JsonTextArrayBuilder append(Integer node) {
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
