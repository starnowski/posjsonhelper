package com.github.starnowski.posjsonhelper.json.core.sql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Override
    public String toString() {
        return "{" + path.stream().map(String::valueOf).collect(Collectors.joining(",")) + "}";
    }
}
