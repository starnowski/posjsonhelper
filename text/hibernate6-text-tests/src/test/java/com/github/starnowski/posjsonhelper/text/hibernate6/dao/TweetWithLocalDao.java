package com.github.starnowski.posjsonhelper.text.hibernate6.dao;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.text.hibernate6.functions.PlainToTSQueryFunction;
import com.github.starnowski.posjsonhelper.text.hibernate6.functions.TSVectorFunction;
import com.github.starnowski.posjsonhelper.text.hibernate6.functions.WebsearchToTSQueryFunction;
import com.github.starnowski.posjsonhelper.text.hibernate6.model.TweetWithLocale;
import com.github.starnowski.posjsonhelper.text.hibernate6.operators.TextOperatorFunction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.query.sqm.NodeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static com.github.starnowski.posjsonhelper.text.hibernate6.Application.*;

@Repository
public class TweetWithLocalDao {

    private static final Map<String, String> configurationLocaleMap = Map.of(ENGLISH_CONFIGURATION, "english", POLISH_CONFIGURATION, "polish", POLISH_EXTENDED_CONFIGURATION, "polish");

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private HibernateContext hibernateContext;

    public List<TweetWithLocale> findBySinglePlainQueryInDescriptionForConfiguration(String phrase, String configuration) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TweetWithLocale> query = cb.createQuery(TweetWithLocale.class);
        Root<TweetWithLocale> root = query.from(TweetWithLocale.class);
        query.select(root);
        TextOperatorFunction textQueryPredicate = new TextOperatorFunction((NodeBuilder) cb, new TSVectorFunction(root.get("titleAndShortContent"), configuration, (NodeBuilder) cb), new PlainToTSQueryFunction((NodeBuilder) cb, configuration, phrase), hibernateContext);
        Predicate localePredicate = cb.equal(root.get("locale"), configurationLocaleMap.get(configuration));
        query.where(cb.and(textQueryPredicate, localePredicate));
        return entityManager.createQuery(query).getResultList();
    }

    public List<TweetWithLocale> findCorrectTweetsByWebSearchToTSQueryInDescription(String phrase, String configuration) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TweetWithLocale> query = cb.createQuery(TweetWithLocale.class);
        Root<TweetWithLocale> root = query.from(TweetWithLocale.class);
        query.select(root);
        TextOperatorFunction textQueryPredicate = new TextOperatorFunction((NodeBuilder) cb, new TSVectorFunction(root.get("titleAndShortContent"), configuration, (NodeBuilder) cb), new WebsearchToTSQueryFunction((NodeBuilder) cb, configuration, phrase), hibernateContext);
        Predicate localePredicate = cb.equal(root.get("locale"), configurationLocaleMap.get(configuration));
        query.where(cb.and(textQueryPredicate, localePredicate));
        return entityManager.createQuery(query).getResultList();
    }
}
