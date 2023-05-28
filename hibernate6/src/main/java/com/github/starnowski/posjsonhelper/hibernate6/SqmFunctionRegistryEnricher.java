package com.github.starnowski.posjsonhelper.hibernate6;

import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.core.CoreContextPropertiesSupplier;
import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.core.HibernateContextPropertiesSupplier;
import com.github.starnowski.posjsonhelper.hibernate6.descriptor.AbstractConditionalFunctionDescriptorRegister;
import com.github.starnowski.posjsonhelper.hibernate6.descriptor.FunctionByNameRegister;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

import java.util.List;

import static com.github.starnowski.posjsonhelper.core.Constants.JSONB_EXTRACT_PATH_FUNCTION_NAME;
import static com.github.starnowski.posjsonhelper.core.Constants.JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME;

public class SqmFunctionRegistryEnricher {

    /**
     * Supplier for {@link Context} object based on system properties
     */
    private final CoreContextPropertiesSupplier coreContextPropertiesSupplier;
    /**
     * Supplier for {@link HibernateContext} object based on system properties
     */
    private final HibernateContextPropertiesSupplier hibernateContextPropertiesSupplier;

    public SqmFunctionRegistryEnricher(){
        this(new CoreContextPropertiesSupplier(), new HibernateContextPropertiesSupplier());
    }

    public SqmFunctionRegistryEnricher(CoreContextPropertiesSupplier coreContextPropertiesSupplier, HibernateContextPropertiesSupplier hibernateContextPropertiesSupplier) {
        this.coreContextPropertiesSupplier = coreContextPropertiesSupplier;
        this.hibernateContextPropertiesSupplier = hibernateContextPropertiesSupplier;
    }

    interface FunctionDescriptorRegisterSupplier {

        AbstractConditionalFunctionDescriptorRegister get(Context context, HibernateContext hibernateContext);
    }

    private final List<FunctionDescriptorRegisterSupplier> functionDescriptorRegisterSuppliers = List.of(
            (context, hibernateContext) ->
                    new FunctionByNameRegister(JSONB_EXTRACT_PATH_FUNCTION_NAME, JSONB_EXTRACT_PATH_FUNCTION_NAME, true),
            (context, hibernateContext) ->
                    new FunctionByNameRegister(JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME, JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME, true)
    );

    public void enrich(SqmFunctionRegistry sqmFunctionRegistry) {
        Context context = coreContextPropertiesSupplier.get();
        HibernateContext hibernateContext = hibernateContextPropertiesSupplier.get();
        functionDescriptorRegisterSuppliers.stream().map(supplier -> supplier.get(context, hibernateContext)).forEach(functionDescriptorRegister ->
                functionDescriptorRegister.registerFunction(sqmFunctionRegistry));
    }
}
