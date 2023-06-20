package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPathText;
import jakarta.persistence.criteria.Path;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.tree.SqmTypedNode;

import java.util.List;

import static com.github.starnowski.posjsonhelper.core.Constants.JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME;

/**
 * Function descriptor for child type of {@link JsonBExtractPathText}
 */
public class JsonBExtractPathTextDescriptor extends AbstractJsonBExtractPathDescriptor<JsonBExtractPathText> {
    public JsonBExtractPathTextDescriptor() {
        super(JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME);
    }

    @Override
    protected JsonBExtractPathText generateAbstractJsonBExtractPathImpl(Path referencedPathSource, List<SqmTypedNode<?>> pathArguments, NodeBuilder nodeBuilder) {
        return new JsonBExtractPathText(referencedPathSource, nodeBuilder, pathArguments);
    }
}
