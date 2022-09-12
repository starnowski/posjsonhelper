package com.github.starnowski.posjsonhelper.core.sql;

public interface SQLDefinitionProducer<R extends SQLDefinition, P extends SQLDefinitionProducerParameters> {

    R produce(P parameters);
}
