package com.github.starnowski.posjsonhelper.core.sql;

import com.github.starnowski.posjsonhelper.core.SystemPropertyReader;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.github.starnowski.posjsonhelper.core.Constants.SQLDEFINITIONCONTEXTFACTORY_TYPES_PROPERTY;

public class SQLDefinitionContextFactorySupplier {

    private final Supplier<Reflections> reflectionsSupplier;
    private final SystemPropertyReader systemPropertyReader;

    public SQLDefinitionContextFactorySupplier() {
        this(() -> new Reflections("com.github.starnowski.posjsonhelper"), new SystemPropertyReader());
    }

    public SQLDefinitionContextFactorySupplier(Supplier<Reflections> reflectionsSupplier, SystemPropertyReader systemPropertyReader) {
        this.reflectionsSupplier = reflectionsSupplier;
        this.systemPropertyReader = systemPropertyReader;
    }

    public List<ISQLDefinitionContextFactory> get() {
        String factoriesPropertyValue = systemPropertyReader.read(SQLDEFINITIONCONTEXTFACTORY_TYPES_PROPERTY);
        Set<Class<? extends ISQLDefinitionContextFactory>> types = null;
        if (factoriesPropertyValue != null) {
            types = Arrays.stream(factoriesPropertyValue.split(",")).map(value -> {
                try {
                    return (Class<? extends ISQLDefinitionContextFactory>) Class.forName(value);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("No such class for name " + value, e);
                }
            }).collect(Collectors.toSet());
        } else {
            types = reflectionsSupplier.get().getSubTypesOf(ISQLDefinitionContextFactory.class);
        }
        List<ISQLDefinitionContextFactory> results = new ArrayList<>();
        for (Class<? extends ISQLDefinitionContextFactory> type : types) {
            try {
                results.add(type.newInstance());
            } catch (Exception e) {
                throw new RuntimeException("Unable to create instance of class with default constructor", e);
            }
        }
        return results;
    }
}
