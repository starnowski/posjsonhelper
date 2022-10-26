package com.github.starnowski.posjsonhelper.hibernate5;

import com.github.starnowski.posjsonhelper.core.CoreContextPropertiesSupplier;
import com.github.starnowski.posjsonhelper.core.HibernateContextPropertiesSupplier;
import com.github.starnowski.posjsonhelper.hibernate5.functions.JsonArrayFunction;
import org.hibernate.dialect.PostgreSQL81Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class PostgreSQLDialectEnricher {

    private final CoreContextPropertiesSupplier coreContextPropertiesSupplier;
    private final HibernateContextPropertiesSupplier hibernateContextPropertiesSupplier;

    public PostgreSQLDialectEnricher() {
        this(new CoreContextPropertiesSupplier(), new HibernateContextPropertiesSupplier());
    }

    public PostgreSQLDialectEnricher(CoreContextPropertiesSupplier coreContextPropertiesSupplier, HibernateContextPropertiesSupplier hibernateContextPropertiesSupplier) {
        this.coreContextPropertiesSupplier = coreContextPropertiesSupplier;
        this.hibernateContextPropertiesSupplier = hibernateContextPropertiesSupplier;
    }

    public void enrich(PostgreSQL81Dialect postgreSQL81Dialect) {
        //TODO Add HibernateContext and Context
        postgreSQL81Dialect.getFunctions().put("json_function_json_array", new JsonArrayFunction());
        postgreSQL81Dialect.getFunctions().put("jsonb_all_array_strings_exist", new StandardSQLFunction("jsonb_all_array_strings_exist", StandardBasicTypes.BOOLEAN));
        postgreSQL81Dialect.getFunctions().put("jsonb_any_array_strings_exist", new StandardSQLFunction("jsonb_any_array_strings_exist", StandardBasicTypes.BOOLEAN));
    }
}
