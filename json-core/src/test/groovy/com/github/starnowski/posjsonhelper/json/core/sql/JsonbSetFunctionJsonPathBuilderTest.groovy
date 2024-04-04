package com.github.starnowski.posjsonhelper.json.core.sql

import spock.lang.Specification
import spock.lang.Unroll

import static java.util.Arrays.asList

class JsonbSetFunctionJsonPathBuilderTest extends Specification {

    @Unroll
    def "should create correct json path (#expectedJsonPath) for object list (#list)"() {
        given:
            def tested = new JsonbSetFunctionJsonPathBuilder()
            list.forEach {tested.append(it)}

        when:
            def result = tested.build()

        then:
            result == expectedJsonPath

        where:
            list                        || expectedJsonPath
            asList("child", "birthday") || "{child,birthday}"
            asList("child", "pets")     || "{child,pets}"
            asList("child", 0)          || "{child,0}"
            asList("child", -1)         || "{child,-1}"
            asList("a1", -1)            || "{a1,-1}"
            asList(0, "b2")             || "{0,b2}"
            asList(0, "b2", "label")    || "{0,b2,label}"
    }
}
