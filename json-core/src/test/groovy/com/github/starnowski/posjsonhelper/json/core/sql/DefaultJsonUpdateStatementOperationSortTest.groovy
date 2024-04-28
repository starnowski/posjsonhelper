package com.github.starnowski.posjsonhelper.json.core.sql

import spock.lang.Specification
import spock.lang.Unroll

class DefaultJsonUpdateStatementOperationSortTest extends Specification {

    @Unroll
    def "should sort correctly json path (current #operations) (expected #expected) : #message"() {
        given:
            def tested = new DefaultJsonUpdateStatementOperationSort()

        when:
            def result = tested.sort(operations)

        then:
            result == expected

        where:
        operations  ||  expected    |   message
        [jsonbSet((new JsonTextArrayBuilder()).append("a").append("a").build()), jsonbSet((new JsonTextArrayBuilder()).append("a").build())] || [jsonbSet((new JsonTextArrayBuilder()).append("a").build()), jsonbSet((new JsonTextArrayBuilder()).append("a").append("a").build())] | "Sorting based on array length"
        [jsonbSet((new JsonTextArrayBuilder()).append("a").build()), jsonbSet((new JsonTextArrayBuilder()).append("a").append("a").build())] || [jsonbSet((new JsonTextArrayBuilder()).append("a").build()), jsonbSet((new JsonTextArrayBuilder()).append("a").append("a").build())] | "Sorting based on array length"
        [jsonbSet((new JsonTextArrayBuilder()).append("b").build()), jsonbSet((new JsonTextArrayBuilder()).append("a").build())] || [jsonbSet((new JsonTextArrayBuilder()).append("a").build()), jsonbSet((new JsonTextArrayBuilder()).append("b").build())] | "Simple alphabet sorting"
        [jsonbSet((new JsonTextArrayBuilder()).append("a").build()), jsonbSet((new JsonTextArrayBuilder()).append("b").build())] || [jsonbSet((new JsonTextArrayBuilder()).append("a").build()), jsonbSet((new JsonTextArrayBuilder()).append("b").build())] | "Simple alphabet sorting"
        [jsonbSet((new JsonTextArrayBuilder()).append("a").append("a").build()), jsonbSet((new JsonTextArrayBuilder()).append("a").append(1).build())] || [jsonbSet((new JsonTextArrayBuilder()).append("a").append(1).build()), jsonbSet((new JsonTextArrayBuilder()).append("a").append("a").build())] | "Alphabet and digit sorting"
        [jsonbSet((new JsonTextArrayBuilder()).append("a").append("a").build()), jsonbSet((new JsonTextArrayBuilder()).append("a").append(11111111).build())] || [jsonbSet((new JsonTextArrayBuilder()).append("a").append(11111111).build()), jsonbSet((new JsonTextArrayBuilder()).append("a").append("a").build())] | "Alphabet and digit sorting, digit longer than string value"
        [jsonbSet((new JsonTextArrayBuilder()).append("kids").append("0").append("b").build()), jsonbSet((new JsonTextArrayBuilder()).append("kids").append(0).append("a").build())] || [jsonbSet((new JsonTextArrayBuilder()).append("kids").append(0).append("a").build()), jsonbSet((new JsonTextArrayBuilder()).append("kids").append("0").append("b").build())] | "one operation has string part and other has integer but both represents the same value"
    }

    private static JsonUpdateStatementConfiguration.JsonUpdateStatementOperation jsonbSet(JsonTextArray jsonTextArray)
    {
        new JsonUpdateStatementConfiguration.JsonUpdateStatementOperation(jsonTextArray, JsonUpdateStatementOperationType.JSONB_SET, "XX1")
    }
}
