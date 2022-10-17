package com.github.starnowski.posjsonhelper.core;

import com.github.starnowski.posjsonhelper.core.operations.CreateOperationsProcessor;
import com.github.starnowski.posjsonhelper.core.operations.DropOperationsProcessor;
import com.github.starnowski.posjsonhelper.core.operations.IDatabaseOperationsProcessor;
import com.github.starnowski.posjsonhelper.core.operations.ValidateOperationsProcessor;
import com.github.starnowski.posjsonhelper.core.operations.exceptions.ValidationDatabaseOperationsException;
import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinition;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.starnowski.posjsonhelper.core.DatabaseOperationType.*;

public class DatabaseOperationExecutor {

    private final Map<DatabaseOperationType, IDatabaseOperationsProcessor> operationsProcessorMap;

    public DatabaseOperationExecutor() {
        this(prepareDatabaseOperationTypeIDatabaseOperationsProcessorMap());
    }

    DatabaseOperationExecutor(Map<DatabaseOperationType, IDatabaseOperationsProcessor> operationsProcessorMap) {
        this.operationsProcessorMap = operationsProcessorMap;
    }

    private static Map<DatabaseOperationType, IDatabaseOperationsProcessor> prepareDatabaseOperationTypeIDatabaseOperationsProcessorMap() {
        Map<DatabaseOperationType, IDatabaseOperationsProcessor> result = new HashMap<>();
        result.put(CREATE, new CreateOperationsProcessor());
        result.put(VALIDATE, new ValidateOperationsProcessor());
        result.put(DROP, new DropOperationsProcessor());
        return result;
    }

    public void execute(DataSource dataSource, List<ISQLDefinition> sqlDefinitions, DatabaseOperationType operationType) throws SQLException, ValidationDatabaseOperationsException {
        IDatabaseOperationsProcessor processor = operationsProcessorMap.get(operationType);
        processor.run(dataSource, sqlDefinitions);
    }

    Map<DatabaseOperationType, IDatabaseOperationsProcessor> getOperationsProcessorMap() {
        return operationsProcessorMap;
    }
}
