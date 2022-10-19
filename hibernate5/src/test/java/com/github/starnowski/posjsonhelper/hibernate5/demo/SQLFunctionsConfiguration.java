package com.github.starnowski.posjsonhelper.hibernate5.demo;

import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.core.DatabaseOperationExecutor;
import com.github.starnowski.posjsonhelper.core.DatabaseOperationType;
import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinition;
import com.github.starnowski.posjsonhelper.core.sql.SQLDefinitionFactoryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class SQLFunctionsConfiguration implements
        ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private DataSource dataSource;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        Context context = Context.builder().build();
        SQLDefinitionFactoryFacade sqlDefinitionFactoryFacade = new SQLDefinitionFactoryFacade();
        List<ISQLDefinition> definitions = sqlDefinitionFactoryFacade.build(context);
        try {
            new DatabaseOperationExecutor().execute(dataSource, definitions, DatabaseOperationType.LOG_ALL);
            new DatabaseOperationExecutor().execute(dataSource, definitions, DatabaseOperationType.CREATE);
            new DatabaseOperationExecutor().execute(dataSource, definitions, DatabaseOperationType.VALIDATE);
        } catch (Exception e) {
            throw new RuntimeException("Error during initialization of sql functions for jsonb type operations", e);
        }
    }
}
