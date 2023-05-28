package com.github.starnowski.posjsonhelper.hibernate6;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;

public class PosjsonhelperFunctionContributor implements FunctionContributor {

    private final SqmFunctionRegistryEnricher sqmFunctionRegistryEnricher;

    public PosjsonhelperFunctionContributor() {
        this(new SqmFunctionRegistryEnricher());
    }

    PosjsonhelperFunctionContributor(SqmFunctionRegistryEnricher sqmFunctionRegistryEnricher) {
        this.sqmFunctionRegistryEnricher = sqmFunctionRegistryEnricher;
    }


    @Override
    public void contributeFunctions(FunctionContributions functionContributions) {
        sqmFunctionRegistryEnricher.enrich(functionContributions.getFunctionRegistry());
    }
}
