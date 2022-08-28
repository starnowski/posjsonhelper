package com.github.starnowski.posjsonhelper.hibernate5.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.sql.DataSource;

@Configuration
public class SQLFunctionsConfiguration implements
        ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private DataSource dataSource;


    //TODO skipExecution

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        SqlFunctionScriptsExecutor sqlFunctionScriptsExecutor = new SqlFunctionScriptsExecutor();
        try {
            sqlFunctionScriptsExecutor.execute(dataSource, this.getClass().getResource("schema-postgresql.sql").toURI());
        } catch (Exception e) {
            throw new RuntimeException("Error during initialization of sql functions for jsonb type operations", e);
        }
    }
}
