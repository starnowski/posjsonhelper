package com.github.starnowski.posjsonhelper.hibernate5.functions;

import com.github.starnowski.posjsonhelper.core.Context;

import javax.persistence.criteria.Expression;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

public class JsonBExtractPath {

    private final Context context;
    private final List<String> path;
    private final Expression<?> operand;

    public JsonBExtractPath(Context context, List<String> path, Expression<?> operand) {
        this.context = context;
        this.path = path;
        this.operand = operand;
    }

    public Context getContext() {
        return context;
    }

    public List<String> getPath() {
        return this.path == null ? emptyList() : new ArrayList<>(this.path);
    }

    public Expression<?> getOperand() {
        return operand;
    }
}
