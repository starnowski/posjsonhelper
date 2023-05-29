package com.github.starnowski.posjsonhelper.hibernate6.operators;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.FunctionRenderingSupport;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.produce.function.StandardFunctionReturnTypeResolvers;
import org.hibernate.query.sqm.tree.expression.SqmExpression;

import java.io.Serializable;
import java.util.List;

public class JsonArrayFunction extends SelfRenderingSqmFunction<String> implements Serializable {

    public JsonArrayFunction(NodeBuilder nodeBuilder, List<SqmExpression<String>> arguments, HibernateContext hibernateContext) {
        this(nodeBuilder, arguments, hibernateContext, true);
    }

    public JsonArrayFunction(NodeBuilder nodeBuilder, List<SqmExpression<String>> arguments, HibernateContext hibernateContext, boolean shouldTryToRegisterFunction) {
        super(nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(hibernateContext.getJsonFunctionJsonArrayOperator()),
                (FunctionRenderingSupport) nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(hibernateContext.getJsonFunctionJsonArrayOperator()),
                arguments,
                null,
                null,
                StandardFunctionReturnTypeResolvers.useFirstNonNull(),
                nodeBuilder,
                hibernateContext.getJsonFunctionJsonArrayOperator());
    }
}
