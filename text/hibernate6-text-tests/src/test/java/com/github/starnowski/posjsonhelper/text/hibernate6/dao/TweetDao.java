package com.github.starnowski.posjsonhelper.text.hibernate6.dao;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.text.hibernate6.functions.PhraseToTSQueryFunction;
import com.github.starnowski.posjsonhelper.text.hibernate6.functions.PlainToTSQueryFunction;
import com.github.starnowski.posjsonhelper.text.hibernate6.functions.TSVectorFunction;
import com.github.starnowski.posjsonhelper.text.hibernate6.functions.WebsearchToTSQueryFunction;
import com.github.starnowski.posjsonhelper.text.hibernate6.model.Tweet;
import com.github.starnowski.posjsonhelper.text.hibernate6.operators.RegconfigTypeCastOperatorFunction;
import com.github.starnowski.posjsonhelper.text.hibernate6.operators.TextOperatorFunction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
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
        query.where(new TextOperatorFunction((NodeBuilder) cb, new TSVectorFunction(root.get("shortContent"), (NodeBuilder) cb), new PlainToTSQueryFunction((NodeBuilder) cb, (String) null, phrase), hibernateContext));
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
        query.where(new TextOperatorFunction((NodeBuilder) cb, new TSVectorFunction(root.get("shortContent"), (NodeBuilder) cb), new PhraseToTSQueryFunction((NodeBuilder) cb, (String) null, phrase), hibernateContext));
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
        query.where(new TextOperatorFunction((NodeBuilder) cb, new TSVectorFunction(root.get("shortContent"), new RegconfigTypeCastOperatorFunction((NodeBuilder) cb, configuration, hibernateContext), (NodeBuilder) cb), new PhraseToTSQueryFunction((NodeBuilder) cb, new RegconfigTypeCastOperatorFunction((NodeBuilder) cb, configuration, hibernateContext), phrase), hibernateContext));
        return entityManager.createQuery(query).getResultList();
    }

    public List<Tweet> findBySinglePlainQueryInDescriptionForConfigurationAndRegconfigTypeCastOperatorFunctionObjectInstance(String phrase, String configuration) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tweet> query = cb.createQuery(Tweet.class);
        Root<Tweet> root = query.from(Tweet.class);
        query.select(root);
        query.where(new TextOperatorFunction((NodeBuilder) cb, new TSVectorFunction(root.get("shortContent"), new RegconfigTypeCastOperatorFunction((NodeBuilder) cb, configuration, hibernateContext), (NodeBuilder) cb), new PlainToTSQueryFunction((NodeBuilder) cb, new RegconfigTypeCastOperatorFunction((NodeBuilder) cb, configuration, hibernateContext), phrase), hibernateContext));
        return entityManager.createQuery(query).getResultList();
    }

    public List<Tweet> findCorrectTweetsByWebSearchToTSQueryInDescriptionAndRegconfigTypeCastOperatorFunctionObjectInstance(String phrase, String configuration) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tweet> query = cb.createQuery(Tweet.class);
        Root<Tweet> root = query.from(Tweet.class);
        query.select(root);
        query.where(new TextOperatorFunction((NodeBuilder) cb, new TSVectorFunction(root.get("shortContent"), new RegconfigTypeCastOperatorFunction((NodeBuilder) cb, configuration, hibernateContext), (NodeBuilder) cb), new WebsearchToTSQueryFunction((NodeBuilder) cb, new RegconfigTypeCastOperatorFunction((NodeBuilder) cb, configuration, hibernateContext), phrase), hibernateContext));
        return entityManager.createQuery(query).getResultList();
    }

    public List<Tweet> findCorrectTweetsByWebSearchToTSQueryInDescription(String phrase, String configuration) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tweet> query = cb.createQuery(Tweet.class);
        Root<Tweet> root = query.from(Tweet.class);
        query.select(root);
        query.where(new TextOperatorFunction((NodeBuilder) cb, new TSVectorFunction(root.get("shortContent"), configuration, (NodeBuilder) cb), new WebsearchToTSQueryFunction((NodeBuilder) cb, configuration, phrase), hibernateContext));
        return entityManager.createQuery(query).getResultList();
    }

    public List<Tweet> findBySinglePlainQueryInDescriptionForDefaultConfigurationWithHQL(String phrase) {
        //plainto_tsquery
        String statement = String.format("from Tweet as tweet where text_operator_function(to_tsvector(tweet.shortContent), plainto_tsquery(:phrase))");
        TypedQuery<Tweet> query = entityManager.createQuery(statement, Tweet.class);
        query.setParameter("phrase", phrase);
        return query.getResultList();
    }

    public List<Tweet> findBySinglePlainQueryInDescriptionForConfigurationWithHQL(String phrase, String configuration) {
        //plainto_tsquery
        String statement = String.format("from Tweet as tweet where text_operator_function(to_tsvector('%1$s', tweet.shortContent), plainto_tsquery('%1$s', :phrase))", configuration);
        TypedQuery<Tweet> query = entityManager.createQuery(statement, Tweet.class);
        query.setParameter("phrase", phrase);
        return query.getResultList();
    }

    public List<Tweet> findBySinglePlainQueryInDescriptionForConfigurationAndRegconfigTypeCastOperatorFunctionObjectInstanceWithHQL(String phrase, String configuration) {
        //plainto_tsquery regconfig cast_operator_function
        String statement = String.format("from Tweet as tweet where text_operator_function(to_tsvector(cast_operator_function(:configuration,'regconfig'), tweet.shortContent), plainto_tsquery(cast_operator_function(:configuration,'regconfig'), :phrase))");
        TypedQuery<Tweet> query = entityManager.createQuery(statement, Tweet.class);
        query.setParameter("phrase", phrase);
        query.setParameter("configuration", configuration);
        return query.getResultList();
    }

    public List<Tweet> findBySinglePhraseInDescriptionForDefaultConfigurationWithHQL(String phrase) {
        //phraseto_tsquery
        String statement = String.format("from Tweet as tweet where text_operator_function(to_tsvector(tweet.shortContent), phraseto_tsquery(:phrase))");
        TypedQuery<Tweet> query = entityManager.createQuery(statement, Tweet.class);
        query.setParameter("phrase", phrase);
        return query.getResultList();
    }

    public List<Tweet> findBySinglePhraseInDescriptionForConfigurationWithHQL(String phrase, String configuration) {
        //phraseto_tsquery
        String statement = String.format("from Tweet as tweet where text_operator_function(to_tsvector('%1$s', tweet.shortContent), phraseto_tsquery('%1$s', :phrase))", configuration);
        TypedQuery<Tweet> query = entityManager.createQuery(statement, Tweet.class);
        query.setParameter("phrase", phrase);
        return query.getResultList();
    }

    public List<Tweet> findBySinglePhraseInDescriptionForConfigurationAndRegconfigTypeCastOperatorFunctionInstanceWithHQL(String phrase, String configuration) {
        //phraseto_tsquery regconfig cast_operator_function
        String statement = String.format("from Tweet as tweet where text_operator_function(to_tsvector(cast_operator_function(:configuration,'regconfig'), tweet.shortContent), phraseto_tsquery(cast_operator_function(:configuration,'regconfig'), :phrase))");
        TypedQuery<Tweet> query = entityManager.createQuery(statement, Tweet.class);
        query.setParameter("phrase", phrase);
        query.setParameter("configuration", configuration);
        return query.getResultList();
    }

    public List<Tweet> findCorrectTweetsByWebSearchToTSQueryInDescriptionWithHQL(String phrase, String configuration) {
        //websearch_to_tsquery
        String statement = String.format("from Tweet as tweet where text_operator_function(to_tsvector('%1$s', tweet.shortContent), websearch_to_tsquery('%1$s', :phrase))", configuration);
        TypedQuery<Tweet> query = entityManager.createQuery(statement, Tweet.class);
        query.setParameter("phrase", phrase);
        return query.getResultList();
    }

    public List<Tweet> findCorrectTweetsByWebSearchToTSQueryInDescriptionAndRegconfigTypeCastOperatorFunctionObjectInstanceWithHQL(String phrase, String configuration) {
        //websearch_to_tsquery regconfig cast_operator_function
        String statement = String.format("from Tweet as tweet where text_operator_function(to_tsvector(cast_operator_function(:configuration,'regconfig'), tweet.shortContent), websearch_to_tsquery(cast_operator_function(:configuration,'regconfig'), :phrase))");
        TypedQuery<Tweet> query = entityManager.createQuery(statement, Tweet.class);
        query.setParameter("phrase", phrase);
        query.setParameter("configuration", configuration);
        return query.getResultList();
    }
}
