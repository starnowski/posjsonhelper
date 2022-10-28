package com.github.starnowski.posjsonhelper.hibernate5

import org.hibernate.dialect.PostgreSQL81Dialect
import org.hibernate.internal.util.collections.StandardStack
import org.hibernate.query.criteria.internal.Renderable
import org.hibernate.query.criteria.internal.compile.RenderingContext
import org.hibernate.sql.ast.Clause
import spock.lang.Specification
import spock.lang.Unroll

import javax.persistence.criteria.Expression
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Selection

abstract class AbstractJsonBExtractPathTest<T extends AbstractJsonBExtractPath> extends Specification {

    @Unroll
    def "should create expected statement #expectedStatement for path '#path' and table column reference #tableColumnRef"(){

        given:
            RenderingContext renderingContext = Mock(RenderingContext)
            org.hibernate.internal.util.collections.Stack< Clause> clauseStack = new StandardStack< Clause>()
            renderingContext.getFunctionStack() >> new StandardStack()
            renderingContext.getClauseStack() >> clauseStack
            renderingContext.getDialect() >> new PostgreSQL81Dialect()
            def tested = getTested(path, new TestOperand(tableColumnRef))

            // Set select Clause
            clauseStack.push(Clause.SELECT)

        when:
            def result = tested.render(renderingContext)

        then:
            result == getFunctionName() + expectedStatement

        where:
            tableColumnRef | path || expectedStatement
            "json" | ["x"]  ||  "( json , 'x' )"
            "json_field" | ["x1"]  ||  "( json_field , 'x1' )"
            "table1.field_with_json" | ["x", "prop1"]  ||  "( table1.field_with_json , 'x', 'prop1' )"
            "non_public_schema.tab45.JSON_FIELDS" | ["z", "jjj", "enum_value"]  ||  "( non_public_schema.tab45.JSON_FIELDS , 'z', 'jjj', 'enum_value' )"
    }

    @Unroll
    def "should throw exception when path arguments are null or empty #path"(){

        given:
            RenderingContext renderingContext = Mock(RenderingContext)
            org.hibernate.internal.util.collections.Stack< Clause> clauseStack = new StandardStack< Clause>()
            renderingContext.getFunctionStack() >> new StandardStack()
            renderingContext.getClauseStack() >> clauseStack
            renderingContext.getDialect() >> new PostgreSQL81Dialect()
            // Set select Clause
            clauseStack.push(Clause.SELECT)

        when:
            getTested(path, new TestOperand("some_column"))

        then:
            def ex = thrown(IllegalArgumentException)

        and:
            ex.message == "Path argument can not be null or empty list"

        where:
            path << [null, []]
    }

    @Unroll
    def "should java type based on operand, java type #javaType"(){

        given:
            RenderingContext renderingContext = Mock(RenderingContext)
            org.hibernate.internal.util.collections.Stack< Clause> clauseStack = new StandardStack< Clause>()
            renderingContext.getFunctionStack() >> new StandardStack()
            renderingContext.getClauseStack() >> clauseStack
            renderingContext.getDialect() >> new PostgreSQL81Dialect()
            def tested = getTested(["some_json_path"], new TestOperand("some_column", javaType))

            // Set select Clause
            clauseStack.push(Clause.SELECT)

        when:
            def result = tested.getJavaType()

        then:
            result == javaType

        where:
            javaType << [String, Boolean, Integer, BigInteger, null]
    }

    protected abstract T getTested(List<String> path, Expression<?> operand);

    protected abstract String getFunctionName();

    private static class TestOperand<X> implements Expression<X>, Renderable{

        private final String reference
        private final Class javaType
        TestOperand(String reference) {
            this(reference, null)
        }

        TestOperand(String reference, Class javaType) {
            this.reference = reference
            this.javaType = javaType
        }

        @Override
        Predicate isNull() {
            return null
        }

        @Override
        Predicate isNotNull() {
            return null
        }

        @Override
        Predicate "in"(Object... objects) {
            return null
        }

        @Override
        Expression<X> "as"(Class aClass) {
            return null
        }

        @Override
        Predicate "in"(Expression expression) {
            return null
        }

        @Override
        Predicate "in"(Collection collection) {
            return null
        }

        @Override
        Predicate "in"(Expression[] expressions) {
            return null
        }

        @Override
        Selection alias(String s) {
            return null
        }

        @Override
        boolean isCompoundSelection() {
            return false
        }

        @Override
        List<Selection<?>> getCompoundSelectionItems() {
            return null
        }

        @Override
        Class getJavaType() {
            javaType
        }

        @Override
        String getAlias() {
            return null
        }

        @Override
        String render(RenderingContext renderingContext) {
            this.reference
        }
    }
}
