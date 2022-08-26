package com.github.starnowski.posjsonhelper.poc;


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

/**
 * TODO Check implementation for org.hibernate.jpa.criteria.predicate.InPredicate
 */
public class JSONBComparisonPredicate extends AbstractSimplePredicate implements UnaryOperatorExpression<Boolean>, Serializable {

    private final String jsonPath;
    private final ComparisonOperator comparisonOperator;
    private final Expression<?> operand;
    private final List<Expression> values;

    public JSONBComparisonPredicate(CriteriaBuilderImpl criteriaBuilder, String jsonPath, ComparisonOperator comparisonOperator, Expression<?> operand, String[] values) {
        super(criteriaBuilder);
        this.jsonPath = jsonPath;
        this.comparisonOperator = comparisonOperator;
        this.operand = operand;
        Class javaType = operand.getJavaType();
        ValueHandlerFactory.ValueHandler valueHandler = javaType != null && ValueHandlerFactory.isNumeric(javaType) ? ValueHandlerFactory.determineAppropriateHandler(javaType) : new ValueHandlerFactory.NoOpValueHandler();
        Iterator var6 = Arrays.asList(values).iterator();
        this.values = new ArrayList(values.length);
        while (var6.hasNext()) {
            Object value = var6.next();
            this.values.add(new LiteralExpression(criteriaBuilder, ((ValueHandlerFactory.ValueHandler) valueHandler).convert(value)));
        }
    }

    @Override
    public void registerParameters(ParameterRegistry registry) {
        Helper.possibleParameter(this.getOperand(), registry);
    }

    @Override
    public String render(boolean isNegated, RenderingContext renderingContext) {
        return this.comparisonOperator.rendered() + "(" + json_function_get_json_element(renderingContext) + " , " + " " + renderValues(renderingContext) + ") = TRUE";
    }

    private String json_function_get_json_element(RenderingContext renderingContext) {
        return "json_function_get_json_element( " + ((Renderable) this.getOperand()).render(renderingContext) + " , '" + jsonPath + "' )";
    }

    @Override
    public Expression<?> getOperand() {
        return operand;
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

    public static enum ComparisonOperator {
        ALL_STRINGS_AT_TOP_LEVEL {
            public String rendered() {
                return "json_function_all_strings_at_top_level";
            }
        };

        public abstract String rendered();
    }
}
