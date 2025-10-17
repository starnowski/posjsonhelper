package com.github.starnowski.posjsonhelper.text.hibernate6.dao;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.text.hibernate6.model.Item;
import jakarta.persistence.EntityManager;
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
        return null;
    }
}
