package com.github.starnowski.posjsonhelper.json.core.sql

import com.github.starnowski.posjsonhelper.core.Context
import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinitionContextFactory
import spock.lang.Specification
import spock.lang.Unroll

abstract class AbstractSQLDefinitionContextFactoryTest<F extends ISQLDefinitionContextFactory> extends Specification {

    @Unroll
    def "should return not null definition for default context"(){
        given:
            def tested = prepareFactory()

        when:
            def result = tested.build(Context.builder().build())

        then:
            result != null
    }

    protected abstract F prepareFactory()
}
