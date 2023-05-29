package com.github.starnowski.posjsonhelper.hibernate6.demo.dao;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPath;
import com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPathText;
import com.github.starnowski.posjsonhelper.hibernate6.demo.model.Item;
import com.github.starnowski.posjsonhelper.hibernate6.predicates.JsonbAllArrayStringsExistPredicate;
import com.github.starnowski.posjsonhelper.hibernate6.predicates.JsonbAnyArrayStringsExistPredicate;
import com.github.starnowski.posjsonhelper.test.utils.NumericComparator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.tree.expression.SqmExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Collections.singletonList;

@Repository
public class ItemDao {

    @Autowired
    private HibernateContext hibernateContext;
    @Autowired
    private EntityManager entityManager;

    public List<Item> findAllByAllMatchingTags(Set<String> tags) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> query = cb.createQuery(Item.class);
        Root<Item> root = query.from(Item.class);
        query.select(root);
        query.where(new JsonbAllArrayStringsExistPredicate(hibernateContext, (NodeBuilder) cb, new JsonBExtractPath(root.get("jsonbContent"), (NodeBuilder) cb, singletonList("top_element_with_set_of_values")), tags.toArray(new String[0])));
        return entityManager.createQuery(query).getResultList();
    }

    public List<Item> findAllThatDoNotMatchByAllMatchingTags(Set<String> tags) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> query = cb.createQuery(Item.class);
        Root<Item> root = query.from(Item.class);
        query.select(root);
        Predicate notAllMatchingTags = cb.not(new JsonbAllArrayStringsExistPredicate(hibernateContext, (NodeBuilder) cb, new JsonBExtractPath(root.get("jsonbContent"), (NodeBuilder) cb, singletonList("top_element_with_set_of_values")), tags.toArray(new String[0])));
        Predicate withoutSetOfValuesProperty = cb.isNull(new JsonBExtractPath(root.get("jsonbContent"), (NodeBuilder) cb, singletonList("top_element_with_set_of_values")));
        query.where(cb.or(withoutSetOfValuesProperty, notAllMatchingTags));
        return entityManager.createQuery(query).getResultList();
    }

    public List<Item> findAllByAnyMatchingTags(HashSet<String> tags) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> query = cb.createQuery(Item.class);
        Root<Item> root = query.from(Item.class);
        query.select(root);
        query.where(new JsonbAnyArrayStringsExistPredicate(hibernateContext, (NodeBuilder) cb, new JsonBExtractPath(root.get("jsonbContent"), (NodeBuilder) cb, singletonList("top_element_with_set_of_values")), tags.toArray(new String[0])));
        return entityManager.createQuery(query).getResultList();
    }

    public List<Item> findAllByNumericValue(BigDecimal bigDecimal, NumericComparator numericComparator) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        NodeBuilder nodeBuilder = (NodeBuilder) cb;
        CriteriaQuery<Item> query = cb.createQuery(Item.class);
        Root<Item> root = query.from(Item.class);
        query.select(root);
        SqmExpression castFunction = nodeBuilder.cast(new JsonBExtractPathText(root.get("jsonbContent"), singletonList("double_value"), nodeBuilder), BigDecimal.class);
        switch (numericComparator) {
            case EQ:
                query.where(cb.equal(castFunction, bigDecimal));
                break;
            case GE:
                query.where(cb.ge(castFunction, bigDecimal));
                break;
            case GT:
                query.where(cb.gt(castFunction, bigDecimal));
                break;
            case LE:
                query.where(cb.le(castFunction, bigDecimal));
                break;
            case LT:
                query.where(cb.lt(castFunction, bigDecimal));
                break;
        }
        return entityManager.createQuery(query).getResultList();
    }

    public List<Item> findAllByStringThatMatchInValues(List<String> strings) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> query = cb.createQuery(Item.class);
        Root<Item> root = query.from(Item.class);
        query.select(root);
        query.where((new JsonBExtractPathText(root.get("jsonbContent"), singletonList("enum_value"), (NodeBuilder) cb)).in(strings));
        return entityManager.createQuery(query).getResultList();
    }

    public List<Item> findAllByStringValueAndLikeOperator(String expression) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> query = cb.createQuery(Item.class);
        Root<Item> root = query.from(Item.class);
        query.select(root);
        query.where(cb.like(new JsonBExtractPathText(root.get("jsonbContent"), singletonList("string_value"), (NodeBuilder) cb), expression));
        return entityManager.createQuery(query).getResultList();
    }
}
