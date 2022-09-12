package com.github.starnowski.posjsonhelper.core.sql;

import java.util.List;

/**
 * The default interface holds DDL statements, statements that drop applied changes, and SQL statements that verify if statements were applied correctly.
 */
public interface SQLDefinition {

    /**
     * Returns DDL statement which should be executed to apply changes that are represented by the main object.
     * @return DDL statement which should be executed to apply changes that are represented by the main object.
     */
    String getCreateScript();

    /**
     * Returns DDL statement that drops changes applied by statement returned by the {@link #getCreateScript()} method.
     * <p>
     * <b>IMPORTANT!</b>
     * </p>
     * By default, there is no assumption that statement has to contains the compensation operation for operation returned by the {@link #getCreateScript()} method.
     * This means that the operation can not be by default treated as a rollback operation, but an operation that removes changes applied by statement returned by the {@link #getCreateScript()} method.
     * @return DDL statement that drops changes applied by statement returned by the {@link #getCreateScript()} method.
     */
    String getDropScript();

    /**
     * Return a list of statements that checks if changes by done the script returned by method {@link #getCreateScript()} was applied.
     * The result of select query should be one column with numeric value.
     * If value is greater than zero then changes were applied correctly if not then changes were not applied.
     * Please be in mind that there is assumption that positive results for queries does not have means that changes were applied correctly.
     * The checking query gives only assumption that the changes generated by method {@link #getCreateScript()}  are applied.
     * And that is why there might be positive results for queries even before applying changes.
     * @return The list of SQL queries that each one return one row with one column. If column value is greater than zero then this means that the changes could been applied but there is no certainty.
     */
    List<String> getCheckingStatements();
}