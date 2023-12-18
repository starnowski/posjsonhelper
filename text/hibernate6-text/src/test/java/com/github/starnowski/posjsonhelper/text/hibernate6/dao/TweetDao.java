package com.github.starnowski.posjsonhelper.text.hibernate6.dao;

import com.github.starnowski.posjsonhelper.text.hibernate6.model.Tweet;

import javax.persistence.EntityManager;
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
