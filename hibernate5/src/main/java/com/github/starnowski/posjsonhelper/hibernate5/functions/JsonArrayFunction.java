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
package com.github.starnowski.posjsonhelper.hibernate5.functions;

import org.hibernate.QueryException;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.BooleanType;
import org.hibernate.type.Type;

import java.util.Iterator;
import java.util.List;

/**
 * Component that renders arguments into postgres operator "array".
 * For example lest assume that the "json_function_json_array" is HQL function that is going to be rendered into the 'array operator:
 *
 * Hibernate:
 * <pre>{@code
 * select
 *         generatedAlias0
 *     from
 *         Item as generatedAlias0
 *     where
 *         jsonb_all_array_strings_exist( jsonb_extract_path( generatedAlias0.jsonbContent , :param0 ) , json_function_json_array(:param1, :param2)) = TRUE
 * }</pre>
 *
 * SQL:
 * <pre>{@code
 * select
 *        item0_.id as id1_0_,
 *        item0_.jsonb_content as jsonb_co2_0_
 *        from
 *        item item0_
 *        where
 *        jsonb_all_array_strings_exist(jsonb_extract_path(item0_.jsonb_content,?),array[?,?])=true
 * }</pre>
 */
public class JsonArrayFunction implements SQLFunction {
    @Override
    public boolean hasArguments() {
        return true;
    }

    @Override
    public boolean hasParenthesesIfNoArguments() {
        return true;
    }

    @Override
    public Type getReturnType(Type firstArgumentType, Mapping mapping) throws QueryException {
        return BooleanType.INSTANCE;
    }

    @Override
    public String render(Type argumentType, List args, SessionFactoryImplementor factory) throws QueryException {
        if ( args.size() < 1  ) {
            throw new QueryException( "json_array requires at least one argument");
        }

        return renderValues(args);
    }

    private String renderValues(List values)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("array[");
        Iterator<Object> it = values.iterator();
        while (it.hasNext())
        {
            String value = it.next().toString();
            sb.append(value);
            if (it.hasNext())
            {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}