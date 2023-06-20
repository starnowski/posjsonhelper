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
package com.github.starnowski.posjsonhelper.hibernate6;

import jakarta.persistence.criteria.Path;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.tree.SqmTypedNode;

import java.util.List;

import static com.github.starnowski.posjsonhelper.core.Constants.JSONB_EXTRACT_PATH_FUNCTION_NAME;
import static com.github.starnowski.posjsonhelper.core.Constants.JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME;

/**
 * Type that extends {@link AbstractJsonBExtractPath}.
 * Implemented of HQL function defined by constant {@link com.github.starnowski.posjsonhelper.core.Constants#JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME}
 */
public class JsonBExtractPathText extends AbstractJsonBExtractPath<JsonBExtractPathText> {
    public JsonBExtractPathText(Path referencedPathSource, List<String> path, NodeBuilder nodeBuilder) {
        super(referencedPathSource, nodeBuilder, path, JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME);
    }

    public JsonBExtractPathText(Path referencedPathSource, NodeBuilder nodeBuilder, List<? extends SqmTypedNode<?>> path) {
        super(referencedPathSource, path, nodeBuilder, JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME);
    }
}
