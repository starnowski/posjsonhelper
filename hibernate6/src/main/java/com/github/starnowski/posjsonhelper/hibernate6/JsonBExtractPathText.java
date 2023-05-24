package com.github.starnowski.posjsonhelper.hibernate6;

import jakarta.persistence.criteria.Path;
import org.hibernate.query.sqm.NodeBuilder;

import java.util.List;

import static com.github.starnowski.posjsonhelper.core.Constants.JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME;

public class JsonBExtractPathText extends AbstractJsonBExtractPath<JsonBExtractPathText> {
    public JsonBExtractPathText(Path referencedPathSource, List<String> path, NodeBuilder nodeBuilder) {
        super(referencedPathSource, nodeBuilder, path, JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME);
    }

}