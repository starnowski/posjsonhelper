package com.github.starnowski.posjsonhelper.text.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.text.hibernate6.functions.WebsearchToTSQueryFunction;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.tree.SqmTypedNode;

import java.util.List;

import static com.github.starnowski.posjsonhelper.core.Constants.WEBSEARCH_TO_TSQUERY_FUNCTION_NAME;

public class WebsearchToTSQueryFunctionDescriptor extends AbstractFunctionWithConfigurationAndTextQueryFunctionDescriptor<WebsearchToTSQueryFunction> {
    public WebsearchToTSQueryFunctionDescriptor() {
        super(WEBSEARCH_TO_TSQUERY_FUNCTION_NAME);
    }

    @Override
    protected WebsearchToTSQueryFunction generateAbstractFunctionWithConfigurationAndTextQueryFunctionImpl(List<? extends SqmTypedNode<?>> arguments, NodeBuilder nodeBuilder) {
        return new WebsearchToTSQueryFunction(arguments, nodeBuilder);
    }
}
