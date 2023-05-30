package com.github.starnowski.posjsonhelper.hibernate6

import jakarta.persistence.criteria.Path
import org.hibernate.query.spi.QueryEngine
import org.hibernate.query.sqm.NodeBuilder
import org.hibernate.query.sqm.function.FunctionRenderingSupport
import org.hibernate.query.sqm.function.SqmFunctionDescriptor
import org.hibernate.query.sqm.function.SqmFunctionRegistry
import org.hibernate.query.sqm.tree.SqmTypedNode
import org.mockito.Mockito
import spock.lang.Specification
import spock.lang.Unroll

abstract class AbstractJsonBExtractPathTest <T extends AbstractJsonBExtractPath> extends Specification {

    @Unroll
    def "should pass correct path #path"(){
        given:
            NodeBuilder nodeBuilder = Mock(NodeBuilder)
            Path referencedPathSource = Mock(Path)

            QueryEngine queryEngine = Mockito.mock(QueryEngine)
//            QueryEngine queryEngine = GroovyMock(QueryEngine, global: true, useObjenesis: false)
            SqmFunctionRegistry sqmFunctionRegistry = Mock(SqmFunctionRegistry)
            TestInterfaceThatImplementsSqmFunctionDescriptorAndFunctionRenderingSupport ti = Mock(TestInterfaceThatImplementsSqmFunctionDescriptorAndFunctionRenderingSupport)
            Mockito.when(queryEngine.getSqmFunctionRegistry()).thenReturn(sqmFunctionRegistry)

        when:
            T tested = prepareTestObject(referencedPathSource, nodeBuilder, path)

        then:
            2 * nodeBuilder.getQueryEngine() >> queryEngine
//            2 * queryEngine.getSqmFunctionRegistry() >> sqmFunctionRegistry
            2 * sqmFunctionRegistry.findFunctionDescriptor(expectedFunctionName()) >> ti
            tested.nodeBuilder().is(nodeBuilder)
            tested.getFunctionName() == expectedFunctionName()
            List<SqmTypedNode> arguments = tested.getArguments()

        where:
            path << [["some", "property"], ["child1", "grandson1"]]
    }

    protected abstract T prepareTestObject(Path referencedPathSource, NodeBuilder nodeBuilder, List<String> path)

    protected abstract String expectedFunctionName()

    private static interface TestInterfaceThatImplementsSqmFunctionDescriptorAndFunctionRenderingSupport extends SqmFunctionDescriptor, FunctionRenderingSupport {}
}
