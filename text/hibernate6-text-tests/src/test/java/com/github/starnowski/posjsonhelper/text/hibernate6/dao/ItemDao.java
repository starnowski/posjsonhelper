package com.github.starnowski.posjsonhelper.text.hibernate6.dao;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.text.hibernate6.functions.TSVectorFunction;
import com.github.starnowski.posjsonhelper.text.hibernate6.functions.WebsearchToTSQueryFunction;
import com.github.starnowski.posjsonhelper.text.hibernate6.model.Item;
import com.github.starnowski.posjsonhelper.text.hibernate6.model.Tweet;
import com.github.starnowski.posjsonhelper.text.hibernate6.operators.RegconfigTypeCastOperatorFunction;
import com.github.starnowski.posjsonhelper.text.hibernate6.operators.TextOperatorFunction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
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


    public List<Item> findItemsByWebSearchToTSQuerySortedByTsRank(String phrase, boolean ascSort) {
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

    public List<Item> findItemsByWebSearchToTSQuerySortedByTsRankInHQL(String phrase, boolean ascSort) {
        StringBuilder statement = new StringBuilder();
        statement.append("from Item as item where ");
        statement.append("text_operator_function("); // text_operator_function - start
        statement.append("concat("); // main concat - start
        statement.append("concat("); // first concat - start
        statement.append("function('setweight', to_tsvector('%1$s', item.shortName), literal('A'))");
        statement.append(",");
        statement.append("function('setweight', to_tsvector('%1$s', item.fullName), literal('B'))");
        statement.append(")"); // first concat - end
        statement.append(","); // main concat - separator
        statement.append("concat("); // second concat - start
        statement.append("function('setweight', to_tsvector('%1$s', item.shortDescription), literal('C'))");
        statement.append(",");
        statement.append("function('setweight', to_tsvector('%1$s', item.fullDescription), literal('D'))");
        statement.append(")"); // first second - end
        statement.append(")"); // main concat - end
        statement.append(","); // text_operator_function - separator
        statement.append(")"); // text_operator_function - end

        TypedQuery<Item> query = entityManager.createQuery(statement.toString().formatted(ENGLISH_CONFIGURATION), Item.class);
        query.setParameter("phrase", phrase);
        return query.getResultList();
    }
}
