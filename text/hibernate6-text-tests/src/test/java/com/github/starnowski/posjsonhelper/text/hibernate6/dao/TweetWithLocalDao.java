package com.github.starnowski.posjsonhelper.text.hibernate6.dao;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.text.hibernate6.functions.PlainToTSQueryFunction;
import com.github.starnowski.posjsonhelper.text.hibernate6.functions.TSVectorFunction;
import com.github.starnowski.posjsonhelper.text.hibernate6.functions.WebsearchToTSQueryFunction;
import com.github.starnowski.posjsonhelper.text.hibernate6.model.Tweet;
import com.github.starnowski.posjsonhelper.text.hibernate6.model.TweetWithLocale;
import com.github.starnowski.posjsonhelper.text.hibernate6.operators.TextOperatorFunction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
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

    public List<TweetWithLocale> findCorrectTweetsByWebSearchToTSQueryForConcatenatedString(String phrase, String configuration) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        NodeBuilder nb = (NodeBuilder) entityManager.getCriteriaBuilder();
        CriteriaQuery<TweetWithLocale> query = cb.createQuery(TweetWithLocale.class);
        Root<TweetWithLocale> root = query.from(TweetWithLocale.class);
        query.select(root);
        TextOperatorFunction textQueryPredicate = new TextOperatorFunction(nb, new TSVectorFunction(nb.concat(nb.concat(root.get("shortContent"), " "), root.get("title")), configuration, nb), new WebsearchToTSQueryFunction(nb, configuration, phrase), hibernateContext);
        Predicate localePredicate = cb.equal(root.get("locale"), configurationLocaleMap.get(configuration));
        query.where(cb.and(textQueryPredicate, localePredicate));
        return entityManager.createQuery(query).getResultList();
    }

    public List<TweetWithLocale> findCorrectTweetsByWebSearchToTSQueryForConcatenatedStringInHQL(String phrase, String configuration) {
        String statement = "from TweetWithLocale as tweet where tweet.locale = :locale and text_operator_function(to_tsvector(cast_operator_function(:configuration,'regconfig'), concat(tweet.shortContent, ' ', tweet.title)), websearch_to_tsquery(cast_operator_function(:configuration,'regconfig'), :phrase) )";
        TypedQuery<TweetWithLocale> query = entityManager.createQuery(statement, TweetWithLocale.class);
        query.setParameter("phrase", phrase);
        query.setParameter("configuration", configuration);
        query.setParameter("locale", configurationLocaleMap.get(configuration));
        return query.getResultList();
    }
}
