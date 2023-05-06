/**
 *     Posjsonhelper library is an open-source project that adds support of
 *     Hibernate query for https://www.postgresql.org/docs/10/functions-json.html)
 *
 *     Copyright (C) 2023  Szymon Tarnowski
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 */
package com.github.starnowski.posjsonhelper.core;

import com.github.starnowski.posjsonhelper.core.operations.*;
import com.github.starnowski.posjsonhelper.core.operations.exceptions.AbstractDatabaseOperationsException;
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
        result.put(LOG_ALL, new DatabaseOperationsLoggerProcessor());
        return result;
    }

    public void execute(DataSource dataSource, List<ISQLDefinition> sqlDefinitions, DatabaseOperationType operationType) throws SQLException, AbstractDatabaseOperationsException {
        IDatabaseOperationsProcessor processor = operationsProcessorMap.get(operationType);
        processor.run(dataSource, sqlDefinitions);
    }

    Map<DatabaseOperationType, IDatabaseOperationsProcessor> getOperationsProcessorMap() {
        return operationsProcessorMap;
    }
}
