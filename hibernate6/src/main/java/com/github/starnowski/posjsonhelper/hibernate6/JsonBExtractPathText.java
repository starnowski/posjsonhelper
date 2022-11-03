package com.github.starnowski.posjsonhelper.hibernate6;

import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.tree.domain.SqmPath;

import java.util.List;

public class JsonBExtractPathText extends AbstractJsonBExtractPath {

    public JsonBExtractPathText(SqmPath<?> mapPath, NodeBuilder nodeBuilder, List<String> path) {
//        super(mapPath, nodeBuilder, path, JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME);
    }
}
