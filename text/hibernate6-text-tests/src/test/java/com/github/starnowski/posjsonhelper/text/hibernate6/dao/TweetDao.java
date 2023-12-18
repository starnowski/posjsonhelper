package com.github.starnowski.posjsonhelper.text.hibernate6.dao;

import com.github.starnowski.posjsonhelper.text.hibernate6.model.Tweet;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TweetDao {

    @Autowired
    private EntityManager entityManager;

    public List<Tweet> findBySinglePhraseInDescriptionForDefaultConfiguration(String phrase)
    {
        //TODO
        return null;
    }
}
