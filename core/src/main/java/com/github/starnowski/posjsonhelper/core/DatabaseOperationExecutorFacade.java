package com.github.starnowski.posjsonhelper.core;

import com.github.starnowski.posjsonhelper.core.operations.exceptions.AbstractDatabaseOperationsException;
import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinition;
import com.github.starnowski.posjsonhelper.core.sql.SQLDefinitionFactoryFacade;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public class DatabaseOperationExecutorFacade {

    private final DatabaseOperationExecutor databaseOperationExecutor;

    private final SQLDefinitionFactoryFacade sqlDefinitionFactoryFacade;
    public DatabaseOperationExecutorFacade(){
        this(new DatabaseOperationExecutor(), new SQLDefinitionFactoryFacade());
    }

    DatabaseOperationExecutorFacade(DatabaseOperationExecutor databaseOperationExecutor, SQLDefinitionFactoryFacade sqlDefinitionFactoryFacade) {
        this.databaseOperationExecutor = databaseOperationExecutor;
        this.sqlDefinitionFactoryFacade = sqlDefinitionFactoryFacade;
    }

    public void execute(DataSource dataSource, Context context, DatabaseOperationType operationType) throws SQLException, AbstractDatabaseOperationsException {
        List<ISQLDefinition> definitions = sqlDefinitionFactoryFacade.build(context);
        databaseOperationExecutor.execute(dataSource, definitions, operationType);
    }

    DatabaseOperationExecutor getDatabaseOperationExecutor() {
        return databaseOperationExecutor;
    }

    SQLDefinitionFactoryFacade getSqlDefinitionFactoryFacade() {
        return sqlDefinitionFactoryFacade;
    }
}
