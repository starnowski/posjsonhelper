package com.github.starnowski.posjsonhelper.hibernate5.dialects


import com.github.starnowski.posjsonhelper.hibernate5.PostgreSQLDialectEnricher
import spock.lang.Specification

class PostgreSQL95DialectWrapperTest extends Specification {

    def "should use enricher in constructor" (){
        given:
            def enricher = Mock(PostgreSQLDialectEnricher)
            PostgreSQL95DialectWrapper passedArgument = null

        when:
            def tested = new PostgreSQL95DialectWrapper(enricher)

        then:
            1 * enricher.enrich(_) >> {
                arg -> passedArgument = arg[0]
            }
            tested.is(passedArgument)
    }

    def "should have expected components initialized" (){
        given:
            def tested = new PostgreSQL95DialectWrapper()

        when:
            def enricher = tested.getEnricher()

        then:
            enricher.getClass() == PostgreSQLDialectEnricher
    }
}
