package com.github.starnowski.posjsonhelper.text.hibernate6.functions;

import jakarta.persistence.criteria.Path;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.FunctionRenderingSupport;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.produce.function.StandardFunctionReturnTypeResolvers;
import org.hibernate.query.sqm.tree.SqmTypedNode;
import org.hibernate.type.StandardBasicTypes;

import java.io.Serializable;
import java.util.Arrays;

import static com.github.starnowski.posjsonhelper.core.Constants.TO_TSVECTOR_FUNCTION_NAME;

public class TSVectorFunction extends SelfRenderingSqmFunction<String> implements Serializable {
    public TSVectorFunction(Path referencedPathSource, NodeBuilder nodeBuilder) {
        super(nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(TO_TSVECTOR_FUNCTION_NAME),
                (FunctionRenderingSupport) nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(TO_TSVECTOR_FUNCTION_NAME),
                //TODO Add configuration
                Arrays.asList((SqmTypedNode<?>) referencedPathSource),
                null,
                null,
                StandardFunctionReturnTypeResolvers.invariant(nodeBuilder.getTypeConfiguration().getBasicTypeRegistry().resolve(StandardBasicTypes.STRING)),
                nodeBuilder,
                TO_TSVECTOR_FUNCTION_NAME);
    }
}
