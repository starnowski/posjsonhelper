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
            "functionMain"     | jp("somePath.child1.grandSon", String)  |   true   |   "array"|["xda", "fix1"]   ||  "functionMain( somePath.child1.grandSon , array('xda', 'fix1')) = FALSE"
            "functionMain"     | jp("somePath.child1.grandSon", Integer)  |   true   |   "array"|["4", "7"]   ||  "functionMain( somePath.child1.grandSon , array(4, 7)) = FALSE"
            "testFunction"     | jp("parent.path", null)  |   false   |   "array"|["13", "7"]   ||  "testFunction( parent.path , array('13', '7')) = TRUE"
            "funXX"     | jp("child33", JsonBExtractPathTestObject)  |   true   |   "array"|["13", "7"]   ||  "funXX( child33 , array('13', '7')) = FALSE"
    }

    def "should return operand for jsonBExtractPath"(){

        given:
            RenderingContext renderingContext = Mock(RenderingContext)
            renderingContext.getFunctionStack() >> new StandardStack()
            renderingContext.getDialect() >> new PostgreSQL81Dialect()

            def jsonBExtractPath = Mock(JsonBExtractPath)
            jsonBExtractPath.getJavaType() >> String
            jsonBExtractPath.render(renderingContext) >> "x1"
            HibernateContext.ContextBuilder builder = HibernateContext.builder()
            builder = enrichHibernateContextWithExpectedFunctionName("test1", builder)
            builder = builder.withJsonFunctionJsonArrayOperator("json_array")

            def tested = getTested(builder.build(), (CriteriaBuilderImpl)null, jsonBExtractPath, ["123"])

        when:
            def result = tested.getOperand()

        then:
            result == jsonBExtractPath
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
