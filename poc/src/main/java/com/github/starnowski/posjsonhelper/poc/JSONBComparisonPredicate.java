package com.github.starnowski.posjsonhelper.poc;

import org.hibernate.jpa.criteria.CriteriaBuilderImpl;
import org.hibernate.jpa.criteria.ParameterRegistry;
import org.hibernate.jpa.criteria.Renderable;
import org.hibernate.jpa.criteria.compile.RenderingContext;
import org.hibernate.jpa.criteria.expression.UnaryOperatorExpression;
import org.hibernate.jpa.criteria.predicate.AbstractSimplePredicate;

import javax.persistence.criteria.Expression;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

/**
 * TODO Check implementation for org.hibernate.jpa.criteria.predicate.InPredicate
 */
public class JSONBComparisonPredicate extends AbstractSimplePredicate implements UnaryOperatorExpression<Boolean>, Serializable {

    private final String jsonPath;
    private final JSONBComparisonPredicate.ComparisonOperator comparisonOperator;
    private final Expression<?> operand;
    private final String[] values;

    public JSONBComparisonPredicate(CriteriaBuilderImpl criteriaBuilder, String jsonPath, ComparisonOperator comparisonOperator, Expression<?> operand, String[] values) {
        super(criteriaBuilder);
        this.jsonPath = jsonPath;
        this.comparisonOperator = comparisonOperator;
        this.operand = operand;
        this.values = values;
    }

    @Override
    public void registerParameters(ParameterRegistry registry) {
        Helper.possibleParameter(this.getOperand(), registry);
    }

    @Override
    public String render(boolean isNegated, RenderingContext renderingContext) {
//        return ((Renderable) this.getOperand()).render(renderingContext) + jsonPath + " " + this.comparisonOperator.rendered() + " array[" + renderValues() + "]";
        return this.comparisonOperator.rendered() + "(" + json_function_get_json_element(renderingContext) + " , " + " " + renderValues() + ") = TRUE";
//        return "json_all( " + ((Renderable) this.getOperand()).render(renderingContext) + ") = TRUE";
    }

    private String json_function_get_json_element(RenderingContext renderingContext)
    {
        return "json_function_get_json_element( " + ((Renderable) this.getOperand()).render(renderingContext) + " , '" + jsonPath + "' )";
//        return "json_function_get_json_element(" + ((Renderable) this.getOperand()).render(renderingContext) + " )";
    }

    @Override
    public Expression<?> getOperand() {
        return operand;
    }

    private String renderValues()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("json_function_json_array(");
        Iterator<String> it = Arrays.asList(values).iterator();
        while (it.hasNext())
        {
            String authour = it.next();
            sb.append("'");
            sb.append(authour);
            sb.append("'");
            if (it.hasNext())
            {
                sb.append(",");
            }
        }
        sb.append(")");
        //TODO Sql escape
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
