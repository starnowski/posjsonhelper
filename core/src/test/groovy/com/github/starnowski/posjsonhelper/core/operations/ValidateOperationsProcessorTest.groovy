package com.github.starnowski.posjsonhelper.core.operations

import com.github.starnowski.posjsonhelper.core.operations.util.SQLUtil
import spock.lang.Specification

class ValidateOperationsProcessorTest extends Specification {

    def "should return default instance type"(){
        given:
            def tested = new ValidateOperationsProcessor()

        when:
            def result = tested.getSqlUtil()

        then:
        result.getClass() == SQLUtil.class
    }
}
