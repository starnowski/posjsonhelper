package com.github.starnowski.posjsonhelper.core

import org.jeasy.random.EasyRandom
import spock.lang.Specification

import static org.mockito.internal.matchers.apachecommons.EqualsBuilder.reflectionEquals

class HibernateContextTest extends Specification {

    def "should build context based on already existing one"()
    {
        given:
            EasyRandom easyRandom = new EasyRandom()
            def randomObject = easyRandom.nextObject(HibernateContext)

        when:
            def result = HibernateContext.builder().withHibernateContext(randomObject).build()

        then:
            reflectionEquals(result, randomObject)
    }

    def "should build context based on already existing one and override it with other values"()
    {
        given:
            EasyRandom easyRandom = new EasyRandom()
            def randomObject = easyRandom.nextObject(HibernateContext)
            def randomObject1 = easyRandom.nextObject(HibernateContext)
            def builder = HibernateContext.builder().withHibernateContext(randomObject)
            def result = builder.build()

        when:
            def result1 = builder
                    .withJsonbAnyArrayStringsExistOperator(randomObject1.getJsonbAnyArrayStringsExistOperator())
                    .withJsonFunctionJsonArrayOperator(randomObject1.getJsonFunctionJsonArrayOperator())
                    .withJsonbAllArrayStringsExistOperator(randomObject1.getJsonbAllArrayStringsExistOperator())
                    .withTextFunctionOperator(randomObject1.getTextFunctionOperator())
                    .withCastFunctionOperator(randomObject1.getCastFunctionOperator())
                    .withConcatenateJsonbOperator(randomObject1.getConcatenateJsonbOperator())
                    .withDeleteJsonBySpecificPathOperator(randomObject1.getDeleteJsonBySpecificPathOperator())
                    .withRemoveJsonValuesFromJsonArrayFunction(randomObject1.getRemoveJsonValuesFromJsonArrayFunction())
                    .withArrayFunctionOperator(randomObject1.getArrayFunctionOperator())
                    .build()



        then:
            reflectionEquals(result, randomObject)
            !reflectionEquals(result, randomObject1)
            reflectionEquals(result1, randomObject1)
            !reflectionEquals(result1, randomObject)
    }
}
