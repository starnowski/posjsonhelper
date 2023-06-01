package com.github.starnowski.posjsonhelper.hibernate6.demo.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.core.HibernateContext;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.type.format.jackson.JacksonJsonFormatMapper;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("different-schema")
public class PosjsonhelperWithDifferentSchemaConfiguration {

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

    @Bean
    HibernatePropertiesCustomizer jsonFormatMapperCustomizer(ObjectMapper objectMapper) {
        return (properties) -> properties.put(AvailableSettings.JSON_FORMAT_MAPPER,
                new JacksonJsonFormatMapper(objectMapper));
    }
}
