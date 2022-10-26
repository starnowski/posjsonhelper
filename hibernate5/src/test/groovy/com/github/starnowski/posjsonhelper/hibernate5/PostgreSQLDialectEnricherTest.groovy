package com.github.starnowski.posjsonhelper.hibernate5


import com.github.starnowski.posjsonhelper.core.Context
import com.github.starnowski.posjsonhelper.core.CoreContextPropertiesSupplier
import com.github.starnowski.posjsonhelper.core.HibernateContext
import com.github.starnowski.posjsonhelper.core.HibernateContextPropertiesSupplier
import com.github.starnowski.posjsonhelper.hibernate5.functions.JsonArrayFunction
import org.hibernate.dialect.PostgreSQL81Dialect
import org.hibernate.dialect.function.SQLFunction
import org.hibernate.dialect.function.StandardSQLFunction
import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.Function
import java.util.stream.Collectors

class PostgreSQLDialectEnricherTest extends Specification {

    @Unroll
    def "should enrich dialect with expected functions types #expectedFunctionTypes" (){
        given:
            def coreContextPropertiesSupplier = Mock(CoreContextPropertiesSupplier)
            def hibernateContextPropertiesSupplier = Mock(HibernateContextPropertiesSupplier)
            def dialect = new PostgreSQL81Dialect()
            coreContextPropertiesSupplier.get() >> Context.builder().build()
            hibernateContextPropertiesSupplier.get() >> hibernateContext
            def tested = new PostgreSQLDialectEnricher(coreContextPropertiesSupplier, hibernateContextPropertiesSupplier)

        when:
            tested.enrich(dialect)

        then:
            dialect.getFunctions().entrySet().stream().filter({it -> expectedFunctionTypes.containsKey(it.getKey())}).collect(Collectors.toMap(new KeyMapper(), new ValueClassMapper())) == expectedFunctionTypes

        where:
            hibernateContext    ||  expectedFunctionTypes
            HibernateContext.builder().build()  ||  ["jsonb_all_array_strings_exist" : StandardSQLFunction, "jsonb_any_array_strings_exist" : StandardSQLFunction, "json_function_json_array" : JsonArrayFunction]
            HibernateContext.builder().withJsonbAllArrayStringsExistOperator("jsonb_all_el").build()  ||  ["jsonb_all_el" : StandardSQLFunction, "jsonb_any_array_strings_exist" : StandardSQLFunction, "json_function_json_array" : JsonArrayFunction]
            HibernateContext.builder().withJsonbAnyArrayStringsExistOperator("fun_2").build()  ||  ["fun_2" : StandardSQLFunction, "jsonb_all_array_strings_exist" : StandardSQLFunction, "json_function_json_array" : JsonArrayFunction]
            HibernateContext.builder().withJsonFunctionJsonArrayOperator("json_operator").build()  ||  ["jsonb_any_array_strings_exist" : StandardSQLFunction, "jsonb_any_array_strings_exist" : StandardSQLFunction, "json_operator" : JsonArrayFunction]
    }

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

    private static class KeyMapper implements Function<Map.Entry<String, SQLFunction>, String>{

        @Override
        String apply(Map.Entry<String, SQLFunction> entry) {
            return entry.getKey()
        }
    }

    private static class ValueClassMapper implements Function<Map.Entry<String, SQLFunction>, Class>{

        @Override
        Class apply(Map.Entry<String, SQLFunction> entry) {
            return entry.getValue().getClass()
        }
    }
}
