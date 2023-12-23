package com.github.starnowski.posjsonhelper.text.hibernate6.dao;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.text.hibernate6.functions.PhraseToTSQueryFunction;
import com.github.starnowski.posjsonhelper.text.hibernate6.functions.PlainToTSQueryFunction;
import com.github.starnowski.posjsonhelper.text.hibernate6.functions.TSVectorFunction;
import com.github.starnowski.posjsonhelper.text.hibernate6.model.Tweet;
import com.github.starnowski.posjsonhelper.text.hibernate6.operators.RegconfigTypeCastOperatorFunction;
import com.github.starnowski.posjsonhelper.text.hibernate6.operators.TextOperatorFunction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.query.sqm.NodeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TweetDao {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private HibernateContext hibernateContext;

    public List<Tweet> findBySinglePlainQueryInDescriptionForDefaultConfiguration(String phrase) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tweet> query = cb.createQuery(Tweet.class);
        Root<Tweet> root = query.from(Tweet.class);
        query.select(root);
        query.where(new TextOperatorFunction((NodeBuilder) cb, new TSVectorFunction(root.get("shortContent"), (NodeBuilder) cb), new PlainToTSQueryFunction((NodeBuilder) cb, null, phrase), hibernateContext));
        return entityManager.createQuery(query).getResultList();
    }

    public List<Tweet> findBySinglePlainQueryInDescriptionForConfiguration(String phrase, String configuration) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tweet> query = cb.createQuery(Tweet.class);
        Root<Tweet> root = query.from(Tweet.class);
        query.select(root);
        query.where(new TextOperatorFunction((NodeBuilder) cb, new TSVectorFunction(root.get("shortContent"), configuration, (NodeBuilder) cb), new PlainToTSQueryFunction((NodeBuilder) cb, configuration, phrase), hibernateContext));
        return entityManager.createQuery(query).getResultList();
    }

    public List<Tweet> findBySinglePhraseInDescriptionForDefaultConfiguration(String phrase) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tweet> query = cb.createQuery(Tweet.class);
        Root<Tweet> root = query.from(Tweet.class);
        query.select(root);
        query.where(new TextOperatorFunction((NodeBuilder) cb, new TSVectorFunction(root.get("shortContent"), (NodeBuilder) cb), new PhraseToTSQueryFunction((NodeBuilder) cb, null, phrase), hibernateContext));
        return entityManager.createQuery(query).getResultList();
    }

    public List<Tweet> findBySinglePhraseInDescriptionForConfiguration(String phrase, String configuration) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tweet> query = cb.createQuery(Tweet.class);
        Root<Tweet> root = query.from(Tweet.class);
        query.select(root);
        query.where(new TextOperatorFunction((NodeBuilder) cb, new TSVectorFunction(root.get("shortContent"), configuration, (NodeBuilder) cb), new PhraseToTSQueryFunction((NodeBuilder) cb, configuration, phrase), hibernateContext));
        return entityManager.createQuery(query).getResultList();
    }

    public List<Tweet> findBySinglePhraseInDescriptionForConfigurationAndRegconfigTypeCastOperatorFunctionInstance(String phrase, String configuration) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tweet> query = cb.createQuery(Tweet.class);
        Root<Tweet> root = query.from(Tweet.class);
        query.select(root);
        query.where(new TextOperatorFunction((NodeBuilder) cb, new TSVectorFunction(root.get("shortContent"), new RegconfigTypeCastOperatorFunction((NodeBuilder) cb, configuration, hibernateContext), (NodeBuilder) cb), new PhraseToTSQueryFunction((NodeBuilder) cb, configuration, phrase), hibernateContext));
        return entityManager.createQuery(query).getResultList();
    }
}
