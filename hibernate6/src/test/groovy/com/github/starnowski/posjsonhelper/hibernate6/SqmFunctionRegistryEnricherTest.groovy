package com.github.starnowski.posjsonhelper.hibernate6

import com.github.starnowski.posjsonhelper.core.Context
import com.github.starnowski.posjsonhelper.core.CoreContextPropertiesSupplier
import com.github.starnowski.posjsonhelper.core.HibernateContext
import com.github.starnowski.posjsonhelper.core.HibernateContextPropertiesSupplier
import org.hibernate.query.sqm.function.AbstractSqmFunctionDescriptor
import org.hibernate.query.sqm.function.SqmFunctionDescriptor
import org.hibernate.query.sqm.function.SqmFunctionRegistry
import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.Function
import java.util.stream.Collectors

class SqmFunctionRegistryEnricherTest extends Specification {

    @Unroll
    def "should enrich sqmFunctionRegistry with expected functions types #expectedFunctionTypes" (){
        given:
            def coreContextPropertiesSupplier = Mock(CoreContextPropertiesSupplier)
            def hibernateContextPropertiesSupplier = Mock(HibernateContextPropertiesSupplier)
            def sqmFunctionRegistry = new SqmFunctionRegistry()
            coreContextPropertiesSupplier.get() >> Context.builder().build()
            hibernateContextPropertiesSupplier.get() >> hibernateContext
            def tested = new SqmFunctionRegistryEnricher(coreContextPropertiesSupplier, hibernateContextPropertiesSupplier)

        when:
            tested.enrich(sqmFunctionRegistry)

        then:
        sqmFunctionRegistry.getFunctions().entrySet().stream().filter({it -> expectedFunctionTypes.containsKey(it.getKey())}).collect(Collectors.toMap(new KeyMapper(), new ValueClassMapper())) == expectedFunctionTypes

        where:
            hibernateContext    ||  expectedFunctionTypes
            HibernateContext.builder().build() ||  ["jsonb_all_array_strings_exist": "jsonb_all_array_strings_exist", "jsonb_any_array_strings_exist": "jsonb_any_array_strings_exist", "json_function_json_array": "array", "jsonb_extract_path": "jsonb_extract_path", "jsonb_extract_path_text": "jsonb_extract_path_text"]
    }

    private static class KeyMapper implements Function<Map.Entry<String, SqmFunctionDescriptor>, String> {

        @Override
        String apply(Map.Entry<String, SqmFunctionDescriptor> entry) {
            return entry.getKey()
        }
    }

    private static class ValueClassMapper implements Function<Map.Entry<String, SqmFunctionDescriptor>, Class>{

        @Override
        Class apply(Map.Entry<String, SqmFunctionDescriptor> entry) {
            return ((AbstractSqmFunctionDescriptor)entry.getValue()).getName();
        }
    }
}
