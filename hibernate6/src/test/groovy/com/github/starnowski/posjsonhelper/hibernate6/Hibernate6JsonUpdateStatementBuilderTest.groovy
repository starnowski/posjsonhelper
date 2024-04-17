package com.github.starnowski.posjsonhelper.hibernate6

import spock.lang.Specification

class Hibernate6JsonUpdateStatementBuilderTest extends Specification {

    def "should set filter and sort component"(){
        given:
            def tested = new Hibernate6JsonUpdateStatementBuilder(null, null, null)
            assert tested.getJsonUpdateStatementConfigurationBuilder().getSort()
            assert tested.getJsonUpdateStatementConfigurationBuilder().getPostSortFilter()

        when:
            tested.withSort(null)
            tested.withPostSortFilter(null)

        then:
            !tested.getJsonUpdateStatementConfigurationBuilder().getSort()
            !tested.getJsonUpdateStatementConfigurationBuilder().getPostSortFilter()
    }
}
