package com.github.starnowski.posjsonhelper.hibernate6.operators;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import jakarta.persistence.criteria.Path;
import org.hibernate.query.ReturnableType;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.FunctionRenderer;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.function.SqmFunctionDescriptor;
import org.hibernate.query.sqm.produce.function.ArgumentsValidator;
import org.hibernate.query.sqm.produce.function.FunctionReturnTypeResolver;
import org.hibernate.query.sqm.produce.function.StandardFunctionReturnTypeResolvers;
import org.hibernate.query.sqm.tree.SqmTypedNode;
import org.hibernate.query.sqm.tree.expression.SqmExpression;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConcatenateJsonbOperator extends SelfRenderingSqmFunction<String> implements Serializable {
    public ConcatenateJsonbOperator(NodeBuilder nodeBuilder, Path referencedPathSource, String value, HibernateContext hibernateContext) {
        this(nodeBuilder, referencedPathSource, (SqmTypedNode<String>)nodeBuilder.value(value), hibernateContext);
    }

    public ConcatenateJsonbOperator(NodeBuilder nodeBuilder, Path referencedPathSource, SqmTypedNode value, HibernateContext hibernateContext) {
        super(
//                nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(hibernateContext.getJsonFunctionJsonArrayOperator()), //TODO
                nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor("XXXXX"), //TODO
//                (FunctionRenderer) nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(hibernateContext.getJsonFunctionJsonArrayOperator()), //TODO
                (FunctionRenderer) nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor("XXXXX"), //TODO
                mapParameters(nodeBuilder, referencedPathSource, value),
                null,
                null,
                StandardFunctionReturnTypeResolvers.useFirstNonNull(),
                nodeBuilder,
                "XXXXX");
    }

    private static List<? extends SqmTypedNode<?>> mapParameters(NodeBuilder nodeBuilder, Path referencedPathSource, SqmTypedNode value) {
        List<SqmTypedNode<?>> result = new ArrayList<>();
        result.add((SqmTypedNode<?>) referencedPathSource);
        result.add((SqmTypedNode<String>)nodeBuilder.value(value));
        return result;
    }
}
