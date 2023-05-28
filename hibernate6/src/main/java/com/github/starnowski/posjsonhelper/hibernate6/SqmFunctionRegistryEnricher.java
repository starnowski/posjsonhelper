package com.github.starnowski.posjsonhelper.hibernate6;

import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.descriptor.AbstractConditionalFunctionDescriptorRegister;
import com.github.starnowski.posjsonhelper.hibernate6.descriptor.FunctionByNameRegister;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

import java.util.Arrays;
import java.util.List;

import static com.github.starnowski.posjsonhelper.core.Constants.JSONB_EXTRACT_PATH_FUNCTION_NAME;
import static com.github.starnowski.posjsonhelper.core.Constants.JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME;

public class SqmFunctionRegistryEnricher {

    interface FunctionDescriptorRegisterSupplier {

        AbstractConditionalFunctionDescriptorRegister get(Context context, HibernateContext hibernateContext);
    }

    List<FunctionDescriptorRegisterSupplier> functionDescriptorRegisters = List.of(
            (context, hibernateContext) ->
                    new FunctionByNameRegister(JSONB_EXTRACT_PATH_FUNCTION_NAME, JSONB_EXTRACT_PATH_FUNCTION_NAME, true),
            (context, hibernateContext) ->
                    new FunctionByNameRegister(JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME, JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME, true)
    );

    public void enrich(SqmFunctionRegistry sqmFunctionRegistry) {
//        sqmFunctionRegistry
    }
}
