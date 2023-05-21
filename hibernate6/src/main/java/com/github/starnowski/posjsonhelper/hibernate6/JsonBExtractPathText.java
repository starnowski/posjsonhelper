package com.github.starnowski.posjsonhelper.hibernate6;

import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.SemanticQueryWalker;
import org.hibernate.query.sqm.SqmPathSource;
import org.hibernate.query.sqm.tree.SqmCopyContext;
import org.hibernate.query.sqm.tree.domain.SqmPath;

import java.util.List;

import static com.github.starnowski.posjsonhelper.core.Constants.JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME;

public class JsonBExtractPathText extends AbstractJsonBExtractPath {
//TODO

    public JsonBExtractPathText(SqmPathSource<String> referencedPathSource, NodeBuilder nodeBuilder, List<String> path) {
        super(referencedPathSource, nodeBuilder, path, JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME);
    }

    @Override
    public SqmPath<String> copy(SqmCopyContext sqmCopyContext) {
        return null;
    }

    @Override
    public <X> X accept(SemanticQueryWalker<X> semanticQueryWalker) {
        return null;
    }

    @Override
    public void appendHqlString(StringBuilder stringBuilder) {

    }
}
