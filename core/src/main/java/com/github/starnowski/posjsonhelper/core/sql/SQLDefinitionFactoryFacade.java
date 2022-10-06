package com.github.starnowski.posjsonhelper.core.sql;

import com.github.starnowski.posjsonhelper.core.Context;

import java.util.ArrayList;
import java.util.List;

public class SQLDefinitionFactoryFacade {

    private final List<ISQLDefinitionContextFactory> factories;

    public SQLDefinitionFactoryFacade(List<ISQLDefinitionContextFactory> factories) {
        this.factories = factories;
    }

    public List<ISQLDefinition> build(Context context){
        //TODO
        return null;
    }

    List<ISQLDefinitionContextFactory> getFactoriesCopy() {
        return new ArrayList<>(factories);
    }
}
