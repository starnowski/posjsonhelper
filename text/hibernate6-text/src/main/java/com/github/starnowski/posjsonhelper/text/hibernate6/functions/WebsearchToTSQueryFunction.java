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
package com.github.starnowski.posjsonhelper.text.hibernate6.functions;

import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.tree.SqmTypedNode;
import org.hibernate.query.sqm.tree.expression.SqmExpression;

import java.util.List;

import static com.github.starnowski.posjsonhelper.core.Constants.WEBSEARCH_TO_TSQUERY_FUNCTION_NAME;

/**
 * Wrapper for the Postgres function 'websearch_to_tsquery'.
 * The function was added to the Postgres in version 11.
 * Please check <a href="https://www.postgresql.org/docs/11/textsearch-controls.html">Postgres doc</a>
 */
public class WebsearchToTSQueryFunction extends AbstractFunctionWithConfigurationAndTextQueryFunction {
    public WebsearchToTSQueryFunction(NodeBuilder nodeBuilder, String configuration, String query) {
        super(nodeBuilder, configuration, query, WEBSEARCH_TO_TSQUERY_FUNCTION_NAME);
    }

    public WebsearchToTSQueryFunction(NodeBuilder nodeBuilder, SqmExpression<?> configuration, String query) {
        super(nodeBuilder, configuration, query, WEBSEARCH_TO_TSQUERY_FUNCTION_NAME);
    }

    public WebsearchToTSQueryFunction(List<? extends SqmTypedNode<?>> arguments, NodeBuilder nodeBuilder) {
        super(arguments, nodeBuilder, WEBSEARCH_TO_TSQUERY_FUNCTION_NAME);
    }
}
