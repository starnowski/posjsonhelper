package com.github.starnowski.posjsonhelper.hibernate5

import javax.persistence.criteria.Expression

class JsonBExtractPathTextTest extends AbstractJsonBExtractPathTest<JsonBExtractPathText> {

    @Override
    protected JsonBExtractPathText getTested(List<String> path, Expression<?> operand) {
        new JsonBExtractPathText(null, path, operand)
    }

    @Override
    protected String getFunctionName() {
        "jsonb_extract_path_text"
    }

}
