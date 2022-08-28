package com.github.starnowski.posjsonhelper.hibernate5.demo.dao;

import com.github.starnowski.posjsonhelper.hibernate5.demo.model.Item;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public class ItemDao {

    public List<Item> findAllByAllMatchingTags(Set<String> tags) {
        //TODO
        return null;
    }
}
