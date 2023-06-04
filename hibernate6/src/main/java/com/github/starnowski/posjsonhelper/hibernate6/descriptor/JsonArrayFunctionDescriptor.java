/**
 *     Posjsonhelper library is an open-source project that adds support of
 *     Hibernate query for https://www.postgresql.org/docs/10/functions-json.html)
 *
 *     Copyright (C) 2023  Szymon Tarnowski
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 */
package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import org.hibernate.query.sqm.function.AbstractSqmSelfRenderingFunctionDescriptor;
import org.hibernate.sql.ast.SqlAstNodeRenderingMode;
import org.hibernate.sql.ast.SqlAstTranslator;
import org.hibernate.sql.ast.spi.SqlAppender;
import org.hibernate.sql.ast.tree.SqlAstNode;

import java.util.Iterator;
import java.util.List;

/**
 * Function descriptor for ARRAY ({@link https://www.postgresql.org/docs/12/functions-array.html} postgres operator.
 * Generally it renders passed arguments into array instance.
 * For example:
 * For two arguments, the component is going to render as below:
 * SQL
 * <pre>{@code
 *  array[?,?]
 * }</pre>
 *
 * In case of five arguments, the component is going to render as below:
 * SQL
 * <pre>{@code
 *  array[?,?,?,?,?]
 * }</pre>
 */
public class JsonArrayFunctionDescriptor extends AbstractSqmSelfRenderingFunctionDescriptor {
    public JsonArrayFunctionDescriptor() {
        super("array", null, null, null);
    }

    @Override
    public void render(SqlAppender sqlAppender, List<? extends SqlAstNode> sqlAstArguments, SqlAstTranslator<?> translator) {
        sqlAppender.appendSql(this.getName());
        sqlAppender.appendSql("[");

        boolean firstPass = true;

        for (Iterator var11 = sqlAstArguments.iterator(); var11.hasNext(); firstPass = false) {
            SqlAstNode arg = (SqlAstNode) var11.next();
            if (!firstPass) {
                sqlAppender.appendSql(",");
            }
            translator.render(arg, SqlAstNodeRenderingMode.DEFAULT);
        }
        sqlAppender.appendSql("]");
    }
}
