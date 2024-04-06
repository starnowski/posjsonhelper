package com.github.starnowski.posjsonhelper.json.core.sql;

import java.util.ArrayList;
import java.util.List;

public class JsonbSetFunctionJsonPathBuilder {

    private final List<Object> path = new ArrayList<>();

    public JsonbSetFunctionJsonPathBuilder append(Object node) {
        path.add(node);
        return this;
    }

    public JsonbSetFunctionJsonPath build() {
        return new JsonbSetFunctionJsonPath(path);
    }

    public String buildString() {
        return build().toString();
    }
}
