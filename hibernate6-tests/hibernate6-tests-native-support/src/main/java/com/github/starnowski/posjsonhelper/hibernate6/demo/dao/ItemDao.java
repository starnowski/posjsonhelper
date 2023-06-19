package com.github.starnowski.posjsonhelper.hibernate6.demo.dao;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPath;
import com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPathText;
import com.github.starnowski.posjsonhelper.hibernate6.demo.model.Item;
import com.github.starnowski.posjsonhelper.hibernate6.predicates.JsonbAllArrayStringsExistPredicate;
import com.github.starnowski.posjsonhelper.hibernate6.predicates.JsonbAnyArrayStringsExistPredicate;
import com.github.starnowski.posjsonhelper.test.utils.NumericComparator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
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

    public List<Item> findAllByAnyMatchingTagsWithHQL(HashSet<String> tags) {
        String statement = String.format("from Item as item_ where %s( jsonb_extract_path( item_.jsonbContent , :param0 ) , %s(%s)) = TRUE", hibernateContext.getJsonbAnyArrayStringsExistOperator(), hibernateContext.getJsonFunctionJsonArrayOperator(), generateParameters("param", 1, tags.size()));
        TypedQuery<Item> query = entityManager.createQuery(statement, Item.class);
        query.setParameter("param0", "top_element_with_set_of_values");
        List<String> parameters = tags.stream().toList();
        for (int p = 1, i = 0; p < parameters.size() + 1; p++, i++) {
            query.setParameter("param" + p, parameters.get(i));
        }
        return query.getResultList();
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

    public List<Item> findAllByStringValueAndLikeOperatorWithHQLQuery(String expression) {
        TypedQuery<Item> query = entityManager.createQuery("from Item as item_ where jsonb_extract_path_text( item_.jsonbContent, 'string_value' ) like :expr", Item.class);
//        query.setParameter("path", "string_value");
        query.setParameter("expr", expression);
        return query.getResultList();
    }

    public List<Item> findAllThatDoNotMatchByAllMatchingTagsWithHQLQuery(Set<String> tags) {
        //top_element_with_set_of_values
        String statement = String.format("from Item as item_ where NOT ( %s( jsonb_extract_path( item_.jsonbContent, :param0 ) , %s(%s)) = TRUE ) OR jsonb_extract_path( item_.jsonbContent, 'top_element_with_set_of_values' ) IS NULL ", hibernateContext.getJsonbAllArrayStringsExistOperator(), hibernateContext.getJsonFunctionJsonArrayOperator(), generateParameters("param", 0, tags.size()));
        TypedQuery<Item> query = entityManager.createQuery(statement, Item.class);
//        query.setParameter("path", "string_value");
//        query.setParameter("expr", expression);
        query.setParameter("param0", "top_element_with_set_of_values");
        List<String> parameters = tags.stream().toList();
        for (int p = 0, i = 0; p < parameters.size(); p++, i++) {
            query.setParameter("param" + p, parameters.get(i));
        }
        return query.getResultList();
    }

    protected String generateParameters(String prefix, int index, int parametersNum)
    {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        int to = index + parametersNum;
        for (;index < to; index++) {
            if (!first) {
                sb.append(" , ");
            }
            sb.append(":");
            sb.append(prefix);
            sb.append(index);
            first = false;
        }
        return sb.toString();
    }

//    from Item as item_ where jsonb_all_array_strings_exist( jsonb_extract_path( item_.jsonbContent , :param0 ) , json_function_json_array(:param1)) = TRUE
}
