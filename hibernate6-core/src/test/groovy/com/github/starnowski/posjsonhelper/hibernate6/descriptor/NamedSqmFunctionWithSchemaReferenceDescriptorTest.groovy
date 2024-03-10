package com.github.starnowski.posjsonhelper.hibernate6.descriptor

import com.github.starnowski.posjsonhelper.core.Context
import com.github.starnowski.posjsonhelper.core.HibernateContext
import org.hibernate.query.ReturnableType
import org.hibernate.query.SortDirection
import org.hibernate.sql.ast.SqlAstTranslator
import org.hibernate.sql.ast.spi.StringBuilderSqlAppender
import org.hibernate.sql.ast.tree.expression.Expression
import org.hibernate.sql.ast.tree.predicate.Predicate
import org.hibernate.sql.ast.tree.select.SortSpecification
import spock.lang.Specification
import spock.lang.Unroll

import static java.util.Optional.ofNullable

class NamedSqmFunctionWithSchemaReferenceDescriptorTest extends Specification {

    @Unroll
    def "render method with only required parameters for function (#funName) without adding schema reference (#schema) at the beginning of statement (#functionThatRequiredExecutionWithSchema)"() {
        given:
            Set<String> functionThatRequiredExecutionWithSchemaSet = new HashSet<>(ofNullable(functionThatRequiredExecutionWithSchema).orElse(new ArrayList()))
            def descriptor = new NamedSqmFunctionWithSchemaReferenceDescriptor(funName, Context.builder()
                    .withSchema(schema)
                    .withFunctionsThatShouldBeExecutedWithSchemaReference(functionThatRequiredExecutionWithSchemaSet).build(),
                    HibernateContext.builder().build())
            def sqlAppender = new StringBuilderSqlAppender()

        when:
            descriptor.render(sqlAppender, [], (ReturnableType)null, (SqlAstTranslator)null)

        then:
            !sqlAppender.toString().startsWith(schema + ".")

        where:
            schema                  | functionThatRequiredExecutionWithSchema   |   funName
            "sche1"                 | []                                        |   "funcName"
            "non_public_schema"     | null                                      |   "funcName"
            "non_public_schema"     | []                                        |   "fun1"
            "non_public_schema"     | ["some function"]                         |   "some_other_fun"
            "non_public_schema"     | ["fun", "get_some"]                       |   "some_other_fun"
    }

    @Unroll
    def "render method with only required parameters for function (#funName) with adding schema reference (#schema) at the beginning of statement (#functionThatRequiredExecutionWithSchema)"() {
        given:
            def descriptor = new NamedSqmFunctionWithSchemaReferenceDescriptor(funName, Context.builder()
                    .withSchema(schema)
                    .withFunctionsThatShouldBeExecutedWithSchemaReference(new HashSet<>(functionThatRequiredExecutionWithSchema)).build(),
                    HibernateContext.builder().build())
            def sqlAppender = new StringBuilderSqlAppender()

        when:
            descriptor.render(sqlAppender, [], (ReturnableType)null, (SqlAstTranslator)null)

        then:
            sqlAppender.toString().startsWith(schema + ".")

        where:
            schema                  | functionThatRequiredExecutionWithSchema   |   funName
            "sche1"                 | ["funcName"]                              |   "funcName"
            "non_public_schema"     | ["get_some", "fun1"]                      |   "fun1"
            "non_public_schema"     | ["some_other_fun"]                        |   "some_other_fun"
            "non_public_schema"     | ["fun", "get_some"]                       |   "fun"
    }

    @Unroll
    def "render method with predicate and sort specification for function (#funName) without adding schema reference (#schema) at the beginning of statement (#functionThatRequiredExecutionWithSchema)"() {
        given:
            Set<String> functionThatRequiredExecutionWithSchemaSet = new HashSet<>(ofNullable(functionThatRequiredExecutionWithSchema).orElse(new ArrayList()))
            def descriptor = new NamedSqmFunctionWithSchemaReferenceDescriptor(funName, Context.builder()
                    .withSchema(schema)
                    .withFunctionsThatShouldBeExecutedWithSchemaReference(functionThatRequiredExecutionWithSchemaSet).build(),
                    HibernateContext.builder().build())
            def sqlAppender = new StringBuilderSqlAppender()
            SqlAstTranslator sqlAstTranslator = Mock(SqlAstTranslator)
            sqlAstTranslator.getCurrentClauseStack() >> Mock(org.hibernate.internal.util.collections.Stack)

        when:
            descriptor.render(sqlAppender, [], (Predicate) null, [new SortSpecification(Mock(Expression), SortDirection.ASCENDING)], (ReturnableType)null, sqlAstTranslator)

        then:
            !sqlAppender.toString().startsWith(schema + ".")

        where:
            schema                  | functionThatRequiredExecutionWithSchema   |   funName
            "sche1"                 | []                                        |   "funcName"
            "non_public_schema"     | null                                      |   "funcName"
            "non_public_schema"     | []                                        |   "fun1"
            "non_public_schema"     | ["some function"]                         |   "some_other_fun"
            "non_public_schema"     | ["fun", "get_some"]                       |   "some_other_fun"
    }
//
//    def "render method with filter parameter"() {
//        given:
//        def descriptor = new NamedSqmFunctionWithSchemaReferenceDescriptor("funcName", Context.builder().build(), HibernateContext.builder().build())
//
//        when:
//        def sqlAppender = new StringBuilder()
//        def filter = { /* mock filter */ }
//        descriptor.render(sqlAppender, [], filter, null)
//
//        then:
//        sqlAppender.toString() == "funcName() filter (where mock filter)"
//    }
//
//    def "render method with withinGroup parameter"() {
//        given:
//        def descriptor = new NamedSqmFunctionWithSchemaReferenceDescriptor("funcName", Context.builder().build(), HibernateContext.builder().build())
//
//        when:
//        def sqlAppender = new StringBuilder()
//        def withinGroup = [new SortSpecification()] // Provide a valid SortSpecification instance
//        descriptor.render(sqlAppender, [], null, withinGroup, null, null, null)
//
//        then:
//        sqlAppender.toString() == "funcName() within group (order by sortSpecification)"
//    }
//
//    def "render method with all optional parameters"() {
//        given:
//        def descriptor = new NamedSqmFunctionWithSchemaReferenceDescriptor("funcName", Context.builder().build(), HibernateContext.builder().build())
//
//        when:
//        def sqlAppender = new StringBuilder()
//        def filter = { /* mock filter */ }
//        def withinGroup = [new SortSpecification()] // Provide a valid SortSpecification instance
//        descriptor.render(sqlAppender, [], filter, withinGroup, true, true, null)
//
//        then:
//        sqlAppender.toString() == "funcName() filter (where mock filter) within group (order by sortSpecification) from first respect nulls"
//    }
}