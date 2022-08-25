package com.github.starnowski.posjsonhelper.poc.dao;

import com.github.starnowski.posjsonhelper.poc.JSONBComparisonPredicate;
import com.github.starnowski.posjsonhelper.poc.model.BookReader;
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
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
public class BookReaderRepository {

    @Autowired
    private EntityManager entityManager;

    List<BookReader> listBookReadersByFavouriteAuthors(String... authors)
    {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookReader> query = cb.createQuery(BookReader.class);
        Root<BookReader> root = query.from(BookReader.class);
        query.select(root);
        query.where(new JSONBComparisonPredicate((CriteriaBuilderImpl) cb, "favourite_authors", JSONBComparisonPredicate.ComparisonOperator.ALL_STRINGS_AT_TOP_LEVEL, root.get("jsonData"), authors));

        return entityManager.createQuery(query).getResultList();
    }

    List<BookReader> listBookReadersByPlacesOfBirth(String... places)
    {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookReader> query = cb.createQuery(BookReader.class);
        Root<BookReader> root = query.from(BookReader.class);
        query.select(root);
        query.where(root.get("placeOfBirth").in(places));
        return entityManager.createQuery(query).getResultList();
    }

    List<BookReader> returnBookForAuthorsByNativeQuery(String... authours)
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
