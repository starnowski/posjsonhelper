package com.github.starnowski.posjsonhelper.hibernate6.operators;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import jakarta.persistence.criteria.Path;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.FunctionRenderer;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.produce.function.StandardFunctionReturnTypeResolvers;
import org.hibernate.query.sqm.tree.SqmTypedNode;
import org.hibernate.query.sqm.tree.expression.SqmExpression;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Implemented of HQL function defined by property {@link HibernateContext#deleteJsonBySpecificPathOperator}.
 * It is wrapper for Concatenation operator.
 */
public class DeleteJsonbBySpecifiedPathOperator extends SelfRenderingSqmFunction<String> implements Serializable {

    public DeleteJsonbBySpecifiedPathOperator(NodeBuilder nodeBuilder, Path referencedPathSource, String jsonPath, HibernateContext hibernateContext) {
        this(nodeBuilder, (SqmTypedNode) referencedPathSource, generateCastedJsonPathToTextArray(nodeBuilder, jsonPath, hibernateContext), hibernateContext);
    }

    public DeleteJsonbBySpecifiedPathOperator(NodeBuilder nodeBuilder, SqmTypedNode referencedPathSource, String jsonPath, HibernateContext hibernateContext) {
        this(nodeBuilder, referencedPathSource, generateCastedJsonPathToTextArray(nodeBuilder, jsonPath, hibernateContext), hibernateContext);
    }

    public DeleteJsonbBySpecifiedPathOperator(NodeBuilder nodeBuilder, SqmTypedNode referencedPathSource, SqmTypedNode jsonPath, HibernateContext hibernateContext) {
        super(
                nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(hibernateContext.getDeleteJsonBySpecificPathOperator()),
                (FunctionRenderer) nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(hibernateContext.getDeleteJsonBySpecificPathOperator()),
                mapParameters(referencedPathSource, jsonPath),
                null,
                null,
                StandardFunctionReturnTypeResolvers.useFirstNonNull(),
                nodeBuilder,
                hibernateContext.getDeleteJsonBySpecificPathOperator());
    }

    private static List<? extends SqmTypedNode<?>> mapParameters(SqmTypedNode referencedPathSource, SqmTypedNode jsonPath) {
        List<SqmTypedNode<?>> result = new ArrayList<>();
        result.add((SqmTypedNode<?>) referencedPathSource);
        result.add((SqmTypedNode<?>) jsonPath);
        return result;
    }

    private static CastOperatorFunction generateCastedJsonPathToTextArray(NodeBuilder nodeBuilder, String jsonPath, HibernateContext hibernateContext) {
        return new CastOperatorFunction(nodeBuilder, (SqmExpression<?>) nodeBuilder.value(jsonPath), "text[]", hibernateContext);
    }
}
