package com.github.starnowski.posjsonhelper.text.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.text.hibernate6.functions.PlainToTSQueryFunction;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.tree.SqmTypedNode;

import java.util.List;

import static com.github.starnowski.posjsonhelper.core.Constants.PLAINTO_TSQUERY_FUNCTION_NAME;

public class PlainToTSQueryFunctionDescriptor extends AbstractFunctionWithConfigurationAndTextQueryFunctionDescriptor<PlainToTSQueryFunction>{
    public PlainToTSQueryFunctionDescriptor() {
        super(PLAINTO_TSQUERY_FUNCTION_NAME);
    }

    @Override
    protected PlainToTSQueryFunction generateAbstractFunctionWithConfigurationAndTextQueryFunctionImpl(List<? extends SqmTypedNode<?>> arguments, NodeBuilder nodeBuilder) {
        return new PlainToTSQueryFunction(arguments, nodeBuilder);
    }
}
