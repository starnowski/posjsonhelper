package com.github.starnowski.posjsonhelper.json.core.sql;

import java.util.ArrayList;
import java.util.List;

public class JsonbSetFunctionJsonPathBuilder {

    private List<Object> path = new ArrayList<>();

    public JsonbSetFunctionJsonPathBuilder append(Object node) {
        path.add(node);
        return this;
    }

    public String build()
    {
        //TODO
        return null;
    }
}
