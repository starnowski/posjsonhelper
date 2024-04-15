package com.github.starnowski.posjsonhelper.hibernate6;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.functions.JsonbSetFunction;
import com.github.starnowski.posjsonhelper.json.core.sql.*;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.tree.SqmTypedNode;

import static com.github.starnowski.posjsonhelper.json.core.sql.JsonUpdateStatementOperationType.JSONB_SET;

public class Hibernate6JsonUpdateStatementBuilder<T> {

    private final Path<T> rootPath;
    private final NodeBuilder nodeBuilder;
    private final HibernateContext hibernateContext;
    private final JsonUpdateStatementConfigurationBuilder jsonUpdateStatementConfigurationBuilder;

    public Hibernate6JsonUpdateStatementBuilder(Path<T> rootPath, NodeBuilder nodeBuilder, HibernateContext hibernateContext) {
        this.rootPath = rootPath;
        this.nodeBuilder = nodeBuilder;
        this.hibernateContext = hibernateContext;
        jsonUpdateStatementConfigurationBuilder = new JsonUpdateStatementConfigurationBuilder()
                .withSort(new DefaultJsonUpdateStatementOperationSort())
                .withPostSortFilter(new DefaultJsonUpdateStatementOperationFilter());
    }

    public Hibernate6JsonUpdateStatementBuilder appendJsonbSet(JsonTextArray jsonTextArray, String value) {
        jsonUpdateStatementConfigurationBuilder.append(JSONB_SET, jsonTextArray, value);
        return this;
    }

    public Expression<? extends T> build() {
        JsonUpdateStatementConfiguration configuration = jsonUpdateStatementConfigurationBuilder.build();
        SqmTypedNode current = null;
        for (JsonUpdateStatementConfiguration.JsonUpdateStatementOperation operation : configuration.getOperations()) {
            if (current == null) {
                current = new JsonbSetFunction(nodeBuilder, rootPath, operation.getJsonTextArray().toString(), operation.getValue(), hibernateContext);
            } else {
                current = new JsonbSetFunction(nodeBuilder, current, operation.getJsonTextArray().toString(), operation.getValue(), hibernateContext);
            }
        }
        return (Expression<? extends T>) current;
    }
}
