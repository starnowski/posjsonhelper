package com.github.starnowski.posjsonhelper.hibernate5;

import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;

import javax.persistence.criteria.Expression;
import java.util.List;

import static com.github.starnowski.posjsonhelper.core.Constants.JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME;

public class JsonBExtractPathText extends AbstractJsonBExtractPath {

    public JsonBExtractPathText(CriteriaBuilderImpl criteriaBuilder, List<String> path, Expression<?> operand) {
        super(criteriaBuilder, path, operand, JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME);
    }
}
