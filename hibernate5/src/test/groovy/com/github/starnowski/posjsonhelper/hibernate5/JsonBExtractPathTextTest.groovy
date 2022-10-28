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

class JsonBExtractPathTextTest extends Specification {

    @Unroll
    def "should create expected statement #expectedStatement for path '#path' and table column reference #tableColumnRef"(){

        given:
            RenderingContext renderingContext = Mock(RenderingContext)
            org.hibernate.internal.util.collections.Stack< Clause> clauseStack = new StandardStack< Clause>()
            renderingContext.getFunctionStack() >> new StandardStack()
            renderingContext.getClauseStack() >> clauseStack
            renderingContext.getDialect() >> new PostgreSQL81Dialect()
            def tested = new JsonBExtractPathText(null, path, new TestOperand(tableColumnRef))

            // Set select Clause
            clauseStack.push(Clause.SELECT)

        when:
            def result = tested.render(renderingContext)

        then:
            result == expectedStatement

        where:
            tableColumnRef | path || expectedStatement
            "json" | ["x"]  ||  "jsonb_extract_path_text( json , 'x' )"
            "json_field" | ["x1"]  ||  "jsonb_extract_path_text( json_field , 'x1' )"
            "table1.field_with_json" | ["x", "prop1"]  ||  "jsonb_extract_path_text( table1.field_with_json , 'x', 'prop1' )"
            "non_public_schema.tab45.JSON_FIELDS" | ["z", "jjj", "enum_value"]  ||  "jsonb_extract_path_text( non_public_schema.tab45.JSON_FIELDS , 'z', 'jjj', 'enum_value' )"
    }

    private static class TestOperand<X> implements Expression<X>, Renderable{

        private final String reference;
        TestOperand(String reference) {
            this.reference = reference
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
            return null
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
