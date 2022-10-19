package com.github.starnowski.posjsonhelper.core.operations;

import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class DropOperationsProcessor implements IDatabaseOperationsProcessor {
    @Override
    public void run(DataSource dataSource, List<ISQLDefinition> sqlDefinitions) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            LinkedList<ISQLDefinition> stack = new LinkedList<>();
            sqlDefinitions.forEach(stack::push);
            for (ISQLDefinition sqlDefinition: stack) {
                Statement statement = connection.createStatement();
                statement.execute(sqlDefinition.getDropScript());
            }
        }
    }
}
