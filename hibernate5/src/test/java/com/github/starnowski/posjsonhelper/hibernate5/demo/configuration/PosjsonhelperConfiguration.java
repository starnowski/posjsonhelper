package com.github.starnowski.posjsonhelper.hibernate5.demo.configuration;

import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.core.HibernateContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!different-schema")
public class PosjsonhelperConfiguration {

    @Bean
    public HibernateContext getHibernateContext(){
        return HibernateContext.builder().build();
    }

    @Bean
    public Context getContext(){
        return Context.builder().build();
    }
}
