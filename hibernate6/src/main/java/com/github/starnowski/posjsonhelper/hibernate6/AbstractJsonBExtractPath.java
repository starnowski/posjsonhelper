package com.github.starnowski.posjsonhelper.hibernate6;

import jakarta.persistence.criteria.Path;
import org.hibernate.metamodel.mapping.ordering.ast.FunctionExpression;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.produce.function.StandardFunctionReturnTypeResolvers;
import org.hibernate.query.sqm.tree.SqmCopyContext;
import org.hibernate.query.sqm.tree.SqmTypedNode;
import org.hibernate.query.sqm.tree.domain.SqmBasicValuedSimplePath;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractJsonBExtractPath<T extends AbstractJsonBExtractPath>
        extends SelfRenderingSqmFunction<String> implements Serializable {

    private final Path referencedPathSourcePath;
    private final List<String> path;

    public AbstractJsonBExtractPath(Path referencedPathSource, NodeBuilder nodeBuilder, List<String> path, String functionName) {
        super(nodeBuilder.getQueryEngine().getSqmFunctionRegistry().registerNamed(functionName),
                new FunctionExpression(functionName, path.size() + 1),
                parameters(referencedPathSource, nodeBuilder, path),
                null,
                null,
                StandardFunctionReturnTypeResolvers.useFirstNonNull(),
                nodeBuilder,
                functionName);
        this.referencedPathSourcePath = referencedPathSource;
        this.path = new ArrayList<>(path);
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Path argument can not be null or empty list");
        }
    }

    private static List<? extends SqmTypedNode<?>> parameters(Path referencedPathSource, NodeBuilder nodeBuilder, List<String> path) {
        List<SqmTypedNode<?>> result = new ArrayList<>();
        result.add((SqmBasicValuedSimplePath) referencedPathSource);
        result.addAll(path.stream().map(p -> nodeBuilder.literal(p)).collect(Collectors.toList()));
        return result;
    }

    @Override
    public T copy(SqmCopyContext context) {
        T existing = (T) context.getCopy(this);
        if (existing != null) {
            return existing;
        } else {
            T copy = context.registerCopy((T) this, generate(this.referencedPathSourcePath, this.nodeBuilder(), this.path));

            this.copyTo(copy, context);
            return copy;
        }
    }

    abstract protected T generate(Path referencedPathSourcePath, NodeBuilder nodeBuilder, List<String> path);
}