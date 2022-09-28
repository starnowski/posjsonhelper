package com.github.starnowski.posjsonhelper.core.sql.functions;

import com.github.starnowski.posjsonhelper.core.sql.DefaultSQLDefinition;
import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinition;

public abstract class AbstractDefaultFunctionDefinitionFactory extends AbstractFunctionDefinitionFactory<DefaultSQLDefinition, DefaultFunctionFactoryParameters>{
    @Override
    protected DefaultSQLDefinition returnFunctionDefinition(DefaultFunctionFactoryParameters parameters, ISQLDefinition functionDefinition) {
        return new DefaultSQLDefinition(functionDefinition.getCreateScript(), functionDefinition.getDropScript(), functionDefinition.getCheckingStatements());
    }
}
