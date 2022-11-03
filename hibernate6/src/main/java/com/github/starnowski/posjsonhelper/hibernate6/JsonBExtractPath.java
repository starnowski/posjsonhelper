package com.github.starnowski.posjsonhelper.hibernate6;

import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.tree.domain.SqmPath;

import java.util.List;

public class JsonBExtractPath extends AbstractJsonBExtractPath {
    public JsonBExtractPath(SqmPath<?> mapPath, NodeBuilder nodeBuilder, List<String> path) {
//        super(mapPath, nodeBuilder, path, JSONB_EXTRACT_PATH_FUNCTION_NAME);
    }
}
