package com.github.starnowski.posjsonhelper.hibernate6.operators;

import org.hibernate.metamodel.mapping.ordering.ast.FunctionExpression;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.produce.function.StandardFunctionReturnTypeResolvers;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class JsonArrayFunction extends SelfRenderingSqmFunction<String> implements Serializable {
    public JsonArrayFunction(NodeBuilder nodeBuilder, List<String> arguments) {
        super(nodeBuilder.getQueryEngine().getSqmFunctionRegistry().registerNamed("array"),
                new FunctionExpression("array", arguments.size()),
                arguments.stream().map(p -> nodeBuilder.literal(p)).collect(Collectors.toList()),
                null,
                null,
                StandardFunctionReturnTypeResolvers.useFirstNonNull(),
                nodeBuilder,
                "array");
    }
}
