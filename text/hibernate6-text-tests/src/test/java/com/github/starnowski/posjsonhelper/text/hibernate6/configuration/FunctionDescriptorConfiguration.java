package com.github.starnowski.posjsonhelper.text.hibernate6.configuration;

import com.github.starnowski.posjsonhelper.hibernate6.SqmFunctionRegistryEnricher;
import jakarta.persistence.EntityManager;
import org.hibernate.query.sqm.NodeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;

@Configuration
@Profile("!different-schema")
public class FunctionDescriptorConfiguration implements
        ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private EntityManager entityManager;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        NodeBuilder nodeBuilder = (NodeBuilder) entityManager.getCriteriaBuilder();
        SqmFunctionRegistryEnricher sqmFunctionRegistryEnricher = new SqmFunctionRegistryEnricher();
        sqmFunctionRegistryEnricher.enrich(nodeBuilder.getQueryEngine().getSqmFunctionRegistry());
    }
}
