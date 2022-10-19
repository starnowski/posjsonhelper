package com.github.starnowski.posjsonhelper.core.sql;

import com.github.starnowski.posjsonhelper.core.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SQLDefinitionFactoryFacade {

    private final List<ISQLDefinitionContextFactory> factories;

    public SQLDefinitionFactoryFacade() {
        this(Arrays.asList(new JsonbAllArrayStringsExistFunctionContextFactory(), new JsonbAnyArrayStringsExistFunctionContextFactory()));
    }

    SQLDefinitionFactoryFacade(List<ISQLDefinitionContextFactory> factories) {
        this.factories = factories;
    }

    public List<ISQLDefinition> build(Context context) {
        return factories.stream().map(factory -> factory.build(context)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    List<ISQLDefinitionContextFactory> getFactoriesCopy() {
        return new ArrayList<>(factories);
    }
}
