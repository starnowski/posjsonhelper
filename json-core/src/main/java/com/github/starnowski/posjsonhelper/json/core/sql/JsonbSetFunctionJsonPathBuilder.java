package com.github.starnowski.posjsonhelper.json.core.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JsonbSetFunctionJsonPathBuilder {

    private final List<Object> path = new ArrayList<>();

    public JsonbSetFunctionJsonPathBuilder append(Object node) {
        path.add(node);
        return this;
    }

    public String build() {
        return "{" + path.stream().map(String::valueOf).collect(Collectors.joining(",")) + "}";
    }
}
