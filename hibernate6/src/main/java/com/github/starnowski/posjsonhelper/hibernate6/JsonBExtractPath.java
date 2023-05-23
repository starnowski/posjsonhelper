package com.github.starnowski.posjsonhelper.hibernate6;

import jakarta.persistence.criteria.Path;
import org.hibernate.query.sqm.NodeBuilder;

import java.util.List;

import static com.github.starnowski.posjsonhelper.core.Constants.JSONB_EXTRACT_PATH_FUNCTION_NAME;

public class JsonBExtractPath extends AbstractJsonBExtractPath<JsonBExtractPath> {
    public JsonBExtractPath(Path referencedPathSource, NodeBuilder nodeBuilder, List<String> path) {
        super(referencedPathSource, nodeBuilder, path, JSONB_EXTRACT_PATH_FUNCTION_NAME);
    }
}
