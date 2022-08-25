package com.github.starnowski.posjsonhelper.hibernate5.functions;

import org.hibernate.QueryException;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.BooleanType;
import org.hibernate.type.Type;

import java.util.List;

public class AllStringAtTopLevelJsonFunction implements SQLFunction {
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
        if ( args.size()!=2 ) {
            throw new QueryException( "jsonb_all_array_strings_exist requires two arguments; found : " + args.size() );
        }

        return "jsonb_all_array_strings_exist(" + args.get( 0 ) + " , " + args.get(1) + ")";
    }
}
