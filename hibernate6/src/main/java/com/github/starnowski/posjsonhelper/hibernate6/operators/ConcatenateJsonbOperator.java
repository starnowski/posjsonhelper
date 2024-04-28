package com.github.starnowski.posjsonhelper.hibernate6.operators;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import jakarta.persistence.criteria.Path;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.FunctionRenderer;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.produce.function.StandardFunctionReturnTypeResolvers;
import org.hibernate.query.sqm.tree.SqmTypedNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Implemented of HQL function defined by property {@link HibernateContext#concatenateJsonbOperator}.
 * It is wrapper for Concatenation operator.
 */
public class ConcatenateJsonbOperator extends SelfRenderingSqmFunction<String> implements Serializable {
    public ConcatenateJsonbOperator(NodeBuilder nodeBuilder, Path referencedPathSource, String value, HibernateContext hibernateContext) {
        this(nodeBuilder, referencedPathSource, new JsonbCastOperatorFunction(nodeBuilder, value, hibernateContext), hibernateContext);
    }

    public ConcatenateJsonbOperator(NodeBuilder nodeBuilder, Path referencedPathSource, SqmTypedNode value, HibernateContext hibernateContext) {
        super(
                nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(hibernateContext.getConcatenateJsonbOperator()),
                (FunctionRenderer) nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(hibernateContext.getConcatenateJsonbOperator()),
                mapParameters(nodeBuilder, referencedPathSource, value),
                null,
                null,
                StandardFunctionReturnTypeResolvers.useFirstNonNull(),
                nodeBuilder,
                hibernateContext.getConcatenateJsonbOperator());
    }

    private static List<? extends SqmTypedNode<?>> mapParameters(NodeBuilder nodeBuilder, Path referencedPathSource, SqmTypedNode value) {
        List<SqmTypedNode<?>> result = new ArrayList<>();
        result.add((SqmTypedNode<?>) referencedPathSource);
        result.add((SqmTypedNode<String>) nodeBuilder.value(value));
        return result;
    }
}
