package com.github.starnowski.posjsonhelper.text.hibernate6.dao;

import com.github.starnowski.posjsonhelper.text.hibernate6.model.Tweet;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TweetDao {

    @Autowired
    private EntityManager entityManager;

    public List<Tweet> findBySinglePhraseInDescriptionForDefaultConfiguration(String phrase)
    {
        //TODO
        return null;
    }
}
