package com.github.starnowski.posjsonhelper.core.sql;

public interface ISQLDefinitionFactory<R extends ISQLDefinition, P extends ISQLDefinitionFactoryParameters> {

    R produce(P parameters);
}
