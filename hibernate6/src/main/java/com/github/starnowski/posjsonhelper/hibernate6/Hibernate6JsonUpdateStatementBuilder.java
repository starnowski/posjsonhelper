package com.github.starnowski.posjsonhelper.hibernate6;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.functions.JsonbSetFunction;
import com.github.starnowski.posjsonhelper.json.core.sql.*;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.tree.SqmTypedNode;

import static com.github.starnowski.posjsonhelper.json.core.sql.JsonUpdateStatementOperationType.JSONB_SET;

/**
 * Builder for SQL statement part that allows to set particular json properties.
 * The idea is to execute some kind of patch operation instead of full update operation for json column value.
 * To set correct order for operation it uses {@link #jsonUpdateStatementConfigurationBuilder} component.
 * TODO - Full code example
 * TODO - Java
 * TODO Generated SQL
 * @param <T>
 * @see #build()
 */
public class Hibernate6JsonUpdateStatementBuilder<T> {

    private final Path<T> rootPath;
    private final NodeBuilder nodeBuilder;
    private final HibernateContext hibernateContext;

    public JsonUpdateStatementConfigurationBuilder getJsonUpdateStatementConfigurationBuilder() {
        return jsonUpdateStatementConfigurationBuilder;
    }

    private final JsonUpdateStatementConfigurationBuilder jsonUpdateStatementConfigurationBuilder;

    /**
     * TODO Add note about default values for the sort and the filter components
     * @param rootPath
     * @param nodeBuilder
     * @param hibernateContext
     */
    public Hibernate6JsonUpdateStatementBuilder(Path<T> rootPath, NodeBuilder nodeBuilder, HibernateContext hibernateContext) {
        this.rootPath = rootPath;
        this.nodeBuilder = nodeBuilder;
        this.hibernateContext = hibernateContext;
        jsonUpdateStatementConfigurationBuilder = new JsonUpdateStatementConfigurationBuilder()
                .withSort(new DefaultJsonUpdateStatementOperationSort())
                .withPostSortFilter(new DefaultJsonUpdateStatementOperationFilter());
    }

    /**
     * TODO
     * @param jsonTextArray
     * @param value
     * @return
     */
    public Hibernate6JsonUpdateStatementBuilder appendJsonbSet(JsonTextArray jsonTextArray, String value) {
        jsonUpdateStatementConfigurationBuilder.append(JSONB_SET, jsonTextArray, value);
        return this;
    }

    /**
     * TODO
     * @param sort
     * @return
     */
    public Hibernate6JsonUpdateStatementBuilder withSort(JsonUpdateStatementConfigurationBuilder.JsonUpdateStatementOperationSort sort) {
        jsonUpdateStatementConfigurationBuilder.withSort(sort);
        return this;
    }

    /**
     * TODO
     * @param postSortFilter
     * @return
     */
    public Hibernate6JsonUpdateStatementBuilder withPostSortFilter(JsonUpdateStatementConfigurationBuilder.JsonUpdateStatementOperationFilter postSortFilter) {
        jsonUpdateStatementConfigurationBuilder.withPostSortFilter(postSortFilter);
        return this;
    }

    /**
     * TODO Example for operations list
     * TODO Generated statement
     * @return
     */
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
