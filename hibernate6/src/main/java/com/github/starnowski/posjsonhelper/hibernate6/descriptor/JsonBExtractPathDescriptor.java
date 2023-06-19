package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPath;
import jakarta.persistence.criteria.Path;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.tree.SqmTypedNode;

import java.util.List;

import static com.github.starnowski.posjsonhelper.core.Constants.JSONB_EXTRACT_PATH_FUNCTION_NAME;

public class JsonBExtractPathDescriptor extends AbstractJsonBExtractPathDescriptor<JsonBExtractPath> {
    public JsonBExtractPathDescriptor() {
        super(JSONB_EXTRACT_PATH_FUNCTION_NAME);
    }

    @Override
    protected JsonBExtractPath generateAbstractJsonBExtractPathImpl(Path referencedPathSource, List<SqmTypedNode<?>> pathArguments, NodeBuilder nodeBuilder) {
        return new JsonBExtractPath(referencedPathSource, pathArguments, nodeBuilder);
    }
}
