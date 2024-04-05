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

public class JsonbSetFunction extends SelfRenderingSqmFunction<String> implements Serializable {

    public JsonbSetFunction(NodeBuilder nodeBuilder, Path referencedPathSource, String jsonPath, String json, HibernateContext hibernateContext) {
        this(nodeBuilder, (SqmTypedNode) referencedPathSource, generateCastedJsonPathToTextArray(nodeBuilder, jsonPath, hibernateContext), new JsonbCastOperatorFunction(nodeBuilder, json, hibernateContext), hibernateContext);
    }

    public JsonbSetFunction(NodeBuilder nodeBuilder, SqmTypedNode referencedPathSource, String jsonPath, String json, HibernateContext hibernateContext) {
        this(nodeBuilder, referencedPathSource, generateCastedJsonPathToTextArray(nodeBuilder, jsonPath, hibernateContext), new JsonbCastOperatorFunction(nodeBuilder, json, hibernateContext), hibernateContext);
    }

    public JsonbSetFunction(NodeBuilder nodeBuilder, SqmTypedNode referencedPathSource, SqmTypedNode jsonPath, SqmTypedNode value, HibernateContext hibernateContext) {
        super(
//                nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(hibernateContext.getJsonFunctionJsonArrayOperator()), //TODO
                nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor("jsonb_set"), //TODO
//                (FunctionRenderer) nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(hibernateContext.getJsonFunctionJsonArrayOperator()), //TODO
                (FunctionRenderer) nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor("jsonb_set"), //TODO
                mapParameters(nodeBuilder, referencedPathSource, jsonPath, value),
                null,
                null,
                StandardFunctionReturnTypeResolvers.useFirstNonNull(),
                nodeBuilder,
                "jsonb_set");
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
