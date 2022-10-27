package com.github.starnowski.posjsonhelper.hibernate5;

import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.core.CoreContextPropertiesSupplier;
import com.github.starnowski.posjsonhelper.core.HibernateContext;
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
        enrich(postgreSQL81Dialect, coreContextPropertiesSupplier.get(), hibernateContextPropertiesSupplier.get());
    }

    public void enrich(PostgreSQL81Dialect postgreSQL81Dialect, Context context, HibernateContext hibernateContext) {
        postgreSQL81Dialect.getFunctions().put(hibernateContext.getJsonFunctionJsonArrayOperator(), new JsonArrayFunction());
        postgreSQL81Dialect.getFunctions().put(hibernateContext.getJsonbAllArrayStringsExistOperator(), new StandardSQLFunction(context.getJsonbAllArrayStringsExistFunctionReference(), StandardBasicTypes.BOOLEAN));
        postgreSQL81Dialect.getFunctions().put(hibernateContext.getJsonbAnyArrayStringsExistOperator(), new StandardSQLFunction(context.getJsonbAnyArrayStringsExistFunctionReference(), StandardBasicTypes.BOOLEAN));
    }

    CoreContextPropertiesSupplier getCoreContextPropertiesSupplier() {
        return coreContextPropertiesSupplier;
    }

    HibernateContextPropertiesSupplier getHibernateContextPropertiesSupplier() {
        return hibernateContextPropertiesSupplier;
    }
}
