package com.github.starnowski.posjsonhelper.core.sql;

import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SQLDefinitionContextFactoryClasspathSupplier {

    public List<ISQLDefinitionContextFactory> get()
    {
        Reflections reflections = new Reflections("com.github.starnowski.posjsonhelper");
        Set<Class<? extends ISQLDefinitionContextFactory>> types = reflections.getSubTypesOf(ISQLDefinitionContextFactory.class);
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
