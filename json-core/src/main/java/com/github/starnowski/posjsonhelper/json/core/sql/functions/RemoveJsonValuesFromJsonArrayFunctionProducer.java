package com.github.starnowski.posjsonhelper.json.core.sql.functions;

import com.github.starnowski.posjsonhelper.core.sql.functions.AbstractDefaultFunctionDefinitionFactory;
import com.github.starnowski.posjsonhelper.core.sql.functions.DefaultFunctionFactoryParameters;
import com.github.starnowski.posjsonhelper.core.sql.functions.IFunctionArgument;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static com.github.starnowski.posjsonhelper.core.sql.functions.DefaultFunctionArgument.withNameAndType;

public class RemoveJsonValuesFromJsonArrayFunctionProducer extends AbstractDefaultFunctionDefinitionFactory {

    private final String removeJsonValuesFromJsonArrayFunctionBody;

    public RemoveJsonValuesFromJsonArrayFunctionProducer() {
        removeJsonValuesFromJsonArrayFunctionBody = convert(RemoveJsonValuesFromJsonArrayFunctionProducer.class.getResourceAsStream("remove_json_values_function_body.txt"));
    }

    private static String convert(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        byte[] byteArray = new byte[1024];
        int readBytes = -1;
        try {
            while ((readBytes = inputStream.read(byteArray)) != -1) {
                stringBuilder.append(new String(Arrays.copyOfRange(byteArray, 0, readBytes)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();
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
