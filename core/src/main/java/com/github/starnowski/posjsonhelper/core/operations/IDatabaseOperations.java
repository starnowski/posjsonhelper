package com.github.starnowski.posjsonhelper.core.operations;

import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinition;

import javax.sql.DataSource;
import java.util.List;

public interface IDatabaseOperations {

    void run(DataSource dataSource, List<ISQLDefinition> sqlDefinitions);
}
