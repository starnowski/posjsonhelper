package com.github.starnowski.posjsonhelper.text.hibernate6.functions;

import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.tree.SqmTypedNode;
import org.hibernate.query.sqm.tree.expression.SqmExpression;

import java.util.List;

import static com.github.starnowski.posjsonhelper.core.Constants.TO_TSQUERY_FUNCTION_NAME;

/**
 * Type that extends {@link AbstractFunctionWithConfigurationAndTextQueryFunction}.
 * Implemented for HQL function defined by constant {@link com.github.starnowski.posjsonhelper.core.Constants#TO_TSQUERY_FUNCTION_NAME}
 */
public class ToTSQueryFunction extends AbstractFunctionWithConfigurationAndTextQueryFunction {
    /**
     * @param nodeBuilder   node builder {@link NodeBuilder}
     * @param configuration text search configuration name
     * @param query         text search query
     */
    public ToTSQueryFunction(NodeBuilder nodeBuilder, String configuration, String query) {
        super(nodeBuilder, configuration, query, TO_TSQUERY_FUNCTION_NAME);
    }

    /**
     * @param nodeBuilder   node builder {@link NodeBuilder}
     * @param configuration text search configuration name
     * @param query         text search query
     */
    public ToTSQueryFunction(NodeBuilder nodeBuilder, SqmExpression<?> configuration, String query) {
        super(nodeBuilder, configuration, query, TO_TSQUERY_FUNCTION_NAME);
    }

    /**
     * @param parameters   function parameters
     * @param nodeBuilder node builder {@link NodeBuilder}
     */
    public ToTSQueryFunction(List<? extends SqmTypedNode<?>> parameters, NodeBuilder nodeBuilder) {
        super(parameters, nodeBuilder, TO_TSQUERY_FUNCTION_NAME);
    }
}
