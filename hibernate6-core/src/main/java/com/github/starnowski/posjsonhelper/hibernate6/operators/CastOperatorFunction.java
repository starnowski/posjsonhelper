package com.github.starnowski.posjsonhelper.hibernate6.operators;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.FunctionRenderer;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.produce.function.StandardFunctionReturnTypeResolvers;
import org.hibernate.query.sqm.tree.SqmCopyContext;
import org.hibernate.query.sqm.tree.expression.SqmExpression;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CastOperatorFunction extends SelfRenderingSqmFunction<String> implements Serializable {

    private final HibernateContext context;
    private final SqmExpression<String> type;
    private final SqmExpression<?> argument;

    public CastOperatorFunction(NodeBuilder nodeBuilder, SqmExpression<?> argument, String type, HibernateContext hibernateContext) {
        this(nodeBuilder, argument, nodeBuilder.literal(type), hibernateContext);
    }

    public CastOperatorFunction(NodeBuilder nodeBuilder, SqmExpression<?> argument, SqmExpression<String> type, HibernateContext hibernateContext) {
        super(nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(hibernateContext.getCastFunctionOperator()),
                (FunctionRenderer) nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(hibernateContext.getCastFunctionOperator()),
                contactParameters(argument, type),
                null,
                null,
                StandardFunctionReturnTypeResolvers.useFirstNonNull(),
                nodeBuilder,
                hibernateContext.getCastFunctionOperator());
        this.context = hibernateContext;
        this.argument = argument;
        this.type = type;
    }

    private static List<? extends SqmExpression<?>> contactParameters(SqmExpression<?> argument, SqmExpression<String> type) {
        if (argument == null) {
            throw new IllegalArgumentException("Argument argument can not be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Type argument can not be null");
        }
        List<SqmExpression<?>> result = new ArrayList<>();
        result.add(argument);
        result.add(type);
        return result;
    }

    public CastOperatorFunction copy(SqmCopyContext context) {
        CastOperatorFunction existing = context.getCopy(this);
        if (existing != null) {
            return existing;
        } else {
            CastOperatorFunction predicate = context.registerCopy(this, new CastOperatorFunction(nodeBuilder(), this.argument, this.type, this.context));
            this.copyTo(predicate, context);
            return predicate;
        }
    }
}
