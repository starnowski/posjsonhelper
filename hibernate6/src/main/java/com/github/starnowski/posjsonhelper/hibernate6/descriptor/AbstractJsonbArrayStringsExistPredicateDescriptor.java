package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPath;
import com.github.starnowski.posjsonhelper.hibernate6.predicates.AbstractJsonbArrayStringsExistPredicate;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.AbstractSqmSelfRenderingFunctionDescriptor;
import org.hibernate.query.sqm.function.NamedSqmFunctionDescriptor;
import org.hibernate.query.sqm.produce.function.ArgumentsValidator;
import org.hibernate.query.sqm.produce.function.FunctionArgumentTypeResolver;
import org.hibernate.query.sqm.produce.function.FunctionReturnTypeResolver;

public abstract class AbstractJsonbArrayStringsExistPredicateDescriptor<T extends AbstractJsonbArrayStringsExistPredicate> extends NamedSqmFunctionDescriptor {

    protected final HibernateContext hibernateContext;
    public AbstractJsonbArrayStringsExistPredicateDescriptor(String name, HibernateContext hibernateContext) {
        super(name, false, null, null);
        this.hibernateContext = hibernateContext;
    }

    abstract protected T generateJsonbArrayStringsExistPredicate(HibernateContext context, NodeBuilder nodeBuilder, JsonBExtractPath jsonBExtractPath, String[] values);

    abstract public String getSqmFunction();

}
