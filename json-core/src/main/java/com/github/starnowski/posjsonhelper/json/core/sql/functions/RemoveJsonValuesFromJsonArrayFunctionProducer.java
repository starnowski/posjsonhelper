package com.github.starnowski.posjsonhelper.json.core.sql.functions;

import com.github.starnowski.posjsonhelper.core.sql.functions.AbstractDefaultFunctionDefinitionFactory;
import com.github.starnowski.posjsonhelper.core.sql.functions.DefaultFunctionFactoryParameters;
import com.github.starnowski.posjsonhelper.core.sql.functions.IFunctionArgument;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static com.github.starnowski.posjsonhelper.core.sql.functions.DefaultFunctionArgument.withNameAndType;

public class RemoveJsonValuesFromJsonArrayFunctionProducer extends AbstractDefaultFunctionDefinitionFactory {

    private final String removeJsonValuesFromJsonArrayFunctionBody;

    public RemoveJsonValuesFromJsonArrayFunctionProducer(){
        URI uri;
        byte[] fileBytes;
        try {
            uri = RemoveJsonValuesFromJsonArrayFunctionProducer.class.getClassLoader().getResource("remove_json_values_function_body.txt").toURI();
            fileBytes = Files.readAllBytes(Paths.get(uri));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        removeJsonValuesFromJsonArrayFunctionBody = new String(fileBytes, StandardCharsets.UTF_8);
    }

    @Override
    protected List<IFunctionArgument> prepareFunctionArguments(DefaultFunctionFactoryParameters parameters) {
        return Arrays.asList(withNameAndType("input_json", "jsonb"), withNameAndType("values_to_remove", "jsonb"));
    }

    @Override
    protected String buildBody(DefaultFunctionFactoryParameters parameters) {
        return removeJsonValuesFromJsonArrayFunctionBody;
    }

    @Override
    protected String prepareReturnType(DefaultFunctionFactoryParameters parameters) {
        return "jsonb";
    }

    @Override
    protected String returnFunctionLanguage(DefaultFunctionFactoryParameters parameters) {
        return "plpgsql";
    }
}
