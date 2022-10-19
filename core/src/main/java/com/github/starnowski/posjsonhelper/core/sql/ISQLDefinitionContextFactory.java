package com.github.starnowski.posjsonhelper.core.sql;

import com.github.starnowski.posjsonhelper.core.Context;

public interface ISQLDefinitionContextFactory {

    ISQLDefinition build(Context context);
}
