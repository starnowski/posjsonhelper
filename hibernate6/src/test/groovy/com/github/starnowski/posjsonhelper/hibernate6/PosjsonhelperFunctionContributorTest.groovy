package com.github.starnowski.posjsonhelper.hibernate6

import org.hibernate.boot.model.FunctionContributions
import org.hibernate.query.sqm.function.SqmFunctionRegistry
import spock.lang.Specification

class PosjsonhelperFunctionContributorTest extends Specification {

    def "should execute enricher"(){
        given:
            SqmFunctionRegistryEnricher sqmFunctionRegistryEnricher = Mock(SqmFunctionRegistryEnricher)
            def tested = new PosjsonhelperFunctionContributor(sqmFunctionRegistryEnricher)
            FunctionContributions functionContributions = Mock(FunctionContributions)
            SqmFunctionRegistry sqmFunctionRegistry = Mock(SqmFunctionRegistry)

        when:
            tested.contributeFunctions(functionContributions)

        then:
            1 * functionContributions.getFunctionRegistry() >> sqmFunctionRegistry
            1 * sqmFunctionRegistryEnricher.enrich(sqmFunctionRegistry)
    }
}
