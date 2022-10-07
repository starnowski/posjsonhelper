package com.github.starnowski.posjsonhelper.core.sql

import com.github.starnowski.posjsonhelper.core.Context
import com.github.starnowski.posjsonhelper.core.sql.functions.DefaultFunctionFactoryParameters
import com.github.starnowski.posjsonhelper.core.sql.functions.JsonbAnyArrayStringsExistFunctionProducer
import spock.lang.Specification
import spock.lang.Unroll

class JsonbAnyArrayStringsExistFunctionContextFactoryTest extends Specification {

    @Unroll
    def "should pass parameters (schema #schema, function name #name) to inner component and pass results"(){
        given:
            def innerFactory = Mock(JsonbAnyArrayStringsExistFunctionProducer)
            def tested = new JsonbAnyArrayStringsExistFunctionContextFactory(innerFactory)
            def context = Mock(Context)
            def definition = Mock(DefaultSQLDefinition)
            DefaultFunctionFactoryParameters parameters
            context.getJsonbAllArrayStringsExistFunctionReference() >> name
            context.getSchema() >> schema

        when:
            def result = tested.build(context)

        then:
            1 * innerFactory.produce(_) >> {
                params ->
                    parameters = params[0]
                    definition
            }
            result == definition

        and: "passed correct parameters"

        where:
            schema      |   name
            null        |   "fun1"
            "public"    |   "produ"
            "schem_1"   |   "produ"
    }
}
