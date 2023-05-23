package com.github.starnowski.posjsonhelper.hibernate6.demo.dao;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPathText;
import com.github.starnowski.posjsonhelper.hibernate6.demo.model.Item;
import com.github.starnowski.posjsonhelper.test.utils.NumericComparator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.SqmPathSource;
import org.hibernate.query.sqm.tree.expression.SqmExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

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
        query.where(new JsonbAllArrayStringsExistPredicate(hibernateContext, (CriteriaBuilderImpl) cb, new JsonBExtractPath((CriteriaBuilderImpl) cb, singletonList("top_element_with_set_of_values"), root.get("jsonbContent")), tags.toArray(new String[0])));
        return entityManager.createQuery(query).getResultList();
    }

    public List<Item> findAllThatDoNotMatchByAllMatchingTags(Set<String> tags) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> query = cb.createQuery(Item.class);
        Root<Item> root = query.from(Item.class);
        query.select(root);
        Predicate notAllMatchingTags = cb.not(new JsonbAllArrayStringsExistPredicate(hibernateContext, (CriteriaBuilderImpl) cb, new JsonBExtractPath((CriteriaBuilderImpl) cb, singletonList("top_element_with_set_of_values"), root.get("jsonbContent")), tags.toArray(new String[0])));
        Predicate withoutSetOfValuesProperty = cb.isNull(new JsonBExtractPath((CriteriaBuilderImpl) cb, singletonList("top_element_with_set_of_values"), root.get("jsonbContent")));
        query.where(cb.or(withoutSetOfValuesProperty, notAllMatchingTags));
        return entityManager.createQuery(query).getResultList();
    }

    public List<Item> findAllByAnyMatchingTags(HashSet<String> tags) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> query = cb.createQuery(Item.class);
        Root<Item> root = query.from(Item.class);
        query.select(root);
        query.where(new JsonbAnyArrayStringsExistPredicate(hibernateContext, (CriteriaBuilderImpl) cb, new JsonBExtractPath((CriteriaBuilderImpl) cb, singletonList("top_element_with_set_of_values"), root.get("jsonbContent")), tags.toArray(new String[0])));
        return entityManager.createQuery(query).getResultList();
    }

    public List<Item> findAllByNumericValue(BigDecimal bigDecimal, NumericComparator numericComparator) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        NodeBuilder nodeBuilder = (NodeBuilder) cb;
        CriteriaQuery<Item> query = cb.createQuery(Item.class);
        Root<Item> root = query.from(Item.class);
        query.select(root);
        SqmExpression castFunction = nodeBuilder.cast(new JsonBExtractPathText(root.get("jsonbContent"), nodeBuilder, singletonList("double_value")), BigDecimal.class);
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
        query.where((new JsonBExtractPathText((CriteriaBuilderImpl) cb, singletonList("enum_value"), root.get("jsonbContent"))).in(strings));
        return entityManager.createQuery(query).getResultList();
    }

    public List<Item> findAllByStringValueAndLikeOperator(String expression) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> query = cb.createQuery(Item.class);
        Root<Item> root = query.from(Item.class);
        query.select(root);
        query.where(cb.like(new JsonBExtractPathText((CriteriaBuilderImpl) cb, singletonList("string_value"), root.get("jsonbContent")), expression));
        return entityManager.createQuery(query).getResultList();
    }
}
