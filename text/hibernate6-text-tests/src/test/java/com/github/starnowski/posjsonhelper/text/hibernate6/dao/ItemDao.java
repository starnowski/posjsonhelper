package com.github.starnowski.posjsonhelper.text.hibernate6.dao;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.text.hibernate6.functions.TSVectorFunction;
import com.github.starnowski.posjsonhelper.text.hibernate6.functions.WebsearchToTSQueryFunction;
import com.github.starnowski.posjsonhelper.text.hibernate6.model.Item;
import com.github.starnowski.posjsonhelper.text.hibernate6.operators.RegconfigTypeCastOperatorFunction;
import com.github.starnowski.posjsonhelper.text.hibernate6.operators.TextOperatorFunction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.tree.expression.SqmExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.github.starnowski.posjsonhelper.text.hibernate6.Application.ENGLISH_CONFIGURATION;

@Repository
public class ItemDao {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private HibernateContext hibernateContext;


    public List<Item> findItemsByQuery(String phrase, boolean ascSort) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> cq = cb.createQuery(Item.class);
        Root<Item> root = cq.from(Item.class);

        // Build weighted tsvector using posjsonhelper functions
        Expression<String> shortNameVec = cb.function("setweight", String.class,
                new TSVectorFunction(root.get("shortName"), new RegconfigTypeCastOperatorFunction((NodeBuilder) cb, ENGLISH_CONFIGURATION, hibernateContext), (NodeBuilder) cb),
                cb.literal("A")
        );

        Expression<String> fullNameVec = cb.function("setweight", String.class,
                new TSVectorFunction(root.get("fullName"), new RegconfigTypeCastOperatorFunction((NodeBuilder) cb, ENGLISH_CONFIGURATION, hibernateContext), (NodeBuilder) cb),
                cb.literal("B")
        );

        Expression<String> shortDescriptionVec = cb.function("setweight", String.class,
                new TSVectorFunction(root.get("shortDescription"), new RegconfigTypeCastOperatorFunction((NodeBuilder) cb, ENGLISH_CONFIGURATION, hibernateContext), (NodeBuilder) cb),
                cb.literal("C")
        );

        Expression<String> fullDescriptionVec = cb.function("setweight", String.class,
                new TSVectorFunction(root.get("fullDescription"), new RegconfigTypeCastOperatorFunction((NodeBuilder) cb, ENGLISH_CONFIGURATION, hibernateContext), (NodeBuilder) cb),
                cb.literal("D")
        );

        // Concatenate tsvectors (|| operator)
        SqmExpression<String> fullVector = (SqmExpression<String>) cb.concat(cb.concat(shortNameVec, fullNameVec), cb.concat(shortDescriptionVec, fullDescriptionVec));

        // Build tsquery
        Expression<String> queryExpr = new WebsearchToTSQueryFunction((NodeBuilder) cb, ENGLISH_CONFIGURATION, phrase);

        // WHERE clause using @@ operator
        TextOperatorFunction matches = new TextOperatorFunction((NodeBuilder) cb, fullVector, new WebsearchToTSQueryFunction((NodeBuilder) cb, new RegconfigTypeCastOperatorFunction((NodeBuilder) cb, ENGLISH_CONFIGURATION, hibernateContext), phrase), hibernateContext);

        cq.where(matches);

        // Ranking
        Expression<Double> rankExpr = cb.function(
                "ts_rank", Double.class,
                fullVector,
                queryExpr
        );

        cq.orderBy(ascSort ? cb.asc(rankExpr) : cb.desc(rankExpr));

        return entityManager.createQuery(cq).getResultList();
    }
}
