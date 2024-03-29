package com.github.starnowski.posjsonhelper.json.core.sql.functions

import com.github.starnowski.posjsonhelper.core.sql.functions.AbstractDefaultFunctionDefinitionFactory
import com.github.starnowski.posjsonhelper.core.sql.functions.IFunctionFactoryParameters
import spock.lang.Specification
import spock.lang.Unroll

import static java.lang.String.format

abstract class AbstractDefaultFunctionDefinitionFactoryGenericTest<T extends AbstractDefaultFunctionDefinitionFactory, P extends IFunctionFactoryParameters> extends Specification {

    def "should return non-empty string object for correct parameters object"() {
        given:
            T tested = returnTestedObject()
            P parameters = returnCorrectParametersSpyObject()

        when:
            String result = tested.produce(parameters).getCreateScript()

        then:
            result != null
            !result.trim().isEmpty()
    }

    def "should throw exception of type 'IllegalArgumentException' when parameters object is null" ()
    {
        given:
            T tested = returnTestedObject()

        when:
            tested.produce((P)null)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "The parameters object cannot be null"
    }

    def "should throw exception of type 'IllegalArgumentException' when function name is null, even if the rest of parameters are correct"()
    {
        given:
            T tested = returnTestedObject()
            P parameters = returnCorrectParametersSpyObject()

        when:
            tested.produce(parameters)

        then:
            1 * parameters.getFunctionName() >> null
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Function name cannot be null"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when function name is blank ('#functionName'), even if the rest of parameters are correct"()
    {
        given:
        T tested = returnTestedObject()
        P parameters = returnCorrectParametersSpyObject()

        when:
        tested.produce(parameters)

        then:
        (1.._) * parameters.getFunctionName() >> functionName
        def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
        ex.message == "Function name cannot be blank"

        where:
        functionName << ["", "  ", "            "]
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when schema name is blank ('#schema'), even if the rest of parameters are correct"()
    {
        given:
        T tested = returnTestedObject()
        P parameters = returnCorrectParametersSpyObject()

        when:
        tested.produce(parameters)

        then:
        (1.._) * parameters.getSchema() >> schema
        def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
        ex.message == "Schema name cannot be blank"

        where:
        schema << ["", "  ", "            "]
    }

    @Unroll
    def "should create correctly the creation statement with contains the right phrase for schema #schema and function #functionName"()
    {
        given:
            T tested = returnTestedObject()
            P parameters = returnCorrectParametersSpyObject()
            parameters.getSchema() >> schema
            parameters.getFunctionName() >> functionName
            def functionDefinition = tested.produce(parameters)
            String functionReference = tested.returnFunctionReference(parameters)
            String expectedCreateFunctionPhrase = format("CREATE OR REPLACE FUNCTION %s(%s)", functionReference, prepareArgumentsPhrase())

        expect:
        functionDefinition.getCreateScript().contains(expectedCreateFunctionPhrase)

        where:
        schema      |   functionName
        null        |   "fun1"
        "public"    |   "fun1"
        "sch"       |   "fun1"
        null        |   "this_is_function"
        "public"    |   "this_is_function"
        "sch"       |   "this_is_function"
    }

    @Unroll
    def "should create correct drop statement for schema #schema and function #functionName"()
    {
        given:
            T tested = returnTestedObject()
            P parameters = returnCorrectParametersSpyObject()
            parameters.getSchema() >> schema
            parameters.getFunctionName() >> functionName
            def functionDefinition = tested.produce(parameters)
            String functionReference = tested.returnFunctionReference(parameters)
            String expectedDropFunctionStatement = format("DROP FUNCTION IF EXISTS %s(%s);", functionReference, prepareArgumentsPhrase())

        expect:
            functionDefinition.getDropScript() == expectedDropFunctionStatement

        where:
            schema      |   functionName
            null        |   "fun1"
            "public"    |   "fun1"
            "sch"       |   "fun1"
            null        |   "this_is_function"
            "public"    |   "this_is_function"
            "sch"       |   "this_is_function"
    }

    @Unroll
    def "should create correct statement that check if function was created for schema #schema and function #functionName"()
    {
        given:
            T tested = returnTestedObject()
            P parameters = returnCorrectParametersSpyObject()
            parameters.getSchema() >> schema
            parameters.getFunctionName() >> functionName
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT COUNT(1) FROM pg_proc pg, pg_catalog.pg_namespace pgn WHERE ")
            sb.append("pg.proname = '")
            sb.append(functionName)
            sb.append("' AND ")
            if (schema == null)
            {
                sb.append("pgn.nspname = 'public'")
            } else {
                sb.append("pgn.nspname = '")
                sb.append(schema)
                sb.append("'")
            }
            sb.append(" AND ")
            sb.append("pg.pronamespace =  pgn.oid;")
            String expectedCheckingStatement = sb.toString()

        when:
            def functionDefinition = tested.produce(parameters)

        then:
            functionDefinition.getCheckingStatements()
            functionDefinition.getCheckingStatements().size() >= 1
            functionDefinition.getCheckingStatements()[0] == expectedCheckingStatement

        where:
            schema      |   functionName
            null        |   "fun1"
            "public"    |   "fun1"
            "sch"       |   "fun1"
            null        |   "this_is_function"
            "public"    |   "this_is_function"
            "sch"       |   "this_is_function"
    }

    protected abstract String prepareArgumentsPhrase()

    protected abstract T returnTestedObject()

    protected abstract P returnCorrectParametersSpyObject();
}
