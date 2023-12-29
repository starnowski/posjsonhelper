package com.github.starnowski.posjsonhelper.hibernate6.descriptor

import com.github.starnowski.posjsonhelper.hibernate6.AbstractJsonBExtractPath
import jakarta.persistence.criteria.Path
import org.hibernate.query.spi.QueryEngine
import org.hibernate.query.sqm.function.SqmFunctionRegistry
import org.hibernate.query.sqm.internal.SqmCriteriaNodeBuilder
import org.hibernate.query.sqm.tree.SqmTypedNode
import org.hibernate.type.BasicTypeRegistry
import org.hibernate.type.StandardBasicTypes
import org.hibernate.type.spi.TypeConfiguration
import spock.lang.Specification

abstract class AbstractJsonBExtractPathDescriptorTest<T extends AbstractJsonBExtractPathDescriptor> extends Specification {

    def "should generate SqmFunctionExpression"(){
        given:
            T tested = generateTestedObject()
            List<? extends SqmTypedNode<?>> arguments = new ArrayList<>()
            TestPath path = Mock(TestPath)
            SqmTypedNode arg = Mock(SqmTypedNode)
            arguments.add(path)
            arguments.add(arg)
            QueryEngine queryEngine = Mock(QueryEngine)
            SqmCriteriaNodeBuilder sqmCriteriaNodeBuilder = Mock(SqmCriteriaNodeBuilder)
            SqmFunctionRegistry sqmFunctionRegistry = Mock(SqmFunctionRegistry)
            TypeConfiguration typeConfiguration = Mock(TypeConfiguration)
            BasicTypeRegistry basicTypeRegistry = Mock(BasicTypeRegistry)
            org.hibernate.type.BasicType basicType = Mock(org.hibernate.type.BasicType)

        when:
            def result = tested.generateSqmFunctionExpression(arguments, null, queryEngine)

        then:
            queryEngine.getCriteriaBuilder() >> sqmCriteriaNodeBuilder
            sqmCriteriaNodeBuilder.getQueryEngine() >> queryEngine
            queryEngine.getSqmFunctionRegistry() >> sqmFunctionRegistry
            sqmFunctionRegistry.findFunctionDescriptor(tested.getName()) >> tested
            sqmCriteriaNodeBuilder.getTypeConfiguration() >> typeConfiguration
            typeConfiguration.getBasicTypeRegistry() >> basicTypeRegistry
            basicTypeRegistry.resolve(StandardBasicTypes.STRING) >> basicType
            result != null
    }

    abstract protected T generateTestedObject()

    static interface TestPath extends Path, SqmTypedNode{}
}
