package com.github.starnowski.posjsonhelper.core.sql;

import com.github.starnowski.posjsonhelper.core.SystemPropertyReader;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class SQLDefinitionContextFactorySupplier {

    private final Supplier<Reflections> reflectionsSupplier;
    private final SystemPropertyReader systemPropertyReader;

    public SQLDefinitionContextFactorySupplier()
    {
        this(() -> new Reflections("com.github.starnowski.posjsonhelper"), new SystemPropertyReader());
    }

    public SQLDefinitionContextFactorySupplier(Supplier<Reflections> reflectionsSupplier, SystemPropertyReader systemPropertyReader) {
        this.reflectionsSupplier = reflectionsSupplier;
        this.systemPropertyReader = systemPropertyReader;
    }

    public List<ISQLDefinitionContextFactory> get()
    {
        Set<Class<? extends ISQLDefinitionContextFactory>> types = reflectionsSupplier.get().getSubTypesOf(ISQLDefinitionContextFactory.class);
        List<ISQLDefinitionContextFactory> results = new ArrayList<>();
        for (Class<? extends ISQLDefinitionContextFactory> type : types)
        {
            try {
                results.add(type.newInstance());
            } catch (Exception e) {
                //TODO Tests
                throw new RuntimeException("Unable to create instance of class with default constructor", e);
            }
        }
        return results;
    }
}
