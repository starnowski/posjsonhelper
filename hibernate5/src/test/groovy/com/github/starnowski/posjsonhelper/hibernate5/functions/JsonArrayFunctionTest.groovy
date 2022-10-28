package com.github.starnowski.posjsonhelper.hibernate5.functions

import spock.lang.Specification

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
}
