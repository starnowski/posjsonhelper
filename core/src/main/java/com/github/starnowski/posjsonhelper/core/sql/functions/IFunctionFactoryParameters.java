package com.github.starnowski.posjsonhelper.core.sql.functions;

import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinitionFactoryParameters;

public interface IFunctionFactoryParameters extends ISQLDefinitionFactoryParameters {

    String getFunctionName();

    String getSchema();
}
