package com.github.starnowski.posjsonhelper.text.hibernate6.operators;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.FunctionRenderingSupport;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.produce.function.StandardFunctionReturnTypeResolvers;
import org.hibernate.query.sqm.tree.SqmTypedNode;

import java.io.Serializable;
import java.util.List;

public class TextOperatorFunction extends SelfRenderingSqmFunction<String> implements Serializable {
    public TextOperatorFunction(NodeBuilder nodeBuilder, List<? extends SqmTypedNode<?>> arguments, HibernateContext hibernateContext) {
        super(nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(hibernateContext.getTextFunctionOperator()),
                (FunctionRenderingSupport) nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(hibernateContext.getTextFunctionOperator()),
                arguments,
                null,
                null,
                StandardFunctionReturnTypeResolvers.useFirstNonNull(),
                nodeBuilder,
                hibernateContext.getTextFunctionOperator());
    }
}
