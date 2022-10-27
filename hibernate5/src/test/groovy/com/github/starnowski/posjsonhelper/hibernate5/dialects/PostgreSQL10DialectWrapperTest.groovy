package com.github.starnowski.posjsonhelper.hibernate5.dialects

import com.github.starnowski.posjsonhelper.hibernate5.PostgreSQLDialectEnricher
import spock.lang.Specification

class PostgreSQL10DialectWrapperTest extends Specification {

    def "should use enricher in constructor" (){
        given:
        def enricher = Mock(PostgreSQLDialectEnricher)
        PostgreSQL10DialectWrapper passedArgument = null

        when:
        def tested = new PostgreSQL10DialectWrapper(enricher)

        then:
        1 * enricher.enrich(_) >> {
            arg -> passedArgument = arg[0]
        }
        tested.is(passedArgument)
    }

    def "should have expected components initialized" (){
        given:
        def tested = new PostgreSQL10DialectWrapper()

        when:
        def enricher = tested.getEnricher()

        then:
        enricher.getClass() == PostgreSQLDialectEnricher
    }
}
