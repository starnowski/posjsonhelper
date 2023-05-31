package com.github.starnowski.posjsonhelper.hibernate6.predicates

import com.github.starnowski.posjsonhelper.core.HibernateContext
import com.github.starnowski.posjsonhelper.hibernate6.AbstractJsonBExtractPath
import com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPath
import com.github.starnowski.posjsonhelper.hibernate6.operators.JsonArrayFunction
import jakarta.persistence.criteria.Path
import org.hibernate.query.spi.QueryEngine
import org.hibernate.query.sqm.NodeBuilder
import org.hibernate.query.sqm.function.FunctionRenderingSupport
import org.hibernate.query.sqm.function.SqmFunctionDescriptor
import org.hibernate.query.sqm.function.SqmFunctionRegistry
import org.hibernate.query.sqm.internal.SqmCriteriaNodeBuilder
import org.hibernate.query.sqm.tree.SqmCopyContext
import org.hibernate.query.sqm.tree.SqmTypedNode
import org.hibernate.query.sqm.tree.domain.SqmBasicValuedSimplePath
import org.hibernate.type.BasicTypeRegistry
import org.hibernate.type.StandardBasicTypes
import org.hibernate.type.spi.TypeConfiguration
import org.mockito.Mockito
import spock.lang.Specification
import spock.lang.Unroll

abstract class AbstractJsonbArrayStringsExistPredicateTest <T extends AbstractJsonbArrayStringsExistPredicate> extends Specification {

    @Unroll
    def "should pass correct values #tags"(){
        given:
            HibernateContext context = HibernateContext.builder().build()
            SqmCriteriaNodeBuilder nodeBuilder = Mock(SqmCriteriaNodeBuilder)

            QueryEngine queryEngine = Mockito.mock(QueryEngine)
            SqmFunctionRegistry sqmFunctionRegistry = Mock(SqmFunctionRegistry)
            TestInterfaceThatImplementsSqmFunctionDescriptorAndFunctionRenderingSupport ti = Mock(TestInterfaceThatImplementsSqmFunctionDescriptorAndFunctionRenderingSupport)
            Mockito.when(queryEngine.getSqmFunctionRegistry()).thenReturn(sqmFunctionRegistry)
            TypeConfiguration typeConfiguration = Mock(TypeConfiguration)
            BasicTypeRegistry basicTypeRegistry = Mock(BasicTypeRegistry)
            org.hibernate.type.BasicType basicType = Mock(org.hibernate.type.BasicType)
            List<? extends SqmTypedNode<?>> expectedArguments = new ArrayList<>()
            for (String p : tags) {
                org.hibernate.query.sqm.tree.expression.SqmExpression argument = Mock(org.hibernate.query.sqm.tree.expression.SqmExpression)
                expectedArguments.add(argument)
                nodeBuilder.value(p) >> argument
            }
            JsonBExtractPath jsonBExtractPath = Mock(JsonBExtractPath)

        when:
            T tested = prepareTestObject(context, nodeBuilder, jsonBExtractPath, tags.toArray(new String[0]))

        then:
            4 * nodeBuilder.getQueryEngine() >> queryEngine
            2 * sqmFunctionRegistry.findFunctionDescriptor(context.getJsonFunctionJsonArrayOperator()) >> ti
            2 * sqmFunctionRegistry.findFunctionDescriptor(expectedFunctionName()) >> ti
            1 * nodeBuilder.getTypeConfiguration() >> typeConfiguration
            1 * typeConfiguration.getBasicTypeRegistry() >> basicTypeRegistry
            1 * basicTypeRegistry.resolve(StandardBasicTypes.BOOLEAN) >> basicType
            tested.nodeBuilder().is(nodeBuilder)
            tested.getFunctionName() == expectedFunctionName()
            tested.getArguments().size() == 2
            tested.getArguments()[0].is(jsonBExtractPath)
            tested.getArguments()[1].getClass() == JsonArrayFunction
            JsonArrayFunction jsonArrayFunction = tested.getArguments()[1]
            jsonArrayFunction.getArguments() == expectedArguments

        where:
            tags << [["tag1", "tag3"], ["val2"]]
    }

    @Unroll
    def "should throw exception when path argument is empty or null #tags"(){
        given:
            HibernateContext context = HibernateContext.builder().build()
            SqmCriteriaNodeBuilder nodeBuilder = Mock(SqmCriteriaNodeBuilder)

            QueryEngine queryEngine = Mockito.mock(QueryEngine)
            SqmFunctionRegistry sqmFunctionRegistry = Mock(SqmFunctionRegistry)
            TestInterfaceThatImplementsSqmFunctionDescriptorAndFunctionRenderingSupport ti = Mock(TestInterfaceThatImplementsSqmFunctionDescriptorAndFunctionRenderingSupport)
            Mockito.when(queryEngine.getSqmFunctionRegistry()).thenReturn(sqmFunctionRegistry)
            TypeConfiguration typeConfiguration = Mock(TypeConfiguration)
            BasicTypeRegistry basicTypeRegistry = Mock(BasicTypeRegistry)
            org.hibernate.type.BasicType basicType = Mock(org.hibernate.type.BasicType)
            List<? extends SqmTypedNode<?>> expectedArguments = new ArrayList<>()
            for (String p : tags) {
                org.hibernate.query.sqm.tree.expression.SqmExpression argument = Mock(org.hibernate.query.sqm.tree.expression.SqmExpression)
                expectedArguments.add(argument)
                nodeBuilder.value(p) >> argument
            }
            JsonBExtractPath jsonBExtractPath = Mock(JsonBExtractPath)

        when:
            T tested = prepareTestObject(context, nodeBuilder, jsonBExtractPath, tags == null ? null : tags.toArray(new String[0]))

        then:
            2 * nodeBuilder.getQueryEngine() >> queryEngine
            2 * sqmFunctionRegistry.findFunctionDescriptor(expectedFunctionName()) >> ti
            def ex = thrown(IllegalArgumentException)
            ex.message == "Values can not be null or empty list"

        where:
            tags << [null, []]
    }

    protected abstract T prepareTestObject(HibernateContext context, NodeBuilder nodeBuilder, JsonBExtractPath jsonBExtractPath, String[] values)

    protected abstract String expectedFunctionName()

    private static interface TestInterfaceThatImplementsSqmFunctionDescriptorAndFunctionRenderingSupport extends SqmFunctionDescriptor, FunctionRenderingSupport {}
}
