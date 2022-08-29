package com.github.starnowski.posjsonhelper.hibernate5.predicates;

import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.hibernate5.JsonBExtractPath;
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.hibernate.query.criteria.internal.ParameterRegistry;
import org.hibernate.query.criteria.internal.Renderable;
import org.hibernate.query.criteria.internal.ValueHandlerFactory;
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

public class JsonbAllArrayStringsExistPredicate extends AbstractSimplePredicate implements UnaryOperatorExpression<Boolean>, Serializable {

    private final Context context;
    private final JsonBExtractPath jsonBExtractPath;
    private final List<Expression> values;

    public JsonbAllArrayStringsExistPredicate(Context context, CriteriaBuilderImpl criteriaBuilder, JsonBExtractPath jsonBExtractPath, String[] values) {
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
            Helper.possibleParameter(value, registry);
        }
    }

    private String renderValues(RenderingContext renderingContext) {
        StringBuilder sb = new StringBuilder();
        sb.append("json_function_json_array(");
        Iterator it = Arrays.asList(values).iterator();

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
        return this.context.getJsonbAllArrayStringsExistFunctionReference() + "( " + this.jsonBExtractPath.render(renderingContext) + " , " + " " + renderValues(renderingContext) + ") = " + (isNegated ? "FALSE" : "TRUE");
    }
}