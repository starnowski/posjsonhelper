package com.github.starnowski.posjsonhelper.hibernate6;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;

public class PosjsonhelperFunctionContributor implements FunctionContributor {
    @Override
    public void contributeFunctions(FunctionContributions functionContributions) {
        SqmFunctionRegistryEnricher sqmFunctionRegistryEnricher = new SqmFunctionRegistryEnricher();
        sqmFunctionRegistryEnricher.enrich(functionContributions.getFunctionRegistry());
    }
}
