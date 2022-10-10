package com.github.starnowski.posjsonhelper.core;

import com.github.starnowski.posjsonhelper.core.operations.IDatabaseOperationsProcessor;
import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinition;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class DatabaseOperationExecutor {

    private final Map<DatabaseOperationType, IDatabaseOperationsProcessor> operationsProcessorMap;

    public DatabaseOperationExecutor(Map<DatabaseOperationType, IDatabaseOperationsProcessor> operationsProcessorMap) {
        this.operationsProcessorMap = operationsProcessorMap;
    }

    public void execute(DataSource dataSource, List<ISQLDefinition> sqlDefinitions, DatabaseOperationType operationType) {
        IDatabaseOperationsProcessor processor = operationsProcessorMap.get(operationType);
        processor.run(dataSource, sqlDefinitions);
        //TODO
    }
}
