package com.github.starnowski.posjsonhelper.hibernate6;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import org.hibernate.metamodel.mapping.ordering.ast.FunctionExpression;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.SemanticQueryWalker;
import org.hibernate.query.sqm.SqmPathSource;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.produce.function.StandardFunctionReturnTypeResolvers;
import org.hibernate.query.sqm.tree.SqmCopyContext;
import org.hibernate.query.sqm.tree.SqmTypedNode;
import org.hibernate.query.sqm.tree.domain.SqmBasicValuedSimplePath;
import org.hibernate.query.sqm.tree.domain.SqmPath;
import org.hibernate.query.sqm.tree.expression.AbstractSqmExpression;
import org.hibernate.query.sqm.tree.expression.SqmCoalesce;
import org.hibernate.query.sqm.tree.expression.SqmExpression;
import org.hibernate.query.sqm.tree.expression.SqmFunction;
import org.hibernate.spi.NavigablePath;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.starnowski.posjsonhelper.core.Constants.JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME;

public abstract class AbstractJsonBExtractPath<T extends AbstractJsonBExtractPath>
//{}
extends SelfRenderingSqmFunction<String> implements Serializable {

    private final String functionName;
    private final Path referencedPathSourcePath;
    private final SqmPathSource<String> referencedPathSource;
    private final List<String> path;
    private final List<SqmExpression> pathValues;

    public AbstractJsonBExtractPath(Path referencedPathSource, NodeBuilder nodeBuilder, List<String> path, String functionName) {
        super(nodeBuilder.getQueryEngine().getSqmFunctionRegistry().registerNamed(functionName),
                new FunctionExpression(functionName, path.size() + 1),
                parameters(referencedPathSource, nodeBuilder, path),
                null,
                null,
                StandardFunctionReturnTypeResolvers.useFirstNonNull(),
                nodeBuilder,
                functionName);
        this.functionName = functionName;
        this.referencedPathSourcePath = referencedPathSource;
        this.referencedPathSource = (SqmPathSource<String>) referencedPathSourcePath.getModel();
        this.path = new ArrayList<>(path);
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Path argument can not be null or empty list");
        }
        this.pathValues = path.stream().map(p -> nodeBuilder.literal(p)).collect(Collectors.toList());
    }

    private static List<? extends SqmTypedNode<?>> parameters(Path referencedPathSource, NodeBuilder nodeBuilder, List<String> path)
    {
        List<SqmTypedNode<?>> result = new ArrayList<>();
        result.add((SqmBasicValuedSimplePath)referencedPathSource);
        result.addAll(path.stream().map(p -> nodeBuilder.literal(p)).collect(Collectors.toList()));
        return result;
    }

//    @Override
//    public void appendHqlString(StringBuilder sb) {
//        sb.append(functionName + "(");
//        ((SqmExpression)this.pathValues.get(0)).appendHqlString(sb);
//
//        for(int i = 1; i < this.pathValues.size(); ++i) {
//            sb.append(", ");
//            ((SqmExpression)this.pathValues.get(i)).appendHqlString(sb);
//        }
//
//        sb.append(')');
//    }

//    @Override
//    public <X> X accept(SemanticQueryWalker<X> semanticQueryWalker) {
//        this.pathValues.forEach((e) -> {
//            e.accept(semanticQueryWalker);
//        });
//        return (X) this;
//    }

    @Override
    public T copy(SqmCopyContext context) {
        T existing = (T)context.getCopy(this);
        if (existing != null) {
            return existing;
        } else {
//            T coalesce = (SqmCoalesce)context.registerCopy(this, new SqmCoalesce(this.getNodeType(), this.arguments.size(), this.nodeBuilder()));
            T copy = context.registerCopy((T)this, generate(this.referencedPathSourcePath, this.nodeBuilder(), this.path));

            this.copyTo(copy, context);
            return copy;
        }
    }

    abstract protected T generate(Path referencedPathSourcePath, NodeBuilder nodeBuilder, List<String> path);

//    public SqmPath<?> resolvePathPart(String name, boolean isTerminal, SqmCreationState creationState) {
//        SqmPath<?> sqmPath = this.get(name);
//        ((SqmCreationProcessingState)creationState.getProcessingStateStack().getCurrent()).getPathRegistry().register(sqmPath);
//        return sqmPath;
//    }

//    public String render(RenderingContext renderingContext) {
//        renderingContext.getFunctionStack().push(this);
//        String var3;
//        try {
//            //TODO Checkin path can be empty (or null) from Postgres perspective
//            var3 = getFunctionName() + "( " + ((Renderable) this.getOperand()).render(renderingContext) + " , " + renderJsonPath(renderingContext) + " )";
//        } finally {
//            renderingContext.getFunctionStack().pop();
//        }
//        return var3;
//    }
//
//    private String renderJsonPath(RenderingContext renderingContext) {
//        StringBuilder sb = new StringBuilder();
//        String sep = "";
//        for (Iterator var11 = pathValues.iterator(); var11.hasNext(); sep = ", ") {
//            Expression value = (Expression) var11.next();
//            sb.append(sep).append(((Renderable) value).render(renderingContext));
//        }
//        return sb.toString();
//    }
}