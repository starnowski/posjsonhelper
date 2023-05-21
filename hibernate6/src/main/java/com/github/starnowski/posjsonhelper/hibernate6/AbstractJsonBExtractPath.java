package com.github.starnowski.posjsonhelper.hibernate6;

import jakarta.persistence.criteria.Expression;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.SqmPathSource;
import org.hibernate.query.sqm.tree.expression.AbstractSqmExpression;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractJsonBExtractPath
//{}
extends AbstractSqmExpression<String> implements Serializable {

    private final List<String> path;
    private final List<Expression> pathValues;

    public AbstractJsonBExtractPath( SqmPathSource<String> referencedPathSource, NodeBuilder nodeBuilder, List<String> path, String functionName) {
        super(referencedPathSource, nodeBuilder);
        this.path = path;
        if (this.path == null || this.path.isEmpty()) {
            throw new IllegalArgumentException("Path argument can not be null or empty list");
        }
        this.pathValues = this.path.stream().map(p -> nodeBuilder.literal(p)).collect(Collectors.toList());
    }

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