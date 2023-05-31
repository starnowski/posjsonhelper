package com.github.starnowski.posjsonhelper.hibernate6.descriptor

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

            JsonArrayFunctionDescriptor jsonArrayFunctionDescriptor = new JsonArrayFunctionDescriptor()

        when:
            jsonArrayFunctionDescriptor.render(sqlAppender, sqlAstArguments, translator)

        then:
            1 * sqlAppender.appendSql("array")
            1 * sqlAppender.appendSql("[")
            1 * sqlAppender.appendSql(",")
            1 * sqlAppender.appendSql("]")

            2 * translator.render(_, SqlAstNodeRenderingMode.DEFAULT)
    }


}
