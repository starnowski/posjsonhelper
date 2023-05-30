package com.github.starnowski.posjsonhelper.hibernate6

import jakarta.persistence.criteria.Path
import org.hibernate.query.spi.QueryEngine
import org.hibernate.query.sqm.NodeBuilder
import org.hibernate.query.sqm.function.FunctionRenderingSupport
import org.hibernate.query.sqm.function.SqmFunctionDescriptor
import org.hibernate.query.sqm.function.SqmFunctionRegistry
import org.hibernate.query.sqm.internal.SqmCriteriaNodeBuilder
import org.hibernate.query.sqm.tree.SqmTypedNode
import org.hibernate.query.sqm.tree.domain.SqmBasicValuedSimplePath
import org.hibernate.type.BasicTypeRegistry
import org.hibernate.type.StandardBasicTypes
import org.hibernate.type.spi.TypeConfiguration
import org.mockito.Mockito
import spock.lang.Specification
import spock.lang.Unroll

abstract class AbstractJsonBExtractPathTest <T extends AbstractJsonBExtractPath> extends Specification {

    @Unroll
    def "should pass correct path #path"(){
        given:
            SqmCriteriaNodeBuilder nodeBuilder = Mock(SqmCriteriaNodeBuilder)
            SqmBasicValuedSimplePath referencedPathSource = Mock(SqmBasicValuedSimplePath)

            QueryEngine queryEngine = Mockito.mock(QueryEngine)
            SqmFunctionRegistry sqmFunctionRegistry = Mock(SqmFunctionRegistry)
            TestInterfaceThatImplementsSqmFunctionDescriptorAndFunctionRenderingSupport ti = Mock(TestInterfaceThatImplementsSqmFunctionDescriptorAndFunctionRenderingSupport)
            Mockito.when(queryEngine.getSqmFunctionRegistry()).thenReturn(sqmFunctionRegistry)
            TypeConfiguration typeConfiguration = Mock(TypeConfiguration)
            BasicTypeRegistry basicTypeRegistry = Mock(BasicTypeRegistry)
            org.hibernate.type.BasicType basicType = Mock(org.hibernate.type.BasicType)
            List<? extends SqmTypedNode<?>> expectedArguments = new ArrayList<>()
            expectedArguments.add(referencedPathSource)
            for (String p : path) {
                org.hibernate.query.sqm.tree.expression.SqmExpression argument = Mock(org.hibernate.query.sqm.tree.expression.SqmExpression)
                expectedArguments.add(argument)
                nodeBuilder.value(p) >> argument
            }

        when:
            T tested = prepareTestObject(referencedPathSource, nodeBuilder, path)

        then:
            2 * nodeBuilder.getQueryEngine() >> queryEngine
            2 * sqmFunctionRegistry.findFunctionDescriptor(expectedFunctionName()) >> ti
            1 * nodeBuilder.getTypeConfiguration() >> typeConfiguration
            1 * typeConfiguration.getBasicTypeRegistry() >> basicTypeRegistry
            1 * basicTypeRegistry.resolve(StandardBasicTypes.STRING) >> basicType
            tested.nodeBuilder().is(nodeBuilder)
            tested.getFunctionName() == expectedFunctionName()
            tested.getArguments() == expectedArguments

        where:
            path << [["some", "property"], ["child1", "grandson1"]]
    }

    protected abstract T prepareTestObject(Path referencedPathSource, NodeBuilder nodeBuilder, List<String> path)

    protected abstract String expectedFunctionName()

    private static interface TestInterfaceThatImplementsSqmFunctionDescriptorAndFunctionRenderingSupport extends SqmFunctionDescriptor, FunctionRenderingSupport {}
}
