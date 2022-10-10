package com.github.starnowski.posjsonhelper.core.operations;

import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinition;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public interface IDatabaseOperationsProcessor {

    void run(DataSource dataSource, List<ISQLDefinition> sqlDefinitions) throws SQLException;
}
