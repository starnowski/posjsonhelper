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
