package com.github.starnowski.posjsonhelper.hibernate6.demo.configuration;

import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.SqmFunctionRegistryEnricher;
import jakarta.persistence.EntityManager;
import org.hibernate.query.sqm.NodeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;

@Configuration
@Profile("different-schema-testing-function-execution-with-schema-reference")
public class FunctionDescriptorConfigurationWithDifferentSchemaExecuteFunctionWithSchemaReference implements
        ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private HibernateContext hibernateContext;
    @Autowired
    private Context context;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        NodeBuilder nodeBuilder = (NodeBuilder) entityManager.getCriteriaBuilder();
        SqmFunctionRegistryEnricher sqmFunctionRegistryEnricher = new SqmFunctionRegistryEnricher();
        sqmFunctionRegistryEnricher.enrich(nodeBuilder.getQueryEngine().getSqmFunctionRegistry(), context, hibernateContext);
    }
}
