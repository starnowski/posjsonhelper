package com.github.starnowski.posjsonhelper.hibernate6.operators;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.tree.expression.SqmExpression;

public class JsonbCastOperatorFunction extends CastOperatorFunction {

    /**
     * @param nodeBuilder      component of type {@link NodeBuilder}
     * @param value            value for cast operation
     * @param hibernateContext context object of type {@link HibernateContext}
     */
    public JsonbCastOperatorFunction(NodeBuilder nodeBuilder, String value, HibernateContext hibernateContext) {
        super(nodeBuilder, (SqmExpression<?>) nodeBuilder.value(value), "jsonb", hibernateContext);
    }
}