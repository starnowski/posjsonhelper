package com.github.starnowski.posjsonhelper.json.core.sql

import com.github.starnowski.posjsonhelper.core.Context
import com.github.starnowski.posjsonhelper.core.sql.DefaultSQLDefinition
import com.github.starnowski.posjsonhelper.core.sql.functions.DefaultFunctionFactoryParameters
import com.github.starnowski.posjsonhelper.json.core.sql.functions.RemoveJsonValuesFromJsonArrayFunctionProducer
import spock.lang.Unroll

class RemoveJsonValuesFromJsonArrayFunctionContextFactoryTest extends AbstractSQLDefinitionContextFactoryTest<RemoveJsonValuesFromJsonArrayFunctionContextFactory> {

    @Unroll
    def "should pass parameters (schema #schema, function name #name) to inner component and pass results"(){
        given:
            def innerFactory = Mock(RemoveJsonValuesFromJsonArrayFunctionProducer)
            def tested = new RemoveJsonValuesFromJsonArrayFunctionContextFactory(innerFactory)
            def context = Mock(Context)
            def definition = Mock(DefaultSQLDefinition)
            DefaultFunctionFactoryParameters parameters
            context.getRemoveValuesFromJsonArrayFunctionReference() >> name
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
            parameters.getSchema() == schema
            parameters.getFunctionName() == name

        where:
            schema      |   name
            null        |   "fun1"
            "public"    |   "produ"
            "schem_1"   |   "produ"
    }

    @Override
    protected RemoveJsonValuesFromJsonArrayFunctionContextFactory prepareFactory() {
        new RemoveJsonValuesFromJsonArrayFunctionContextFactory()
    }
}