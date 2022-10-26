package com.github.starnowski.posjsonhelper.hibernate5.predicates;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate5.JsonBExtractPath;
import org.hibernate.query.criteria.internal.*;
import org.hibernate.query.criteria.internal.compile.RenderingContext;
import org.hibernate.query.criteria.internal.expression.LiteralExpression;
import org.hibernate.query.criteria.internal.expression.UnaryOperatorExpression;
import org.hibernate.query.criteria.internal.predicate.AbstractSimplePredicate;

import javax.persistence.criteria.Expression;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractJsonbArrayStringsExistPredicate extends AbstractSimplePredicate implements UnaryOperatorExpression<Boolean>, Serializable {

    private final HibernateContext context;
    private final JsonBExtractPath jsonBExtractPath;
    private final List<Expression> values;

    public AbstractJsonbArrayStringsExistPredicate(HibernateContext context, CriteriaBuilderImpl criteriaBuilder, JsonBExtractPath jsonBExtractPath, String[] values) {
        super(criteriaBuilder);
        this.jsonBExtractPath = jsonBExtractPath;
        this.context = context;
        Class javaType = jsonBExtractPath.getJavaType();
        ValueHandlerFactory.ValueHandler valueHandler = javaType != null && ValueHandlerFactory.isNumeric(javaType) ? ValueHandlerFactory.determineAppropriateHandler(javaType) : new ValueHandlerFactory.NoOpValueHandler();
        Iterator var6 = Arrays.asList(values).iterator();
        this.values = new ArrayList(values.length);
        while (var6.hasNext()) {
            Object value = var6.next();
            this.values.add(new LiteralExpression(criteriaBuilder, valueHandler.convert(value)));
        }
    }

    @Override
    public void registerParameters(ParameterRegistry registry) {
        Iterator var2 = this.values.iterator();

        while (var2.hasNext()) {
            Expression value = (Expression) var2.next();
            ParameterContainer.Helper.possibleParameter(value, registry);
        }
    }

    private String renderValues(RenderingContext renderingContext) {
        StringBuilder sb = new StringBuilder();
        //TODO get json_function_json_array function name from context
        sb.append("json_function_json_array(");

        String sep = "";
        for (Iterator var11 = this.values.iterator(); var11.hasNext(); sep = ", ") {
            Expression value = (Expression) var11.next();
            sb.append(sep).append(((Renderable) value).render(renderingContext));
        }

        sb.append(")");
        return sb.toString();
    }

    @Override
    public Expression<?> getOperand() {
        return this.jsonBExtractPath;
    }

    @Override
    public String render(boolean isNegated, RenderingContext renderingContext) {
        return getFunctionName() + "( " + this.jsonBExtractPath.render(renderingContext) + " , " + " " + renderValues(renderingContext) + ") = " + (isNegated ? "FALSE" : "TRUE");
    }

    abstract protected String getFunctionName();

    protected HibernateContext getContext() {
        return context;
    }

    protected JsonBExtractPath getJsonBExtractPath() {
        return jsonBExtractPath;
    }

    protected List<Expression> getValues() {
        return values;
    }
}
