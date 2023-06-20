package com.github.starnowski.posjsonhelper.hibernate6.descriptor

import com.github.starnowski.posjsonhelper.core.HibernateContext
import org.hibernate.query.spi.QueryEngine
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction
import org.hibernate.query.sqm.function.SqmFunctionRegistry
import org.hibernate.query.sqm.internal.SqmCriteriaNodeBuilder
import org.hibernate.query.sqm.tree.SqmTypedNode
import org.hibernate.sql.ast.SqlAstNodeRenderingMode
import org.hibernate.sql.ast.SqlAstTranslator
import org.hibernate.sql.ast.spi.SqlAppender
import org.hibernate.sql.ast.tree.SqlAstNode
import spock.lang.Specification

class JsonArrayFunctionDescriptorTest extends Specification {

    def "render method should append the expected SQL"() {
        given:
            SqlAppender sqlAppender = Mock(SqlAppender)
            SqlAstTranslator<?> translator = Mock(SqlAstTranslator)
            List<SqlAstNode> sqlAstArguments = [Mock(SqlAstNode), Mock(SqlAstNode)]

            JsonArrayFunctionDescriptor jsonArrayFunctionDescriptor = new JsonArrayFunctionDescriptor(null)

        when:
            jsonArrayFunctionDescriptor.render(sqlAppender, sqlAstArguments, translator)

        then:
            1 * sqlAppender.appendSql("array")
            1 * sqlAppender.appendSql("[")
            1 * sqlAppender.appendSql(",")
            1 * sqlAppender.appendSql("]")

            2 * translator.render(_, SqlAstNodeRenderingMode.DEFAULT)
    }

    def "should generate SqmFunctionExpression for method #method"() {
        given:
            QueryEngine queryEngine = Mock(QueryEngine)
            SqmFunctionRegistry sqmFunctionRegistry = Mock(SqmFunctionRegistry)
            List<? extends SqmTypedNode<?>> arguments = new ArrayList<>()
            def criteriaBuilder = Mock(SqmCriteriaNodeBuilder)
            def hibernateContext = Mock(HibernateContext)
            JsonArrayFunctionDescriptor jsonArrayFunctionDescriptor = new JsonArrayFunctionDescriptor(hibernateContext)
            hibernateContext.getJsonFunctionJsonArrayOperator() >> method

        when:
            def result = jsonArrayFunctionDescriptor.generateSqmFunctionExpression(arguments, null, queryEngine, null)

        then:
            queryEngine.getCriteriaBuilder() >> criteriaBuilder
            criteriaBuilder.getQueryEngine() >> queryEngine
            queryEngine.getSqmFunctionRegistry() >> sqmFunctionRegistry
            sqmFunctionRegistry.findFunctionDescriptor(method) >> jsonArrayFunctionDescriptor
            result instanceof SelfRenderingSqmFunction

        where:
            method << ["test_function", "arrayFunction"]
    }

}
