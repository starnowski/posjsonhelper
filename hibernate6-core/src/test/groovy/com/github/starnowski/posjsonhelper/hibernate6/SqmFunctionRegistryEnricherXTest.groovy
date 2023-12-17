package com.github.starnowski.posjsonhelper.hibernate6

import com.github.starnowski.posjsonhelper.core.Context
import com.github.starnowski.posjsonhelper.core.CoreContextPropertiesSupplier
import com.github.starnowski.posjsonhelper.core.HibernateContext
import com.github.starnowski.posjsonhelper.core.HibernateContextPropertiesSupplier
import com.github.starnowski.posjsonhelper.hibernate6.descriptor.AbstractConditionalFunctionDescriptorRegister
import com.github.starnowski.posjsonhelper.hibernate6.descriptor.FunctionDescriptorRegister
import com.github.starnowski.posjsonhelper.hibernate6.descriptor.FunctionDescriptorRegisterFactoriesSupplier
import com.github.starnowski.posjsonhelper.hibernate6.descriptor.FunctionDescriptorRegisterFactory
import org.hibernate.query.sqm.function.AbstractSqmFunctionDescriptor
import org.hibernate.query.sqm.function.SqmFunctionDescriptor
import org.hibernate.query.sqm.function.SqmFunctionRegistry
import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.Function
import java.util.stream.Collectors

class SqmFunctionRegistryEnricherXTest extends Specification {

//    @Unroll
    def "should enrich sqmFunctionRegistry with expected functions types #expectedFunctionTypes"() {
        given:
            def coreContextPropertiesSupplier = Mock(CoreContextPropertiesSupplier)
            def hibernateContextPropertiesSupplier = Mock(HibernateContextPropertiesSupplier)
            def functionDescriptorRegisterFactoriesSupplier = Mock(FunctionDescriptorRegisterFactoriesSupplier)
            def functionDescriptorRegisterFactory = Mock(FunctionDescriptorRegisterFactory)
            def functionDescriptorRegister = Mock(FunctionDescriptorRegister)
            def sqmFunctionRegistry = new SqmFunctionRegistry()
            def context = cBuilder().build()
            def hibernateContext = hcBuilder().build()
            coreContextPropertiesSupplier.get() >> context
            hibernateContextPropertiesSupplier.get() >> hibernateContext
            functionDescriptorRegisterFactoriesSupplier.get() >> [functionDescriptorRegisterFactory]
            functionDescriptorRegisterFactory.get(context, hibernateContext) >> functionDescriptorRegister
            def tested = new SqmFunctionRegistryEnricherX(coreContextPropertiesSupplier, hibernateContextPropertiesSupplier, functionDescriptorRegisterFactoriesSupplier)

        when:
            tested.enrich(sqmFunctionRegistry)

        then:
            1 * functionDescriptorRegister.registerFunction(sqmFunctionRegistry)
//        sqmFunctionRegistry.getFunctions().entrySet().stream().filter({ it -> expectedFunctionTypes.containsKey(it.getKey()) }).collect(Collectors.toMap(new KeyMapper(), new ValueClassMapper())) == expectedFunctionTypes

//        where:
//            hibernateContext    | context                   || expectedFunctionTypes
//            hcBuilder().build() | cBuilder().build()        || ["jsonb_all_array_strings_exist": "jsonb_all_array_strings_exist", "jsonb_any_array_strings_exist": "jsonb_any_array_strings_exist", "json_function_json_array": "array", "jsonb_extract_path": "jsonb_extract_path", "jsonb_extract_path_text": "jsonb_extract_path_text"]
//            hcBuilder().withJsonbAllArrayStringsExistOperator("jsonb_all").build() | cBuilder().build()        || ["jsonb_all": "jsonb_all_array_strings_exist", "jsonb_any_array_strings_exist": "jsonb_any_array_strings_exist", "json_function_json_array": "array", "jsonb_extract_path": "jsonb_extract_path", "jsonb_extract_path_text": "jsonb_extract_path_text"]
//            hcBuilder().withJsonbAllArrayStringsExistOperator("jsonb_all").build() | cBuilder().withJsonbAllArrayStringsExistFunctionReference("json_all_call").build()        || ["jsonb_all": "json_all_call", "jsonb_any_array_strings_exist": "jsonb_any_array_strings_exist", "json_function_json_array": "array", "jsonb_extract_path": "jsonb_extract_path", "jsonb_extract_path_text": "jsonb_extract_path_text"]
//            hcBuilder().withJsonbAnyArrayStringsExistOperator("any_fun").build() | cBuilder().withJsonbAnyArrayStringsExistFunctionReference("only_some").build()        || ["jsonb_all_array_strings_exist": "jsonb_all_array_strings_exist", "any_fun": "only_some", "json_function_json_array": "array", "jsonb_extract_path": "jsonb_extract_path", "jsonb_extract_path_text": "jsonb_extract_path_text"]
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
