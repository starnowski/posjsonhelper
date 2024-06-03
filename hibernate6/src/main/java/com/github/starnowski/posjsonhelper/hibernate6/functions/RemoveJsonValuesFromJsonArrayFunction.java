package com.github.starnowski.posjsonhelper.hibernate6.functions;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.operators.JsonbCastOperatorFunction;
import jakarta.persistence.criteria.Path;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.FunctionRenderer;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.produce.function.StandardFunctionReturnTypeResolvers;
import org.hibernate.query.sqm.tree.SqmTypedNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RemoveJsonValuesFromJsonArrayFunction extends SelfRenderingSqmFunction<String> implements Serializable {
    public RemoveJsonValuesFromJsonArrayFunction(NodeBuilder nodeBuilder, Path referencedPathSource, String value, HibernateContext hibernateContext) {
        this(nodeBuilder, referencedPathSource, new JsonbCastOperatorFunction(nodeBuilder, value, hibernateContext), hibernateContext);
    }

    public RemoveJsonValuesFromJsonArrayFunction(NodeBuilder nodeBuilder, Path referencedPathSource, SqmTypedNode value, HibernateContext hibernateContext) {
        this(nodeBuilder, (SqmTypedNode<?>)referencedPathSource, value, hibernateContext);
    }

    public RemoveJsonValuesFromJsonArrayFunction(NodeBuilder nodeBuilder, SqmTypedNode<?> referencedPathSource, SqmTypedNode value, HibernateContext hibernateContext) {
        super(
                nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(hibernateContext.getRemoveJsonValuesFromJsonArrayFunction()),
                (FunctionRenderer) nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(hibernateContext.getRemoveJsonValuesFromJsonArrayFunction()),
                mapParameters(nodeBuilder, referencedPathSource, value),
                null,
                null,
                StandardFunctionReturnTypeResolvers.useFirstNonNull(),
                nodeBuilder,
                hibernateContext.getRemoveJsonValuesFromJsonArrayFunction());
    }

    private static List<? extends SqmTypedNode<?>> mapParameters(NodeBuilder nodeBuilder, SqmTypedNode<?> referencedPathSource, SqmTypedNode value) {
        List<SqmTypedNode<?>> result = new ArrayList<>();
        result.add(referencedPathSource);
        result.add((SqmTypedNode<String>) nodeBuilder.value(value));
        return result;
    }
}