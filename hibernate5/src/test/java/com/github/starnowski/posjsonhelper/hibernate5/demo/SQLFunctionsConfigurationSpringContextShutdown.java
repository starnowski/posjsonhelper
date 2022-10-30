package com.github.starnowski.posjsonhelper.hibernate5.demo;

import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.core.DatabaseOperationExecutorFacade;
import com.github.starnowski.posjsonhelper.core.DatabaseOperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;

import javax.sql.DataSource;

@Configuration
public class SQLFunctionsConfigurationSpringContextShutdown implements
        ApplicationListener<ContextClosedEvent> {

    @Autowired
    private Context context;
    @Autowired
    private DataSource dataSource;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        DatabaseOperationExecutorFacade facade = new DatabaseOperationExecutorFacade();
        try {
            facade.execute(dataSource, context, DatabaseOperationType.LOG_ALL);
            facade.execute(dataSource, context, DatabaseOperationType.DROP);
        } catch (Exception e) {
            throw new RuntimeException("Error during dropping of sql functions for jsonb type operations", e);
        }
    }
}
