package com.github.starnowski.posjsonhelper.hibernate6.functions;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.operators.CastOperatorFunction;
import com.github.starnowski.posjsonhelper.hibernate6.operators.JsonbCastOperatorFunction;
import jakarta.persistence.criteria.Path;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.FunctionRenderer;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.produce.function.StandardFunctionReturnTypeResolvers;
import org.hibernate.query.sqm.tree.SqmTypedNode;
import org.hibernate.query.sqm.tree.expression.SqmExpression;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.github.starnowski.posjsonhelper.core.Constants.JSONB_SET_FUNCTION_NAME;

/**
 * Implemented of HQL function defined by constant {@link com.github.starnowski.posjsonhelper.core.Constants#JSONB_SET_FUNCTION_NAME}
 */
public class JsonbSetFunction extends SelfRenderingSqmFunction<String> implements Serializable {

    /**
     *
     * @param nodeBuilder component of type {@link NodeBuilder}
     * @param referencedPathSource path for property that represent JSON or String type. Property has to implement {@link SqmTypedNode}
     * @param jsonPath value for a text array that represents the JSON path for an element that is supposed to be set. For example "{parent,child,property}"
     * @param json json value that should be set
     * @param hibernateContext object of type {@link HibernateContext}
     */
    public JsonbSetFunction(NodeBuilder nodeBuilder, Path referencedPathSource, String jsonPath, String json, HibernateContext hibernateContext) {
        this(nodeBuilder, (SqmTypedNode) referencedPathSource, generateCastedJsonPathToTextArray(nodeBuilder, jsonPath, hibernateContext), new JsonbCastOperatorFunction(nodeBuilder, json, hibernateContext));
    }

    /**
     *
     * @param nodeBuilder component of type {@link NodeBuilder}
     * @param referencedPathSource path for property that represent JSON or String type
     * @param jsonPath value for a text array that represents the JSON path for an element that is supposed to be set. For example "{parent,child,property}"
     * @param json json value that should be set
     * @param hibernateContext object of type {@link HibernateContext}
     */
    public JsonbSetFunction(NodeBuilder nodeBuilder, SqmTypedNode referencedPathSource, String jsonPath, String json, HibernateContext hibernateContext) {
        this(nodeBuilder, referencedPathSource, generateCastedJsonPathToTextArray(nodeBuilder, jsonPath, hibernateContext), new JsonbCastOperatorFunction(nodeBuilder, json, hibernateContext));
    }

    /**
     *
     * @param nodeBuilder component of type {@link NodeBuilder}
     * @param referencedPathSource path for property that represent JSON or String type
     * @param jsonPath value for a text array that represents the JSON path for an element that is supposed to be set. For example "{parent,child,property}"
     * @param value son value that should be set
     */
    public JsonbSetFunction(NodeBuilder nodeBuilder, SqmTypedNode referencedPathSource, SqmTypedNode jsonPath, SqmTypedNode value) {
        super(
                nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(JSONB_SET_FUNCTION_NAME),
                (FunctionRenderer) nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(JSONB_SET_FUNCTION_NAME),
                mapParameters(nodeBuilder, referencedPathSource, jsonPath, value),
                null,
                null,
                StandardFunctionReturnTypeResolvers.useFirstNonNull(),
                nodeBuilder,
                JSONB_SET_FUNCTION_NAME);
    }

    private static CastOperatorFunction generateCastedJsonPathToTextArray(NodeBuilder nodeBuilder, String jsonPath, HibernateContext hibernateContext) {
        return new CastOperatorFunction(nodeBuilder, (SqmExpression<?>) nodeBuilder.value(jsonPath), "text[]", hibernateContext);
    }

    private static List<? extends SqmTypedNode<?>> mapParameters(NodeBuilder nodeBuilder, SqmTypedNode referencedPathSource, SqmTypedNode jsonPath, SqmTypedNode value) {
        List<SqmTypedNode<?>> result = new ArrayList<>();
        result.add((SqmTypedNode<?>) referencedPathSource);
        result.add((SqmTypedNode<?>) jsonPath);
        result.add((SqmTypedNode<String>) nodeBuilder.value(value));
        return result;
    }
}
