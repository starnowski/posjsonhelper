package com.github.starnowski.posjsonhelper.core.sql;

import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinition;
import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinitionContextFactory;

/**
 * This class is only used for tests purpose
 */
public class TestClasspathSQLDefinitionContextFactory implements ISQLDefinitionContextFactory {
    @Override
    public ISQLDefinition build(Context context) {
        return null;
    }
}
