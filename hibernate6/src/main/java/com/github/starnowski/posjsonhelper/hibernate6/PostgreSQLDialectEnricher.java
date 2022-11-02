package com.github.starnowski.posjsonhelper.hibernate6;

import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.core.CoreContextPropertiesSupplier;
import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.core.HibernateContextPropertiesSupplier;
import org.hibernate.dialect.PostgreSQLDialect;

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

    public void enrich(PostgreSQLDialect postgreSQLDialect) {
        enrich(postgreSQLDialect, coreContextPropertiesSupplier.get(), hibernateContextPropertiesSupplier.get());
    }

    public void enrich(PostgreSQLDialect postgreSQLDialect, Context context, HibernateContext hibernateContext) {
        //TODO
//        postgreSQLDialect.initializeFunctionRegistry().put(hibernateContext.getJsonFunctionJsonArrayOperator(), new JsonArrayFunction());
//        postgreSQLDialect.getFunctions().put(hibernateContext.getJsonbAllArrayStringsExistOperator(), new StandardSQLFunction(context.getJsonbAllArrayStringsExistFunctionReference(), StandardBasicTypes.BOOLEAN));
//        postgreSQLDialect.getFunctions().put(hibernateContext.getJsonbAnyArrayStringsExistOperator(), new StandardSQLFunction(context.getJsonbAnyArrayStringsExistFunctionReference(), StandardBasicTypes.BOOLEAN));
    }

    CoreContextPropertiesSupplier getCoreContextPropertiesSupplier() {
        return coreContextPropertiesSupplier;
    }

    HibernateContextPropertiesSupplier getHibernateContextPropertiesSupplier() {
        return hibernateContextPropertiesSupplier;
    }
}
