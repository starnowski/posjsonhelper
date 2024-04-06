package com.github.starnowski.posjsonhelper.json.core.sql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class JsonTextArray {

    private final List<Object> path;

    public JsonTextArray(List<Object> path) {
        this.path = path == null ? new ArrayList<>() : Collections.unmodifiableList(path);
    }

    public List<Object> getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "{" + path.stream().map(String::valueOf).collect(Collectors.joining(",")) + "}";
    }
}
