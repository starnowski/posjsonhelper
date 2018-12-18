package com.github.starnowski.posjsonhelper.poc.dao;

import com.github.starnowski.posjsonhelper.poc.JSONBComparisonPredicate;
import com.github.starnowski.posjsonhelper.poc.model.Book;
import org.hibernate.jpa.criteria.CriteriaBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Repository
public class BookRepository {

    @Autowired
    private EntityManager entityManager;

    List<Book> listBooksByAuthors(String... authors)
    {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> query = cb.createQuery(Book.class);
        Root<Book> root = query.from(Book.class);
        query.select(root);
        query.where(new JSONBComparisonPredicate((CriteriaBuilderImpl) cb, "authors", JSONBComparisonPredicate.ComparisonOperator.ALL_STRINGS_AT_TOP_LEVEL, root.get("jsonData"), authors));

        return entityManager.createQuery(query).getResultList();
    }

    List<Book> returnBookForAuthorsByNativeQuery(String... authours)
    {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = Arrays.asList(authours).iterator();
        while (it.hasNext())
        {
            String authour = it.next();
            sb.append("'");
            sb.append(authour);
            sb.append("'");
            if (it.hasNext())
            {
                sb.append(",");
            }

        }
        return entityManager.createNativeQuery("SELECT * FROM book WHERE jsonb_data->'authors' escape ?& array[" + sb.toString() + "]").getResultList(); //Caused by: org.hibernate.QueryException: Expected positional parameter count: 1, actual parameters: [] [SELECT * FROM book WHERE jsonb_data->'authors' ?& array['William Shakespeare','Stephen King']]
//        return entityManager.createNativeQuery("SELECT * FROM book WHERE jsonb_data->'authors' ? array[" + sb.toString() + "]").setParameter(1, "&").getResultList();
    }
}
