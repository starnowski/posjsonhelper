package com.github.starnowski.posjsonhelper.text.hibernate6.operators;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.text.hibernate6.functions.TSVectorFunction;
import jakarta.persistence.criteria.Path;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.FunctionRenderingSupport;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.produce.function.StandardFunctionReturnTypeResolvers;
import org.hibernate.query.sqm.tree.SqmCopyContext;
import org.hibernate.query.sqm.tree.SqmTypedNode;
import org.hibernate.query.sqm.tree.expression.SqmExpression;
import org.hibernate.type.StandardBasicTypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TextOperatorFunction extends SelfRenderingSqmFunction<Boolean> implements Serializable {

    private final HibernateContext context;
    private final TSVectorFunction tsVectorFunction;
    private final SqmExpression<String> argument;

    public TextOperatorFunction(NodeBuilder nodeBuilder, TSVectorFunction tsVectorFunction, SqmExpression<String> argument, HibernateContext hibernateContext) {
        super(nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(hibernateContext.getTextFunctionOperator()),
                (FunctionRenderingSupport) nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(hibernateContext.getTextFunctionOperator()),
                contactParameters(tsVectorFunction, argument),
                null,
                null,
                StandardFunctionReturnTypeResolvers.invariant(nodeBuilder.getTypeConfiguration().getBasicTypeRegistry().resolve(StandardBasicTypes.BOOLEAN)),
                nodeBuilder,
                hibernateContext.getTextFunctionOperator());
        this.context = hibernateContext;
        this.tsVectorFunction = tsVectorFunction;
        this.argument = argument;
    }

    public TextOperatorFunction copy(SqmCopyContext context) {
        TextOperatorFunction existing = (TextOperatorFunction) context.getCopy(this);
        if (existing != null) {
            return existing;
        } else {
            TextOperatorFunction predicate = (TextOperatorFunction) context.registerCopy(this, new TextOperatorFunction(nodeBuilder(), this.tsVectorFunction, this.argument, this.context));
            this.copyTo(predicate, context);
            return predicate;
        }
    }

    private static List<? extends SqmExpression<String>> contactParameters(TSVectorFunction tsVectorFunction, SqmExpression<String> argument) {
        if (tsVectorFunction == null) {
            throw new IllegalArgumentException("TSVectorFunction argument can not be null");
        }
        List<SqmExpression<String>> result = new ArrayList<>();
        result.add(tsVectorFunction);
        result.add(argument);
        return result;
    }
}
