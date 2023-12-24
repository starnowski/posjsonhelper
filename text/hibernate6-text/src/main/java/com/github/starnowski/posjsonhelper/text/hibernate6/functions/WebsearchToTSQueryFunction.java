package com.github.starnowski.posjsonhelper.text.hibernate6.functions;

import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.tree.SqmTypedNode;
import org.hibernate.query.sqm.tree.expression.SqmExpression;

import java.util.List;

import static com.github.starnowski.posjsonhelper.core.Constants.WEBSEARCH_TO_TSQUERY_FUNCTION_NAME;

public class WebsearchToTSQueryFunction extends AbstractFunctionWithConfigurationAndTextQueryFunction {
    public WebsearchToTSQueryFunction(NodeBuilder nodeBuilder, String configuration, String query) {
        super(nodeBuilder, configuration, query, WEBSEARCH_TO_TSQUERY_FUNCTION_NAME);
    }

    public WebsearchToTSQueryFunction(NodeBuilder nodeBuilder, SqmExpression<?> configuration, String query) {
        super(nodeBuilder, configuration, query, WEBSEARCH_TO_TSQUERY_FUNCTION_NAME);
    }

    public WebsearchToTSQueryFunction(List<? extends SqmTypedNode<?>> arguments, NodeBuilder nodeBuilder) {
        super(arguments, nodeBuilder, WEBSEARCH_TO_TSQUERY_FUNCTION_NAME);
    }
}