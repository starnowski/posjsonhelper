package com.github.starnowski.posjsonhelper.core.sql

import spock.lang.Specification
import spock.lang.Unroll

class DefaultSQLDefinitionTest extends Specification {

    @Unroll
    def "should set correct properties via constructor, #createScript, #dropScript, #checkingStatements"()
    {
        when:
            def result = new DefaultSQLDefinition(createScript, dropScript, checkingStatements)

        then:
            result.getCreateScript() == createScript
            result.getDropScript() == dropScript
            result.getCheckingStatements() == checkingStatements

        where:
            createScript    |   dropScript  | checkingStatements
            "crate something"   |   "drop something"    | ["check 1", "check 2"]
    }

    def "should throw exception when checking statement is null"()
    {
        when:
        def result = new DefaultSQLDefinition("crate something", "drop something", null)

        then:
            def ex = thrown(IllegalArgumentException)
            ex.message == "checkingStatements can not be null"
    }
}
