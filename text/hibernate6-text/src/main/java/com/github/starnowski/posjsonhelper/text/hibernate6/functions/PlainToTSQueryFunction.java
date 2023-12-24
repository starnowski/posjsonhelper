package com.github.starnowski.posjsonhelper.text.hibernate6.functions;

import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.tree.SqmTypedNode;
import org.hibernate.query.sqm.tree.expression.SqmExpression;

import java.util.List;

import static com.github.starnowski.posjsonhelper.core.Constants.PLAINTO_TSQUERY_FUNCTION_NAME;

public class PlainToTSQueryFunction extends AbstractFunctionWithConfigurationAndTextQueryFunction {
    public PlainToTSQueryFunction(NodeBuilder nodeBuilder, String configuration, String query) {
        super(nodeBuilder, configuration, query, PLAINTO_TSQUERY_FUNCTION_NAME);
    }

    public PlainToTSQueryFunction(NodeBuilder nodeBuilder, SqmExpression<?> configuration, String query) {
        super(nodeBuilder, configuration, query, PLAINTO_TSQUERY_FUNCTION_NAME);
    }

    public PlainToTSQueryFunction(List<? extends SqmTypedNode<?>> arguments, NodeBuilder nodeBuilder) {
        super(arguments, nodeBuilder, PLAINTO_TSQUERY_FUNCTION_NAME);
    }
}
