package com.github.starnowski.posjsonhelper.json.core.sql

import spock.lang.Specification

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
            result == [operation4, operation3, operation2]
    }
}
