package com.github.starnowski.posjsonhelper.hibernate6.descriptor

import com.github.starnowski.posjsonhelper.core.Context
import com.github.starnowski.posjsonhelper.core.HibernateContext

class JsonbAnyArrayStringsExistPredicateDescriptorTest extends AbstractJsonbArrayStringsExistPredicateDescriptorTest<JsonbAnyArrayStringsExistPredicateDescriptor> {
    @Override
    protected JsonbAnyArrayStringsExistPredicateDescriptor generateTestedObject(HibernateContext hibernateContext) {
        new JsonbAnyArrayStringsExistPredicateDescriptor(Context.builder().build(), hibernateContext)
    }
}
