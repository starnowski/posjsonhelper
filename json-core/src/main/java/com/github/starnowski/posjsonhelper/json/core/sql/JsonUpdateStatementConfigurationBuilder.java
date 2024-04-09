package com.github.starnowski.posjsonhelper.json.core.sql;

import java.util.ArrayList;
import java.util.List;

public class JsonUpdateStatementConfigurationBuilder {

    private List<JsonUpdateStatementConfiguration.JsonUpdateStatementOperation> operations = new ArrayList<>();

    public JsonUpdateStatementConfigurationBuilder append(JsonUpdateStatementOperationType operation, JsonTextArray jsonTextArray, String value){
        //TODO
        operations.add(new JsonUpdateStatementConfiguration.JsonUpdateStatementOperation(jsonTextArray, operation, value));
        return this;
    }
}
