package com.github.starnowski.posjsonhelper.hibernate6;

import jakarta.persistence.criteria.Path;
import org.hibernate.query.ReturnableType;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.SemanticQueryWalker;
import org.hibernate.query.sqm.SqmPathSource;
import org.hibernate.query.sqm.function.SelfRenderingFunctionSqlAstExpression;
import org.hibernate.query.sqm.sql.SqmToSqlAstConverter;
import org.hibernate.query.sqm.tree.SqmCopyContext;
import org.hibernate.query.sqm.tree.domain.SqmPath;
import org.hibernate.query.sqm.tree.expression.SqmExpression;
import org.hibernate.query.sqm.tree.select.SqmSelectableNode;
import org.hibernate.sql.ast.tree.SqlAstNode;
import org.hibernate.sql.ast.tree.expression.Expression;

import java.util.List;
import java.util.function.Consumer;

import static com.github.starnowski.posjsonhelper.core.Constants.JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME;

public class JsonBExtractPathText extends AbstractJsonBExtractPath<JsonBExtractPathText> {
//TODO

    public JsonBExtractPathText(Path referencedPathSource, NodeBuilder nodeBuilder, List<String> path) {
        super(referencedPathSource, nodeBuilder, path, JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME);
    }

    @Override
    protected JsonBExtractPathText generate(Path referencedPathSource, NodeBuilder nodeBuilder, List<String> path) {
        return new JsonBExtractPathText(referencedPathSource, nodeBuilder, path);
    }

}
