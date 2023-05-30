package com.github.starnowski.posjsonhelper.hibernate6

import jakarta.persistence.criteria.Path
import org.hibernate.query.sqm.NodeBuilder
import spock.lang.Specification

import static com.github.starnowski.posjsonhelper.core.Constants.JSONB_EXTRACT_PATH_FUNCTION_NAME

class JsonBExtractPathTest extends AbstractJsonBExtractPathTest<JsonBExtractPath> {

    @Override
    protected JsonBExtractPath prepareTestObject(Path referencedPathSource, NodeBuilder nodeBuilder, List<String> path) {
        new JsonBExtractPath(referencedPathSource, nodeBuilder, path)
    }

    @Override
    protected String expectedFunctionName() {
        JSONB_EXTRACT_PATH_FUNCTION_NAME
    }
}
