package com.github.starnowski.posjsonhelper.hibernate5


import javax.persistence.criteria.Expression

class JsonBExtractPathTest extends AbstractJsonBExtractPathTest<JsonBExtractPath> {
    @Override
    protected JsonBExtractPath getTested(List<String> path, Expression<?> operand) {
        new JsonBExtractPath(null, path, operand)
    }

    @Override
    protected String getFunctionName() {
        "jsonb_extract_path"
    }
}
