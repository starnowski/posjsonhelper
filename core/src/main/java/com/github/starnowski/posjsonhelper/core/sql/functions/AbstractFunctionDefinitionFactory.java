package com.github.starnowski.posjsonhelper.core.sql.functions;

import com.github.starnowski.posjsonhelper.core.sql.DefaultSQLDefinition;
import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinition;
import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinitionFactory;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public abstract class AbstractFunctionDefinitionFactory<R extends ISQLDefinition, P extends IFunctionFactoryParameters> implements ISQLDefinitionFactory<R, P> {

    @Override
    public R produce(P parameters) {
        validate(parameters);
        String createScript = produceStatement(parameters);
        String dropScript = returnDropScript(parameters);
        return returnFunctionDefinition(parameters, new DefaultSQLDefinition(createScript, dropScript, returnCheckingStatements(parameters)));
    }

    protected String returnFunctionReference(P parameters) {
        StringBuilder sb = new StringBuilder();
        if (parameters.getSchema() != null) {
            sb.append(parameters.getSchema());
            sb.append(".");
        }
        sb.append(parameters.getFunctionName());
        return sb.toString();
    }

    protected String returnDropScript(P parameters) {
        List<IFunctionArgument> arguments = prepareFunctionArguments(parameters);
        return format("DROP FUNCTION IF EXISTS %s(%s);", returnFunctionReference(parameters), prepareArgumentsPhrase(arguments));
    }

    protected List<String> returnCheckingStatements(P parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(1) FROM pg_proc pg, pg_catalog.pg_namespace pgn WHERE ");
        sb.append("pg.proname = '");
        sb.append(parameters.getFunctionName());
        sb.append("' AND ");
        if (parameters.getSchema() == null) {
            sb.append("pgn.nspname = 'public'");
        } else {
            sb.append("pgn.nspname = '");
            sb.append(parameters.getSchema());
            sb.append("'");
        }
        sb.append(" AND ");
        sb.append("pg.pronamespace =  pgn.oid;");
        return singletonList(sb.toString());
    }

    protected String prepareArgumentsPhrase(List<IFunctionArgument> functionArguments) {
        return functionArguments.stream().map(IFunctionArgument::getType).collect(Collectors.joining(", "));
    }

    protected void validate(P parameters) {
        if (parameters == null) {
            throw new IllegalArgumentException("The parameters object cannot be null");
        }
        if (parameters.getFunctionName() == null) {
            throw new IllegalArgumentException("Function name cannot be null");
        }
        if (parameters.getFunctionName().trim().isEmpty()) {
            throw new IllegalArgumentException("Function name cannot be blank");
        }
        if (parameters.getSchema() != null && parameters.getSchema().trim().isEmpty()) {
            throw new IllegalArgumentException("Schema name cannot be blank");
        }
    }

    protected List<IFunctionArgument> prepareFunctionArguments(P parameters) {
        return emptyList();
    }

    abstract protected R returnFunctionDefinition(P parameters, ISQLDefinition functionDefinition);

    abstract protected String produceStatement(P parameters);
}
