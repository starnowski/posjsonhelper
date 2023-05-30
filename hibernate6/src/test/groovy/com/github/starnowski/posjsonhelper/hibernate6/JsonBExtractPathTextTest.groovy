package com.github.starnowski.posjsonhelper.hibernate6

import jakarta.persistence.criteria.Path
import org.hibernate.query.sqm.NodeBuilder
import spock.lang.Specification

import static com.github.starnowski.posjsonhelper.core.Constants.JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME

class JsonBExtractPathTextTest extends AbstractJsonBExtractPathTest<JsonBExtractPathText> {
    @Override
    protected JsonBExtractPathText prepareTestObject(Path referencedPathSource, NodeBuilder nodeBuilder, List<String> path) {
        new JsonBExtractPathText(referencedPathSource, path, nodeBuilder)
    }

    @Override
    protected String expectedFunctionName() {
        JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME
    }
}
