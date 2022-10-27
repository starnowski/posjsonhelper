package com.github.starnowski.posjsonhelper.core.sql.functions;

import com.github.starnowski.posjsonhelper.core.sql.DefaultSQLDefinition;
import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinition;
import com.github.starnowski.posjsonhelper.core.sql.ISQLDefinitionFactory;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
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

    protected String buildFunctionNameAndArgumentDeclaration(P parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE OR REPLACE FUNCTION ");
        sb.append(returnFunctionReference(parameters));
        sb.append(buildArgumentDeclaration(parameters));
        sb.append(" ");
        sb.append(buildReturnPhrase(parameters));
        return sb.toString();
    }

    private String buildReturnPhrase(P parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("RETURNS ");
        sb.append(prepareReturnType(parameters));
        return sb.toString();
    }

    protected String buildArgumentDeclaration(P parameters) {
        List<IFunctionArgument> arguments = prepareFunctionArguments(parameters);
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(prepareArgumentsPhrase(arguments));
        sb.append(")");
        return sb.toString();
    }

    protected String buildBodyAndMetaData(P parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append(buildBody(parameters));
        sb.append("\n");
        sb.append("$$ LANGUAGE ");
        sb.append(returnFunctionLanguage(parameters));
//        sb.append("\n");
        //TODO Metadata
//        sb.append(buildMetaData(parameters));
        return sb.toString();
    }

    protected String produceStatement(P parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append(buildFunctionNameAndArgumentDeclaration(parameters));
        sb.append(" AS $$");
        sb.append("\n");
        sb.append(buildBodyAndMetaData(parameters));
        sb.append(";");
        return sb.toString();
    }

    protected abstract List<IFunctionArgument> prepareFunctionArguments(P parameters);

    protected String returnFunctionLanguage(P parameters) {
        return "SQL";
    }


    protected abstract String buildBody(P parameters);

    protected abstract String prepareReturnType(P parameters);

    protected abstract R returnFunctionDefinition(P parameters, ISQLDefinition functionDefinition);

}
