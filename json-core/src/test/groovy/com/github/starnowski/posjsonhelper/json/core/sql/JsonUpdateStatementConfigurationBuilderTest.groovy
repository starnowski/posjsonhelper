package com.github.starnowski.posjsonhelper.json.core.sql

import spock.lang.Specification

import static com.github.starnowski.posjsonhelper.json.core.sql.JsonUpdateStatementOperationType.JSONB_SET
import static com.github.starnowski.posjsonhelper.json.core.sql.JsonUpdateStatementOperationType.JSONB_SET

class JsonUpdateStatementConfigurationBuilderTest extends Specification {

    def "should build configuration based on components if component are defined"()
    {
        given:
            JsonUpdateStatementConfiguration.JsonUpdateStatementOperation operation1 = Mock(JsonUpdateStatementConfiguration.JsonUpdateStatementOperation)
            JsonUpdateStatementConfiguration.JsonUpdateStatementOperation operation2 = Mock(JsonUpdateStatementConfiguration.JsonUpdateStatementOperation)
            JsonUpdateStatementConfiguration.JsonUpdateStatementOperation operation3 = Mock(JsonUpdateStatementConfiguration.JsonUpdateStatementOperation)
            JsonUpdateStatementConfiguration.JsonUpdateStatementOperation operation4 = Mock(JsonUpdateStatementConfiguration.JsonUpdateStatementOperation)
            JsonUpdateStatementConfigurationBuilder.JsonUpdateStatementOperationSort sort = Mock(JsonUpdateStatementConfigurationBuilder.JsonUpdateStatementOperationSort)
            JsonUpdateStatementConfigurationBuilder.JsonUpdateStatementOperationFilter filter = Mock(JsonUpdateStatementConfigurationBuilder.JsonUpdateStatementOperationFilter)
            def tested = new JsonUpdateStatementConfigurationBuilder()
                    .append(operation1)
                    .append(operation2)
                    .append(operation3)
                    .append(operation4)
                    .withPostSortFilter(filter)
                    .withSort(sort)

        when:
            def result = tested.build()

        then:
            1 * sort.sort([operation1, operation2, operation3, operation4]) >> [operation4, operation3, operation1, operation2]
            1 * filter.filter([operation4, operation3, operation1, operation2]) >> [operation4, operation3, operation2]
            result
            result.getOperations() == [operation4, operation3, operation2]
    }

    def "should build configuration even when component are null"()
    {
        given:
            JsonUpdateStatementConfiguration.JsonUpdateStatementOperation operation1 = Mock(JsonUpdateStatementConfiguration.JsonUpdateStatementOperation)
            JsonUpdateStatementConfiguration.JsonUpdateStatementOperation operation2 = Mock(JsonUpdateStatementConfiguration.JsonUpdateStatementOperation)
            JsonUpdateStatementConfiguration.JsonUpdateStatementOperation operation3 = Mock(JsonUpdateStatementConfiguration.JsonUpdateStatementOperation)
            JsonUpdateStatementConfiguration.JsonUpdateStatementOperation operation4 = Mock(JsonUpdateStatementConfiguration.JsonUpdateStatementOperation)
            def tested = new JsonUpdateStatementConfigurationBuilder()
                    .append(operation1)
                    .append(operation2)
                    .append(operation3)
                    .append(operation4)
                    .withPostSortFilter(null)
                    .withSort(null)

        when:
            def result = tested.build()

        then:
            result
            result.getOperations() == [operation1, operation2, operation3, operation4]
    }

    def "should build correct configuration"()
    {
        given:
            def tested = new JsonUpdateStatementConfigurationBuilder()
                    .append(JSONB_SET, (new JsonTextArrayBuilder()).append("parent").append("child1").build(), "some value1")
                    .withPostSortFilter(null)
                    .withSort(null)

        when:
            def result = tested.build()

        then:
            result
            result.getOperations() == [new JsonUpdateStatementConfiguration.JsonUpdateStatementOperation((new JsonTextArrayBuilder()).append("parent").append("child1").build(), JSONB_SET, "some value1")]
    }

    def "should organize list"(){
        given:
            def tested = new JsonUpdateStatementConfigurationBuilder()
                    .withSort(new DefaultJsonUpdateStatementOperationSort())
                    .withPostSortFilter(new DefaultJsonUpdateStatementOperationFilter())
                    .append(JSONB_SET, new JsonTextArrayBuilder().append("child").append("birthday").build(), "\"2021-11-23\"")
                    .append(JSONB_SET, new JsonTextArrayBuilder().append("child").append("pets").build(), "[\"cat\"]")
                    .append(JSONB_SET, new JsonTextArrayBuilder().append("parents").append(0).build(), "{\"type\":\"mom\", \"name\":\"simone\"}")
                    .append(JSONB_SET, new JsonTextArrayBuilder().append("parents").build(), "[]")


        when:
            def result = tested.build()

        then:
            result
            result.operations
            System.out.println(result.operations)
    }
}
