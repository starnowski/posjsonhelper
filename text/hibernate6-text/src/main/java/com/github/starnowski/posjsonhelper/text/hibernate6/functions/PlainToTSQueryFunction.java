package com.github.starnowski.posjsonhelper.text.hibernate6.functions;

import jakarta.persistence.criteria.Path;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.tree.SqmTypedNode;

import java.util.List;

import static com.github.starnowski.posjsonhelper.core.Constants.JSONB_EXTRACT_PATH_FUNCTION_NAME;
import static com.github.starnowski.posjsonhelper.core.Constants.PLAINTO_TSQUERY_FUNCTION_NAME;

public class PlainToTSQueryFunction extends AbstractFunctionWithConfigurationAndTextQueryFunction{
    public PlainToTSQueryFunction(NodeBuilder nodeBuilder, String configuration, String query) {
        super(nodeBuilder, configuration, query, PLAINTO_TSQUERY_FUNCTION_NAME);
    }

    public PlainToTSQueryFunction(List<? extends SqmTypedNode<?>> arguments, NodeBuilder nodeBuilder) {
        super(arguments, nodeBuilder, JSONB_EXTRACT_PATH_FUNCTION_NAME);
    }
}
