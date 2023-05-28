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
    def "should enrich sqmFunctionRegistry with expected functions types #expectedFunctionTypes"() {
        given:
            def coreContextPropertiesSupplier = Mock(CoreContextPropertiesSupplier)
            def hibernateContextPropertiesSupplier = Mock(HibernateContextPropertiesSupplier)
            def sqmFunctionRegistry = new SqmFunctionRegistry()
            coreContextPropertiesSupplier.get() >> context
            hibernateContextPropertiesSupplier.get() >> hibernateContext
            def tested = new SqmFunctionRegistryEnricher(coreContextPropertiesSupplier, hibernateContextPropertiesSupplier)

        when:
            tested.enrich(sqmFunctionRegistry)

        then:
            sqmFunctionRegistry.getFunctions().entrySet().stream().filter({ it -> expectedFunctionTypes.containsKey(it.getKey()) }).collect(Collectors.toMap(new KeyMapper(), new ValueClassMapper())) == expectedFunctionTypes

        where:
            hibernateContext    | context                   || expectedFunctionTypes
            hcBuilder().build() | cBuilder().build()        || ["jsonb_all_array_strings_exist": "jsonb_all_array_strings_exist", "jsonb_any_array_strings_exist": "jsonb_any_array_strings_exist", "json_function_json_array": "array", "jsonb_extract_path": "jsonb_extract_path", "jsonb_extract_path_text": "jsonb_extract_path_text"]
            hcBuilder().withJsonbAllArrayStringsExistOperator("jsonb_all").build() | cBuilder().build()        || ["jsonb_all": "jsonb_all_array_strings_exist", "jsonb_any_array_strings_exist": "jsonb_any_array_strings_exist", "json_function_json_array": "array", "jsonb_extract_path": "jsonb_extract_path", "jsonb_extract_path_text": "jsonb_extract_path_text"]
            hcBuilder().withJsonbAllArrayStringsExistOperator("jsonb_all").build() | cBuilder().withJsonbAllArrayStringsExistFunctionReference("json_all_call").build()        || ["jsonb_all": "json_all_call", "jsonb_any_array_strings_exist": "jsonb_any_array_strings_exist", "json_function_json_array": "array", "jsonb_extract_path": "jsonb_extract_path", "jsonb_extract_path_text": "jsonb_extract_path_text"]
    }

    private static HibernateContext.ContextBuilder hcBuilder()
    {
        HibernateContext.builder()
    }

    private static Context.ContextBuilder cBuilder()
    {
        Context.builder()
    }

    private static class KeyMapper implements Function<Map.Entry<String, SqmFunctionDescriptor>, String> {

        @Override
        String apply(Map.Entry<String, SqmFunctionDescriptor> entry) {
            return entry.getKey()
        }
    }

    private static class ValueClassMapper implements Function<Map.Entry<String, SqmFunctionDescriptor>, String>{

        @Override
        String apply(Map.Entry<String, SqmFunctionDescriptor> entry) {
            return ((AbstractSqmFunctionDescriptor)entry.getValue()).getName();
        }
    }
}
