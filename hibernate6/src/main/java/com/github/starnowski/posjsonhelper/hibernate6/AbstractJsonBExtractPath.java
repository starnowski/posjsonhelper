package com.github.starnowski.posjsonhelper.hibernate6;

import jakarta.persistence.criteria.Path;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.FunctionRenderingSupport;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.produce.function.StandardFunctionReturnTypeResolvers;
import org.hibernate.query.sqm.tree.SqmTypedNode;
import org.hibernate.query.sqm.tree.domain.SqmBasicValuedSimplePath;
import org.hibernate.type.StandardBasicTypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractJsonBExtractPath<T extends AbstractJsonBExtractPath>
        extends SelfRenderingSqmFunction<String> implements Serializable {

    public AbstractJsonBExtractPath(Path referencedPathSource, NodeBuilder nodeBuilder, List<String> path, String functionName) {
        super(nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(functionName),
                (FunctionRenderingSupport) nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(functionName),
                parameters(referencedPathSource, nodeBuilder, path),
                null,
                null,
                StandardFunctionReturnTypeResolvers.invariant(nodeBuilder.getTypeConfiguration().getBasicTypeRegistry().resolve(StandardBasicTypes.STRING)),
                nodeBuilder,
                functionName);
    }

    private static List<? extends SqmTypedNode<?>> parameters(Path referencedPathSource, NodeBuilder nodeBuilder, List<String> path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Path argument can not be null or empty list");
        }
        List<SqmTypedNode<?>> result = new ArrayList<>();
        result.add((SqmTypedNode) referencedPathSource);
        result.addAll(path.stream().map(p -> nodeBuilder.value(p)).collect(Collectors.toList()));
        return result;
    }
}