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
package com.github.starnowski.posjsonhelper.hibernate6.predicates;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPath;
import com.github.starnowski.posjsonhelper.hibernate6.operators.JsonArrayFunction;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.FunctionRenderer;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.produce.function.StandardFunctionReturnTypeResolvers;
import org.hibernate.query.sqm.tree.SqmCopyContext;
import org.hibernate.query.sqm.tree.expression.SqmExpression;
import org.hibernate.type.StandardBasicTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The component renders arguments in the below form for SQM. Based on string arguments, JsonPath, and main function.
 * Let's assume that for the below example, we have two arguments, JsonPath
 *
 * <pre>{@code
 * {{main_func}}( jsonb_extract_path( generatedAlias0.jsonbContent , :param0 ) , json_function_json_array(:param1, :param2))
 * }</pre>
 *
 * where:
 * {{main_func}} - name of main function returned by method {@link #getFunctionName()}
 * jsonb_extract_path( generatedAlias0.jsonbContent , :param0 ) - json path part, with this example path has only one element normally this could part could contain more elements "param"
 * {{json_function_json_array}} - hibernate operator that wraps the "array" operator in postgres. Values comes from  {@link HibernateContext#getJsonFunctionJsonArrayOperator()}
 * (:param1, :param2) - rendered string arguments
 */
public abstract class AbstractJsonbArrayStringsExistPredicate<T extends AbstractJsonbArrayStringsExistPredicate> extends SelfRenderingSqmFunction<Boolean> {

    private final HibernateContext context;
    private final JsonBExtractPath jsonBExtractPath;
    private final JsonArrayFunction jsonArrayFunction;

    /**
     * @param context          object of type {@link HibernateContext}
     * @param nodeBuilder      node builder {@link NodeBuilder}
     * @param jsonBExtractPath json path for json property {@link JsonBExtractPath}
     * @param values           array of string values passed to as argument for function
     * @param functionName     function name
     */
    public AbstractJsonbArrayStringsExistPredicate(HibernateContext context, NodeBuilder nodeBuilder, JsonBExtractPath jsonBExtractPath, String[] values, String functionName) {
        this(context, nodeBuilder, jsonBExtractPath, mapArrayValues(nodeBuilder, context, values), functionName);
    }

    /**
     * @param context          object of type {@link HibernateContext}
     * @param nodeBuilder      node builder {@link NodeBuilder}
     * @param jsonBExtractPath json path for json property {@link JsonBExtractPath}
     * @param arrayFunction    component of type {@link JsonArrayFunction} that represent array of values passed to as argument for function
     * @param functionName     function name
     */
    public AbstractJsonbArrayStringsExistPredicate(HibernateContext context, NodeBuilder nodeBuilder, JsonBExtractPath jsonBExtractPath, JsonArrayFunction arrayFunction, String functionName) {
        super(nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(functionName),
                (FunctionRenderer) nodeBuilder.getQueryEngine().getSqmFunctionRegistry().findFunctionDescriptor(functionName),
                parameters(jsonBExtractPath, arrayFunction),
                null,
                null,
                StandardFunctionReturnTypeResolvers.invariant(nodeBuilder.getTypeConfiguration().getBasicTypeRegistry().resolve(StandardBasicTypes.BOOLEAN)),
                nodeBuilder,
                functionName);
        this.jsonBExtractPath = jsonBExtractPath;
        this.context = context;
        this.jsonArrayFunction = arrayFunction;
    }

    private static List<? extends SqmExpression<String>> parameters(JsonBExtractPath jsonBExtractPath, JsonArrayFunction jsonArrayFunction) {
        List<SqmExpression<String>> result = new ArrayList<>();
        result.add(jsonBExtractPath);
        result.add(jsonArrayFunction);
        return result;
    }

    private static JsonArrayFunction mapArrayValues(NodeBuilder nodeBuilder, HibernateContext context, String... values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Values can not be null or empty list");
        }
        List<SqmExpression<String>> arrayArguments = new ArrayList<>();
        arrayArguments.addAll(Stream.of(values).map(p -> (SqmExpression<String>) nodeBuilder.value(p)).collect(Collectors.toList()));
        JsonArrayFunction jsonArrayFunction = new JsonArrayFunction(nodeBuilder, arrayArguments, context);
        return jsonArrayFunction;
    }

    public T copy(SqmCopyContext context) {
        T existing = (T) context.getCopy(this);
        if (existing != null) {
            return existing;
        } else {
            T predicate = (T) context.registerCopy(this, generateCopy(this.context, nodeBuilder(), jsonBExtractPath, jsonArrayFunction));
            this.copyTo(predicate, context);
            return predicate;
        }
    }


    abstract protected T generateCopy(HibernateContext context, NodeBuilder nodeBuilder, JsonBExtractPath jsonBExtractPath, JsonArrayFunction jsonArrayFunction);
}
