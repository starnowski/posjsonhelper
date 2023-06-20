package com.github.starnowski.posjsonhelper.hibernate6.descriptor

import spock.lang.Specification

class JsonBExtractPathTextDescriptorTest extends AbstractJsonBExtractPathDescriptorTest<JsonBExtractPathTextDescriptor> {
    @Override
    protected JsonBExtractPathTextDescriptor generateTestedObject() {
        new JsonBExtractPathTextDescriptor()
    }
}
