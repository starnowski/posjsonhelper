package com.github.starnowski.posjsonhelper.text.hibernate6.operators;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.FunctionRenderingSupport;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.produce.function.StandardFunctionReturnTypeResolvers;
import org.hibernate.query.sqm.tree.SqmCopyContext;
import org.hibernate.query.sqm.tree.expression.SqmExpression;
import org.hibernate.type.StandardBasicTypes;

import java.io.Serializable;
import java.util.List;

public class TextOperatorFunction extends SelfRenderingSqmFunction<Boolean> implements Serializable {

    private final HibernateContext context;
    private final List<? extends SqmExpression<String>> arguments;

    public TextOperatorFunction(NodeBuilder nodeBuilder, List<? extends SqmExpression<String>> arguments, HibernateContext hibernateContext) {
        super(nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(hibernateContext.getTextFunctionOperator()),
                (FunctionRenderingSupport) nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(hibernateContext.getTextFunctionOperator()),
                //TODO Check if only two arguments are being passed!
                //TODO First should be TSVector type
                arguments,
                null,
                null,
                StandardFunctionReturnTypeResolvers.invariant(nodeBuilder.getTypeConfiguration().getBasicTypeRegistry().resolve(StandardBasicTypes.BOOLEAN)),
                nodeBuilder,
                hibernateContext.getTextFunctionOperator());
        this.context = hibernateContext;
        this.arguments = arguments;
    }

    public TextOperatorFunction copy(SqmCopyContext context) {
        TextOperatorFunction existing = (TextOperatorFunction) context.getCopy(this);
        if (existing != null) {
            return existing;
        } else {
            TextOperatorFunction predicate = (TextOperatorFunction) context.registerCopy(this, new TextOperatorFunction(nodeBuilder(), this.arguments, this.context));
            this.copyTo(predicate, context);
            return predicate;
        }
    }
}
