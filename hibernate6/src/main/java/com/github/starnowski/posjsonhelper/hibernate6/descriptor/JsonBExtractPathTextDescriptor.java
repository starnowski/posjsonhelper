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

import com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPathText;
import jakarta.persistence.criteria.Path;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.tree.SqmTypedNode;

import java.util.List;

import static com.github.starnowski.posjsonhelper.core.Constants.JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME;

/**
 * Function descriptor for child type of {@link JsonBExtractPathText}
 */
public class JsonBExtractPathTextDescriptor extends AbstractJsonBExtractPathDescriptor<JsonBExtractPathText> {
    public JsonBExtractPathTextDescriptor() {
        super(JSONB_EXTRACT_PATH_TEXT_FUNCTION_NAME);
    }

    @Override
    protected JsonBExtractPathText generateAbstractJsonBExtractPathImpl(Path referencedPathSource, List<SqmTypedNode<?>> pathArguments, NodeBuilder nodeBuilder) {
        return new JsonBExtractPathText(referencedPathSource, nodeBuilder, pathArguments);
    }
}
