package com.github.starnowski.posjsonhelper.hibernate6.demo.configuration;

import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.core.HibernateContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("different-schema-testing-function-execution-with-schema-reference")
public class PosjsonhelperWithDifferentSchemaConfigurationExecuteFunctionWithSchemaReference {

    @Bean
    public HibernateContext getHibernateContext() {
        return HibernateContext.builder()
                .withJsonFunctionJsonArrayOperator("array_fun")
                .withJsonbAnyArrayStringsExistOperator("any_string_in_json")
                .withJsonbAllArrayStringsExistOperator("all_string_in_json").build();
    }

    @Bean
    public Context getContext() {
        return Context.builder()
                .withSchema("non_public_schema")
                .withJsonbAnyArrayStringsExistFunctionReference("poshelper_json_array_any_string")
                .withJsonbAllArrayStringsExistFunctionReference("poshelper_json_array_all_string")
                .build();
    }
}
