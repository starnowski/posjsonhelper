package com.github.starnowski.posjsonhelper.hibernate6.demo.dao;

import com.github.starnowski.posjsonhelper.core.HibernateContext;
import com.github.starnowski.posjsonhelper.hibernate6.Hibernate6JsonUpdateStatementBuilder;
import com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPath;
import com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPathText;
import com.github.starnowski.posjsonhelper.hibernate6.demo.model.Item;
import com.github.starnowski.posjsonhelper.hibernate6.functions.JsonbSetFunction;
import com.github.starnowski.posjsonhelper.hibernate6.operators.ConcatenateJsonbOperator;
import com.github.starnowski.posjsonhelper.hibernate6.predicates.JsonbAllArrayStringsExistPredicate;
import com.github.starnowski.posjsonhelper.hibernate6.predicates.JsonbAnyArrayStringsExistPredicate;
import com.github.starnowski.posjsonhelper.json.core.sql.JsonTextArrayBuilder;
import com.github.starnowski.posjsonhelper.test.utils.NumericComparator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.tree.expression.SqmExpression;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
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
        TypedQuery<Item> query = entityManager.createQuery("from Item as item0_ where jsonb_extract_path_text( item0_.jsonbContent , :path ) like :expr", Item.class);
        query.setParameter("path", "string_value");
        query.setParameter("expr", expression);
        return query.getResultList();
    }

    public List<Item> findAllThatDoNotMatchByAllMatchingTagsWithHQLQuery(Set<String> tags) {
        //top_element_with_set_of_values
        String statement = String.format("from Item as item_ where NOT ( %s( jsonb_extract_path( item_.jsonbContent , :param0 ) , %s(%s)) = TRUE ) OR jsonb_extract_path( item_.jsonbContent , :param0 ) IS NULL ", hibernateContext.getJsonbAllArrayStringsExistOperator(), hibernateContext.getJsonFunctionJsonArrayOperator(), generateParameters("param", 1, tags.size()));
        TypedQuery<Item> query = entityManager.createQuery(statement, Item.class);
        query.setParameter("param0", "top_element_with_set_of_values");
        List<String> parameters = tags.stream().toList();
        for (int p = 1, i = 0; p < parameters.size() + 1; p++, i++) {
            query.setParameter("param" + p, parameters.get(i));
        }
        return query.getResultList();
    }

    public List<Item> findAllByAnyMatchingTagsInInnerElement(HashSet<String> tags) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> query = cb.createQuery(Item.class);
        Root<Item> root = query.from(Item.class);
        query.select(root);
        query.where(new JsonbAnyArrayStringsExistPredicate(hibernateContext, (NodeBuilder) cb, new JsonBExtractPath(root.get("jsonbContent"), (NodeBuilder) cb, asList("child", "pets")), tags.toArray(new String[0])));
        return entityManager.createQuery(query).getResultList();
    }

    public Item findById(Long id)
    {
        return entityManager.find(Item.class, id);
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

    @Transactional
    public void updateJsonPropertyForItem(Long itemId, String property, String value) throws JSONException {
        CriteriaUpdate<Item> criteriaUpdate = entityManager.getCriteriaBuilder().createCriteriaUpdate(Item.class);
        Root<Item> root = criteriaUpdate.from(Item.class);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("child", new JSONObject());
        jsonObject.getJSONObject("child").put(property, value);
        criteriaUpdate.set("jsonbContent", new ConcatenateJsonbOperator((NodeBuilder) entityManager.getCriteriaBuilder(), root.get("jsonbContent"), jsonObject.toString(), hibernateContext));

        criteriaUpdate.where(entityManager.getCriteriaBuilder().equal(root.get("id"), itemId));

        int updatedEntities = entityManager.createQuery(criteriaUpdate).executeUpdate();
    }

    @Transactional
    public void updateJsonBySettingPropertyForItem(Long itemId, String property, String value) {
        CriteriaUpdate<Item> criteriaUpdate = entityManager.getCriteriaBuilder().createCriteriaUpdate(Item.class);
        Root<Item> root = criteriaUpdate.from(Item.class);

        // Set the property you want to update and the new value
        criteriaUpdate.set("jsonbContent", new JsonbSetFunction((NodeBuilder) entityManager.getCriteriaBuilder(), root.get("jsonbContent"), new JsonTextArrayBuilder().append("child").append(property).build().toString(), JSONObject.quote(value), hibernateContext));

        // Add any conditions to restrict which entities will be updated
        criteriaUpdate.where(entityManager.getCriteriaBuilder().equal(root.get("id"), itemId));

        // Execute the update
        int updatedEntities = entityManager.createQuery(criteriaUpdate).executeUpdate();
    }

    @Transactional
    public void updateJsonBySettingPropertyForItem(Long itemId, List<AbstractItemDaoTest.JsonBSetTestPair> pairs) {
        CriteriaUpdate<Item> criteriaUpdate = entityManager.getCriteriaBuilder().createCriteriaUpdate(Item.class);
        Root<Item> root = criteriaUpdate.from(Item.class);

        JsonbSetFunction current = null;

        for (AbstractItemDaoTest.JsonBSetTestPair pair : pairs) {
            if (current == null) {
                current = new JsonbSetFunction((NodeBuilder) entityManager.getCriteriaBuilder(), root.get("jsonbContent"), pair.getJsonbSetFunctionJsonPathBuilder().build().toString(), pair.getJsonValue(), hibernateContext);
            } else {
                current = new JsonbSetFunction((NodeBuilder) entityManager.getCriteriaBuilder(), current, pair.getJsonbSetFunctionJsonPathBuilder().build().toString(), pair.getJsonValue(), hibernateContext);
            }
        }

        // Set the property you want to update and the new value
        criteriaUpdate.set("jsonbContent", current);

        // Add any conditions to restrict which entities will be updated
        criteriaUpdate.where(entityManager.getCriteriaBuilder().equal(root.get("id"), itemId));

        // Execute the update
        int updatedEntities = entityManager.createQuery(criteriaUpdate).executeUpdate();
    }

    @Transactional
    public void updateJsonBySettingPropertyForItemWithHibernate6JsonUpdateStatementBuilder(Long itemId, List<AbstractItemDaoTest.JsonBSetTestPair> pairs) {
        CriteriaUpdate<Item> criteriaUpdate = entityManager.getCriteriaBuilder().createCriteriaUpdate(Item.class);
        Root<Item> root = criteriaUpdate.from(Item.class);

        Hibernate6JsonUpdateStatementBuilder hibernate6JsonUpdateStatementBuilder = new Hibernate6JsonUpdateStatementBuilder(root.get("jsonbContent"), (NodeBuilder) entityManager.getCriteriaBuilder(), hibernateContext);
        for (AbstractItemDaoTest.JsonBSetTestPair pair : pairs) {
            hibernate6JsonUpdateStatementBuilder.appendJsonbSet(pair.getJsonbSetFunctionJsonPathBuilder().build(), pair.getJsonValue());
        }

        // Set the property you want to update and the new value
        criteriaUpdate.set("jsonbContent", hibernate6JsonUpdateStatementBuilder.build());

        // Add any conditions to restrict which entities will be updated
        criteriaUpdate.where(entityManager.getCriteriaBuilder().equal(root.get("id"), itemId));

        // Execute the update
        int updatedEntities = entityManager.createQuery(criteriaUpdate).executeUpdate();
    }

    @Transactional
    public void updateJsonPropertyForItemByHQL(Long itemId, String property, String value) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("child", new JSONObject());
        jsonObject.getJSONObject("child").put(property, value);
        String hqlUpdate = "UPDATE Item SET jsonbContent = %s(jsonbContent, %s(:json, 'jsonb' ) ) WHERE id = :id".formatted(hibernateContext.getConcatenateJsonbOperator(), hibernateContext.getCastFunctionOperator());
        int updatedEntities = entityManager.createQuery( hqlUpdate )
                .setParameter("id", itemId)
                .setParameter("json", jsonObject.toString())
                .executeUpdate();
    }

    @Transactional
    public void updateJsonBySettingPropertyForItemByHQL(Long itemId, String property, String value) {
        // Execute the update
        String hqlUpdate = "UPDATE Item SET jsonbContent = jsonb_set(jsonbContent, %s(:path, 'text[]'), %s(:json, 'jsonb' ) ) WHERE id = :id".formatted(hibernateContext.getCastFunctionOperator(), hibernateContext.getCastFunctionOperator());
        int updatedEntities = entityManager.createQuery( hqlUpdate )
                .setParameter("id", itemId)
                .setParameter("path", new JsonTextArrayBuilder().append("child").append(property).build().toString())
                .setParameter("json", JSONObject.quote(value))
                .executeUpdate();
    }
}
