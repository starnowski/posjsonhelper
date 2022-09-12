package com.github.starnowski.posjsonhelper.core.sql.functions;

public class DefaultFunctionFactoryParameters implements IFunctionFactoryParameters{

    private final String functionName;
    private final String schema;

    public DefaultFunctionFactoryParameters(String functionName, String schema) {
        this.functionName = functionName;
        this.schema = schema;
    }

    @Override
    public String getFunctionName() {
        return functionName;
    }

    @Override
    public String getSchema() {
        return schema;
    }
}
