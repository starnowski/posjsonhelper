package com.github.starnowski.posjsonhelper.core.sql;

public interface ISQLDefinitionProducer<R extends ISQLDefinition, P extends ISQLDefinitionProducerParameters> {

    R produce(P parameters);
}
