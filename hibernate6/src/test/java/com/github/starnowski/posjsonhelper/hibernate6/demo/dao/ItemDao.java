package com.github.starnowski.posjsonhelper.hibernate6.demo.dao;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.demo.model.Item;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.query.sqm.NodeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemDao {

    @Autowired
    private HibernateContext hibernateContext;
    @Autowired
    private EntityManager entityManager;

//    public List<Item> findAllByAllMatchingTags(Set<String> tags) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Item> query = cb.createQuery(Item.class);
//        Root<Item> root = query.from(Item.class);
//        query.select(root);
//        query.where(new JsonbAllArrayStringsExistPredicate(hibernateContext, (CriteriaBuilderImpl) cb, new JsonBExtractPath((CriteriaBuilderImpl) cb, singletonList("top_element_with_set_of_values"), root.get("jsonbContent")), tags.toArray(new String[0])));
//        return entityManager.createQuery(query).getResultList();
//    }

//    public List<Item> findAllThatDoNotMatchByAllMatchingTags(Set<String> tags) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Item> query = cb.createQuery(Item.class);
//        Root<Item> root = query.from(Item.class);
//        query.select(root);
//        Predicate notAllMatchingTags = cb.not(new JsonbAllArrayStringsExistPredicate(hibernateContext, (CriteriaBuilderImpl) cb, new JsonBExtractPath((CriteriaBuilderImpl) cb, singletonList("top_element_with_set_of_values"), root.get("jsonbContent")), tags.toArray(new String[0])));
//        Predicate withoutSetOfValuesProperty = cb.isNull(new JsonBExtractPath((CriteriaBuilderImpl) cb, singletonList("top_element_with_set_of_values"), root.get("jsonbContent")));
//        query.where(cb.or(withoutSetOfValuesProperty, notAllMatchingTags));
//        return entityManager.createQuery(query).getResultList();
//    }

//    public List<Item> findAllByAnyMatchingTags(HashSet<String> tags) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Item> query = cb.createQuery(Item.class);
//        Root<Item> root = query.from(Item.class);
//        query.select(root);
//        query.where(new JsonbAnyArrayStringsExistPredicate(hibernateContext, (CriteriaBuilderImpl) cb, new JsonBExtractPath((CriteriaBuilderImpl) cb, singletonList("top_element_with_set_of_values"), root.get("jsonbContent")), tags.toArray(new String[0])));
//        return entityManager.createQuery(query).getResultList();
//    }

//    public List<Item> findAllByNumericValue(BigDecimal bigDecimal, NumericComparator numericComparator) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        NodeBuilder nodeBuilder = (NodeBuilder) cb;
//        CriteriaQuery<Item> query = cb.createQuery(Item.class);
//        Root<Item> root = query.from(Item.class);
//        query.select(root);
//        CastFunction castFunction = new CastFunction(cb, BigDecimal.class, new JsonBExtractPathText((CriteriaBuilderImpl) cb, singletonList("double_value"), root.get("jsonbContent")));
//        switch (numericComparator) {
//            case EQ:
//                query.where(cb.equal(castFunction, bigDecimal));
//                break;
//            case GE:
//                query.where(cb.ge(castFunction, bigDecimal));
//                break;
//            case GT:
//                query.where(cb.gt(castFunction, bigDecimal));
//                break;
//            case LE:
//                query.where(cb.le(castFunction, bigDecimal));
//                break;
//            case LT:
//                query.where(cb.lt(castFunction, bigDecimal));
//                break;
//        }
//        return entityManager.createQuery(query).getResultList();
//    }

    public List<Item> findAllByStringThatMatchInValues(List<String> strings) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> query = cb.createQuery(Item.class);
        Root<Item> root = query.from(Item.class);
        NodeBuilder nodeBuilder = (NodeBuilder) cb;
        query.select(root);
//        query.where((new JsonBExtractPathText(root.get("jsonbContent"), nodeBuilder, singletonList("enum_value"))).in(strings));
        query.where((root.get("jsonbContent")).in(strings));
        return entityManager.createQuery(query).getResultList();
    }

    public List<Item> findAllByStringValueAndLikeOperator(String expression) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> query = cb.createQuery(Item.class);
        Root<Item> root = query.from(Item.class);
        query.select(root);
//        query.where(cb.like(new JsonBExtractPathText((CriteriaBuilderImpl) cb, singletonList("string_value"), root.get("jsonbContent")), expression));
        query.where(cb.like(root.get("jsonbContent"), expression));
        return entityManager.createQuery(query).getResultList();
    }
}
