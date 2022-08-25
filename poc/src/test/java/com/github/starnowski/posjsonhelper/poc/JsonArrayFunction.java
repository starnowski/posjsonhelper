package com.github.starnowski.posjsonhelper.poc;

import org.hibernate.QueryException;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.BooleanType;
import org.hibernate.type.Type;

import java.util.Iterator;
import java.util.List;

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
        if ( args.size() <= 0  ) {
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
        //TODO Sql escape
        return sb.toString();
    }
}