package com.github.starnowski.posjsonhelper.json.core.sql

import spock.lang.Specification
import spock.lang.Unroll

class DefaultJsonUpdateStatementOperationFilterTest extends Specification {

    @Unroll
    def "should filter correctly json path (current #operations) (expected #expected) : #message"() {
        given:
            def tested = new DefaultJsonUpdateStatementOperationFilter()

        when:
            def result = tested.filter(operations)

        then:
            result == expected

        where:
        operations  ||  expected    |   message
        [jsonbSet((new JsonTextArrayBuilder()).append("a").build(), "b1"), jsonbSet((new JsonTextArrayBuilder()).append("a").build(), "c1")] || [jsonbSet((new JsonTextArrayBuilder()).append("a").build(), "c1")] | "filter redundant operation that is going to override by next operation"
    }

    private static JsonUpdateStatementConfiguration.JsonUpdateStatementOperation jsonbSet(JsonTextArray jsonTextArray, String json)
    {
        new JsonUpdateStatementConfiguration.JsonUpdateStatementOperation(jsonTextArray, JsonUpdateStatementOperationType.JSONB_SET, json)
    }
}
