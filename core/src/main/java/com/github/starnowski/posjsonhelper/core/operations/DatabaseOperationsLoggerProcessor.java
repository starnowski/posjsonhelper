package com.github.starnowski.posjsonhelper.core.operations;

import com.github.starnowski.posjsonhelper.core.operations.exceptions.ValidationDatabaseOperationsException;
import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinition;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class DatabaseOperationsLoggerProcessor implements IDatabaseOperationsProcessor {

    private final Logger logger = Logger.getLogger(DatabaseOperationsLoggerProcessor.class.getName());

    @Override
    public void run(DataSource dataSource, List<ISQLDefinition> sqlDefinitions) throws SQLException, ValidationDatabaseOperationsException {

    }
}
