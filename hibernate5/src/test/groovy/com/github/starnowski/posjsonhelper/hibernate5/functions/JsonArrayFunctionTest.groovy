package com.github.starnowski.posjsonhelper.hibernate5.functions

import org.hibernate.QueryException
import spock.lang.Specification
import spock.lang.Unroll

class JsonArrayFunctionTest extends Specification {

    def "should declare as function with arguments"(){
        given:
            def tested = new JsonArrayFunction()

        when:
            def hasArguments = tested.hasArguments()

        then:
            hasArguments
    }

    def "should declare as function with parentheses if no arguments"(){
        given:
            def tested = new JsonArrayFunction()

        when:
            def hasParenthesesIfNoArguments = tested.hasParenthesesIfNoArguments()

        then:
            hasParenthesesIfNoArguments
    }

    def "should declare as function that returns correct type"(){
        given:
            def tested = new JsonArrayFunction()

        when:
            def type = tested.getReturnType(null, null)

        then:
            type == org.hibernate.type.BooleanType.INSTANCE
    }

    def "should throw exception when no arguments is being passed"(){
        given:
            def tested = new JsonArrayFunction()

        when:
            tested.render(null, [], null)

        then:
            def ex = thrown(QueryException)

        and: "exception should has correct message"
            ex.message == "json_array requires at least one argument"
    }

    @Unroll
    def "should create expected statement '#expectedStatement'"(){
        given:
            def tested = new JsonArrayFunction()

        when:
            def result = tested.render(null, args, null)

        then:
            result == expectedStatement

        where:
            args || expectedStatement
            ["x"]  ||  "array[x]"
            ["x1"]  ||  "array[x1]"
            ["x", "some value"]  ||  "array[x,some value]"
            ["z", "x", "some value"]  ||  "array[z,x,some value]"
    }
}
