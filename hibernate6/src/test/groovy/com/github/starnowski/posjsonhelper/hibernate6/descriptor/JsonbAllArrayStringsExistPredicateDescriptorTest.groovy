package com.github.starnowski.posjsonhelper.hibernate6.descriptor

import com.github.starnowski.posjsonhelper.core.Context
import com.github.starnowski.posjsonhelper.core.HibernateContext

class JsonbAllArrayStringsExistPredicateDescriptorTest extends AbstractJsonbArrayStringsExistPredicateDescriptorTest<JsonbAllArrayStringsExistPredicateDescriptor> {
    @Override
    protected JsonbAllArrayStringsExistPredicateDescriptor generateTestedObject(HibernateContext hibernateContext) {
        new JsonbAllArrayStringsExistPredicateDescriptor(Context.builder().build(), hibernateContext)
    }
}
