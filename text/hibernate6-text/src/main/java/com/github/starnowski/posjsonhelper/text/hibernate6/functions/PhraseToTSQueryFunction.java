package com.github.starnowski.posjsonhelper.text.hibernate6.functions;

import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.tree.SqmTypedNode;

import java.util.List;

import static com.github.starnowski.posjsonhelper.core.Constants.PHRASETO_TSQUERY_FUNCTION_NAME;

public class PhraseToTSQueryFunction extends AbstractFunctionWithConfigurationAndTextQueryFunction {
    public PhraseToTSQueryFunction(NodeBuilder nodeBuilder, String configuration, String query) {
        super(nodeBuilder, configuration, query, PHRASETO_TSQUERY_FUNCTION_NAME);
    }

    public PhraseToTSQueryFunction(List<? extends SqmTypedNode<?>> arguments, NodeBuilder nodeBuilder) {
        super(arguments, nodeBuilder, PHRASETO_TSQUERY_FUNCTION_NAME);
    }
}
