package com.github.starnowski.posjsonhelper.hibernate6.operators;

import com.github.starnowski.posjsonhelper.hibernate6.descriptors.JsonArrayFunctionDescriptor;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.FunctionRenderingSupport;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.produce.function.StandardFunctionReturnTypeResolvers;
import org.hibernate.query.sqm.tree.expression.SqmExpression;

import java.io.Serializable;
import java.util.List;

public class JsonArrayFunction extends SelfRenderingSqmFunction<String> implements Serializable {
    public JsonArrayFunction(NodeBuilder nodeBuilder, List<SqmExpression<String>> arguments) {
        super(nodeBuilder.getQueryEngine().getSqmFunctionRegistry().register("array",
                        new JsonArrayFunctionDescriptor()),
                (FunctionRenderingSupport) nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor("array"),
                arguments,
                null,
                null,
                StandardFunctionReturnTypeResolvers.useFirstNonNull(),
                nodeBuilder,
                "array_agg");
    }

    @Override
    public void appendHqlString(StringBuilder sb) {
        sb.append(this.getFunctionName());
        if (this.getArguments().isEmpty()) {
            sb.append("[]");
            return;
        }
        sb.append('[');
        for (int i = 1; i < this.getArguments().size(); ++i) {
            sb.append(", ");
            this.getArguments().get(i).appendHqlString(sb);
        }
        sb.append(']');
    }
}
