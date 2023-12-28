package com.github.starnowski.posjsonhelper.text.hibernate6.operators;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.operators.CastOperatorFunction;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.tree.expression.SqmExpression;

public class RegconfigTypeCastOperatorFunction extends CastOperatorFunction {
    public RegconfigTypeCastOperatorFunction(NodeBuilder nodeBuilder, String value, HibernateContext hibernateContext) {
        super(nodeBuilder, (SqmExpression<?>) nodeBuilder.value(value), "regconfig", hibernateContext);
    }
}
