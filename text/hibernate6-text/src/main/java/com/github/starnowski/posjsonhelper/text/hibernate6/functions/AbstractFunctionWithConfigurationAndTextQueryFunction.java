package com.github.starnowski.posjsonhelper.text.hibernate6.functions;

import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.FunctionRenderingSupport;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.produce.function.StandardFunctionReturnTypeResolvers;
import org.hibernate.query.sqm.tree.SqmTypedNode;
import org.hibernate.type.StandardBasicTypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFunctionWithConfigurationAndTextQueryFunction extends SelfRenderingSqmFunction<String> implements Serializable {


    public AbstractFunctionWithConfigurationAndTextQueryFunction(NodeBuilder nodeBuilder, String configuration, String query, String functionName) {
        this(mapPathParameters(nodeBuilder, configuration, query), nodeBuilder, functionName);
    }

    public AbstractFunctionWithConfigurationAndTextQueryFunction(List<? extends SqmTypedNode<?>> parameters, NodeBuilder nodeBuilder, String functionName) {
        super(nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(functionName),
                (FunctionRenderingSupport) nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(functionName),
                //TODO Check if only two arguments are being passed!
                parameters,
                null,
                null,
                StandardFunctionReturnTypeResolvers.invariant(nodeBuilder.getTypeConfiguration().getBasicTypeRegistry().resolve(StandardBasicTypes.STRING)),
                nodeBuilder,
                functionName);
    }

    private static List<? extends SqmTypedNode<?>> mapPathParameters(NodeBuilder nodeBuilder, String configuration, String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Query argument can not be null or empty string");
        }
        List<SqmTypedNode<?>> result = new ArrayList<>();
        if (configuration != null) {
            if (configuration.trim().isEmpty()) {
                throw new IllegalArgumentException("Configuration argument can not be empty string");
            }
            //TODO Literal
            result.add(nodeBuilder.literal(configuration));
        }
        result.add(nodeBuilder.value(query));
        return result;
    }
}
