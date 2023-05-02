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
}
