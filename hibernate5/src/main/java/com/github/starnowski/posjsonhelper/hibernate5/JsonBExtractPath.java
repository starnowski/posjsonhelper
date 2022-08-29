package com.github.starnowski.posjsonhelper.hibernate5;

import com.github.starnowski.posjsonhelper.core.Context;
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.hibernate.query.criteria.internal.Renderable;
import org.hibernate.query.criteria.internal.ValueHandlerFactory;
import org.hibernate.query.criteria.internal.compile.RenderingContext;
import org.hibernate.query.criteria.internal.expression.LiteralExpression;
import org.hibernate.query.criteria.internal.expression.function.BasicFunctionExpression;

import javax.persistence.criteria.Expression;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

public class JsonBExtractPath extends BasicFunctionExpression<String> implements Serializable {

    private final List<String> path;
    private final List<Expression> pathValues;
    private final Expression<?> operand;

    public JsonBExtractPath(CriteriaBuilderImpl criteriaBuilder, List<String> path, Expression<?> operand) {
        super(criteriaBuilder, String.class, "jsonb_extract_path");
        this.path = path;
        this.operand = operand;
        this.pathValues = getPath().stream().map(p -> new LiteralExpression(criteriaBuilder, p)).collect(Collectors.toList());
    }

    public List<String> getPath() {
        return this.path == null ? emptyList() : new ArrayList<>(this.path);
    }

    public Expression<?> getOperand() {
        return operand;
    }

    public Class getJavaType() {
        return operand.getJavaType();
    }

    public String render(RenderingContext renderingContext) {
        renderingContext.getFunctionStack().push(this);
        String var3;
        try {
            var3 = "jsonb_extract_path( " + ((Renderable) this.getOperand()).render(renderingContext) + " , " + renderJsonPath(renderingContext) + " )";
        } finally {
            renderingContext.getFunctionStack().pop();
        }
        return var3;
    }

    private String renderJsonPath(RenderingContext renderingContext) {
        StringBuilder sb = new StringBuilder();
        String sep = "";
        for (Iterator var11 = pathValues.iterator(); var11.hasNext(); sep = ", ") {
            Expression value = (Expression) var11.next();
            sb.append(sep).append(((Renderable) value).render(renderingContext));
        }
        return sb.toString();
    }
}
