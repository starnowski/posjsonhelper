package com.github.starnowski.posjsonhelper.hibernate5.demo.dao;

import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.hibernate5.JsonBExtractPath;
import com.github.starnowski.posjsonhelper.hibernate5.demo.model.Item;
import com.github.starnowski.posjsonhelper.hibernate5.predicates.JsonbAllArrayStringsExistPredicate;
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

import static java.util.Collections.singletonList;

@Repository
public class ItemDao {

    @Autowired
    private EntityManager entityManager;

    public List<Item> findAllByAllMatchingTags(Set<String> tags) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> query = cb.createQuery(Item.class);
        Root<Item> root = query.from(Item.class);
        query.select(root);
        query.where(new JsonbAllArrayStringsExistPredicate(new Context(), (CriteriaBuilderImpl) cb, new JsonBExtractPath((CriteriaBuilderImpl) cb, singletonList("top_element_with_set_of_values"), root.get("jsonbContent")), tags.toArray(new String[0])));
        return entityManager.createQuery(query).getResultList();
    }
}
