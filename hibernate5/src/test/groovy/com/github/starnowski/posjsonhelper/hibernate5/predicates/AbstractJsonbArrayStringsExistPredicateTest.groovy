package com.github.starnowski.posjsonhelper.hibernate5.predicates

import com.github.starnowski.posjsonhelper.core.HibernateContext
import com.github.starnowski.posjsonhelper.hibernate5.JsonBExtractPath
import org.hibernate.dialect.PostgreSQL81Dialect
import org.hibernate.internal.util.collections.StandardStack
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl
import org.hibernate.query.criteria.internal.compile.RenderingContext
import org.hibernate.sql.ast.Clause
import spock.lang.Specification
import spock.lang.Unroll

abstract class AbstractJsonbArrayStringsExistPredicateTest <T extends AbstractJsonbArrayStringsExistPredicate> extends Specification {

    @Unroll
    def "should create expected statement #expectedStatement for function name #functionName, json path #jp, isNegated #isNegated, array function #arrayFunction, values #values"(){

        given:
            RenderingContext renderingContext = Mock(RenderingContext)
            org.hibernate.internal.util.collections.Stack< Clause> clauseStack = new StandardStack< Clause>()
            renderingContext.getFunctionStack() >> new StandardStack()
            renderingContext.getClauseStack() >> clauseStack
            renderingContext.getDialect() >> new PostgreSQL81Dialect()

            def jsonBExtractPath = Mock(JsonBExtractPath)
            jsonBExtractPath.getJavaType() >> jp.getJavaType()
            jsonBExtractPath.render(renderingContext) >> jp.getRenderedValue()
            HibernateContext.ContextBuilder builder = HibernateContext.builder()
            builder = enrichHibernateContextWithExpectedFunctionName(functionName, builder)
            builder = builder.withJsonFunctionJsonArrayOperator(arrayFunction)

            def tested = getTested(builder.build(), (CriteriaBuilderImpl)null, jsonBExtractPath, values)

            // Set select Clause
            clauseStack.push(Clause.SELECT)

        when:
            def result = tested.render(isNegated, renderingContext)

        then:
            result == expectedStatement

        where:
            functionName | jp | isNegated | arrayFunction   | values || expectedStatement
            "test1"     | jp("json_path", String)  |   false   |   "json_array"| ["x"]   ||  "test1( json_path , json_array('x')) = TRUE"
            "test1"     | jp("json_path", String)  |   true   |   "array"|["xda"]   ||  "test1( json_path , array('xda')) = FALSE"
    }

    protected abstract HibernateContext.ContextBuilder enrichHibernateContextWithExpectedFunctionName(String functionName, HibernateContext.ContextBuilder builder)

    protected abstract T getTested(HibernateContext context, CriteriaBuilderImpl criteriaBuilder, JsonBExtractPath jsonBExtractPath, List<String> values)

    private static JsonBExtractPathTestObject jp(String renderedValue, Class javaType){
        new JsonBExtractPathTestObject(renderedValue, javaType)
    }

    private static class JsonBExtractPathTestObject {

        private final String renderedValue
        private final Class javaType

        JsonBExtractPathTestObject(String renderedValue, Class javaType) {
            this.renderedValue = renderedValue
            this.javaType = javaType
        }

        String getRenderedValue() {
            return renderedValue
        }
        Class getJavaType() {
            return javaType
        }


        @Override
        public String toString() {
            return "JsonBExtractPath{" +
                    "renderedValue='" + renderedValue + '\'' +
                    ", javaType=" + javaType +
                    '}';
        }
    }
}
