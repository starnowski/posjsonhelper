package com.github.starnowski.posjsonhelper.text.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.text.hibernate6.functions.PhraseToTSQueryFunction;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.tree.SqmTypedNode;

import java.util.List;

import static com.github.starnowski.posjsonhelper.core.Constants.PHRASETO_TSQUERY_FUNCTION_NAME;

public class PhraseToTSQueryFunctionDescriptor extends AbstractFunctionWithConfigurationAndTextQueryFunctionDescriptor<PhraseToTSQueryFunction> {
    public PhraseToTSQueryFunctionDescriptor() {
        super(PHRASETO_TSQUERY_FUNCTION_NAME);
    }

    @Override
    protected PhraseToTSQueryFunction generateAbstractFunctionWithConfigurationAndTextQueryFunctionImpl(List<? extends SqmTypedNode<?>> arguments, NodeBuilder nodeBuilder) {
        return new PhraseToTSQueryFunction(arguments, nodeBuilder);
    }
}