package com.github.starnowski.posjsonhelper.text.hibernate6.dao;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.text.hibernate6.model.Item;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemDao {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private HibernateContext hibernateContext;


    public List<Item> findItemsByQuery(String phrase) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> cq = cb.createQuery(Item.class);
        Root<Item> root = cq.from(Item.class);

        // Build weighted tsvector using posjsonhelper functions
        Expression<String> shortNameVec = cb.function("setweight", String.class,
                cb.function("to_tsvector", String.class, cb.literal("english"), root.get("shortName")),
                cb.literal("A")
        );

        Expression<String> fullNameVec = cb.function("setweight", String.class,
                cb.function("to_tsvector", String.class, cb.literal("english"), root.get("fullName")),
                cb.literal("B")
        );

        Expression<String> shortDescriptionVec = cb.function("setweight", String.class,
                cb.function("to_tsvector", String.class, cb.literal("english"), root.get("shortDescription")),
                cb.literal("C")
        );

        Expression<String> fullDescriptionVec = cb.function("setweight", String.class,
                cb.function("to_tsvector", String.class, cb.literal("english"), root.get("fullDescription")),
                cb.literal("D")
        );

        // Concatenate tsvectors (|| operator)
        Expression<String> fullVector = cb.function("tsvector_concat", String.class,
                shortNameVec, fullNameVec, shortDescriptionVec, fullDescriptionVec
        );

        // Build tsquery
        Expression<String> queryExpr = cb.function(
                "plainto_tsquery", String.class,
                cb.literal("english"),
                cb.literal(phrase)
        );

        // WHERE clause using @@ operator (registered as ts_match)
        Predicate matches = cb.isTrue(
                cb.function("ts_match", Boolean.class, fullVector, queryExpr)
        );

        cq.where(matches);

        // Ranking
        Expression<Double> rankExpr = cb.function(
                "ts_rank", Double.class,
                fullVector,
                queryExpr
        );

        cq.orderBy(cb.desc(rankExpr));

        return entityManager.createQuery(cq).getResultList();
    }
}
