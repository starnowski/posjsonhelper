package com.github.starnowski.posjsonhelper.core.sql.functions;

public class DefaultFunctionArgument implements IFunctionArgument {

    private final String type;

    public DefaultFunctionArgument(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }

    public static DefaultFunctionArgument ofType(String type){
        return new DefaultFunctionArgument(type);
    }
}
