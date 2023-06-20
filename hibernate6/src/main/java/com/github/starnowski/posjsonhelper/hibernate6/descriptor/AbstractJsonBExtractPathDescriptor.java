package com.github.starnowski.posjsonhelper.hibernate6.descriptor;

import com.github.starnowski.posjsonhelper.hibernate6.AbstractJsonBExtractPath;
import jakarta.persistence.criteria.Path;
import org.hibernate.query.ReturnableType;
import org.hibernate.query.spi.QueryEngine;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.function.NamedSqmFunctionDescriptor;
import org.hibernate.query.sqm.function.SelfRenderingSqmFunction;
import org.hibernate.query.sqm.tree.SqmTypedNode;
import org.hibernate.type.spi.TypeConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Function descriptor for child type of {@link AbstractJsonBExtractPath}
 *
 * @param <T> child type for {@link AbstractJsonBExtractPath}
 */
public abstract class AbstractJsonBExtractPathDescriptor<T extends AbstractJsonBExtractPath> extends NamedSqmFunctionDescriptor {

    public AbstractJsonBExtractPathDescriptor(String functionName) {
        super(functionName, false, null, null);
    }


    abstract protected T generateAbstractJsonBExtractPathImpl(Path referencedPathSource, List<SqmTypedNode<?>> pathArguments, NodeBuilder nodeBuilder);

    protected <T> SelfRenderingSqmFunction<T> generateSqmFunctionExpression(List<? extends SqmTypedNode<?>> arguments, ReturnableType<T> impliedResultType, QueryEngine queryEngine, TypeConfiguration typeConfiguration) {
        List<SqmTypedNode<?>> pathArguments = new ArrayList<>();
        for (int i = 1; i < arguments.size(); i++) {
            pathArguments.add(arguments.get(i));
        }
        return generateAbstractJsonBExtractPathImpl((Path) arguments.get(0), pathArguments, queryEngine.getCriteriaBuilder());
    }
}
