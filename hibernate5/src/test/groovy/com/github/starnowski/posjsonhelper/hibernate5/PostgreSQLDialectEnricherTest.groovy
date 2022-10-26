package com.github.starnowski.posjsonhelper.hibernate5

import com.github.starnowski.posjsonhelper.core.CoreContextPropertiesSupplier
import com.github.starnowski.posjsonhelper.core.HibernateContextPropertiesSupplier
import spock.lang.Specification

class PostgreSQLDialectEnricherTest extends Specification {

    def "should have expected components initialized" (){
        given:
            def tested = new PostgreSQLDialectEnricher()

        when:
            def coreContextPropertiesSupplier = tested.getCoreContextPropertiesSupplier()
            def hibernateContextPropertiesSupplier = tested.getHibernateContextPropertiesSupplier()

        then:
            coreContextPropertiesSupplier.getClass() == CoreContextPropertiesSupplier
            hibernateContextPropertiesSupplier.getClass() == HibernateContextPropertiesSupplier
    }
}
