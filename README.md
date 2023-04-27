# posjsonhelper

[![Run tests for posmulten hibernate](https://github.com/starnowski/posjsonhelper/actions/workflows/ci.yml/badge.svg)](https://github.com/starnowski/posjsonhelper/actions/workflows/ci.yml)

### Useful links
https://thorben-janssen.com/persist-postgresqls-jsonb-data-type-hibernate/
https://stackoverflow.com/questions/15974474/mapping-postgresql-json-column-to-a-hibernate-entity-property
https://prateek-ashtikar512.medium.com/how-to-handle-json-in-postgresql-5e2745d5324
https://fullstackdeveloper.guru/2020/05/29/how-to-map-json-data-in-postgresql-database-to-a-hibernate-entity-column/


https://www.postgresql.org/docs/10/functions-json.html
https://medium.com/geekculture/postgres-jsonb-usage-and-performance-analysis-cdbd1242a018
### Escape operators in JPA2 native query -- Create operators
https://stackoverflow.com/questions/50464741/how-to-escape-question-mark-character-with-spring-jparepository

### TODO Implement 
* get element - json_extract_path and jsonb_extract_path, json_extract_path_text, jsonb_extract_path_text
* add option to chose if you want to user operators or functions for escaping characters


* [Introduction](#introduction)
* [How to start using posjsonhelper](#how-to-start-using-posjsonhelper)
    * [Setting maven dependency](#setting-maven-dependency)
    * [Building project locally](#building-project-locally)
* [How to attach postgresql dialect](#how-to-attach-postgresql dialect)
* [Apply DDL changes](#apply-ddl-changes)
 * [Apply DDL changes programmatically](#apply-ddl-changes-programmatically)
* [How to use query helper](#how-to-use-query-helper)
 * [JsonBExtractPath - jsonb_extract_path](#jsonbextractpath---jsonb_extract_path)
 * [JsonBExtractPathText - jsonb_extract_path_text](#jsonbextractpathtext---jsonb_extract_path_text)
 * [#TODO]
 * [JsonbAllArrayStringsExistPredicate](#jsonballarraystringsexistpredicate)
 * [JsonbAnyArrayStringsExistPredicate](#jsonbanyarraystringsexistpredicate)
* [Properties](#properties)
* [Reporting issues](#reporting-issues)
* [Project contribution](#project-contribution)

# Introduction
Posjsonhelper library is an open-source project that adds support of Hibernate query for [postgresql json functions](https://www.postgresql.org/docs/10/functions-json.html).
The library is written in a java programming language.
The project for this moment supports only Hibernate with version 5.
The required version of java is at least version 8.

### Setting maven dependency
The project is available in the central maven repository.
You can use it just by adding it as a dependency in the project descriptor file (pom.xml).

```xml
        <dependency>
            <groupId>com.github.starnowski.posjsonhelper</groupId>
            <artifactId>hibernate5</artifactId>
            <version>0.1.0</version>
        </dependency>
```

### Building project locally
If someone would like to build the project locally from the source please see the CONTRIBUTING.md file to check how to set up the project locally.

### How to attach postgresql dialect

To be able to use the posjsonhelper library in the project there has to be specified correct hibernate dialect.
Library implements few wrappers that extends already existed hibernate dialects for postgresql:
- com.github.starnowski.posjsonhelper.hibernate5.dialects.PostgreSQL10DialectWrapper
- com.github.starnowski.posjsonhelper.hibernate5.dialects.PostgreSQL95DialectWrapper

Dialect has to be set in hibernate configuation file for example:

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<hibernate-configuration xmlns="http://www.hibernate.org/xsd/orm/cfg">
    <session-factory>
        <property name="hibernate.dialect">com.github.starnowski.posjsonhelper.hibernate5.dialects.PostgreSQL95DialectWrapper</property>
...
```

or for example in spring framework configuration properties file:

```properties
...
spring.jpa.properties.hibernate.dialect=com.github.starnowski.posjsonhelper.hibernate5.dialects.PostgreSQL95DialectWrapper
...
```

In case if you already have type that extends hibernate dialect type and it required for your project.
You add adjustment to your type so that it would use PostgreSQLDialectEnricher component.

```java
import com.github.starnowski.posjsonhelper.hibernate5.PostgreSQLDialectEnricher;
import org.hibernate.dialect.PostgreSQL95Dialect;

public class PostgreSQLDialectWithDifferentSchema extends PostgreSQL95Dialect {

    public PostgreSQLDialectWithDifferentSchema() {
        PostgreSQLDialectEnricher enricher = new PostgreSQLDialectEnricher();
        enricher.enrich(this);
    }
}

```

### Apply DDL changes

To use the posjsonhelper library it is required to create a few SQL functions that execute JSON operators.
Some JSON operators can not be executed by hibernate because they must be escaped.
For a default configuration, the library requires the below functions to be created.

```sql
CREATE OR REPLACE FUNCTION jsonb_all_array_strings_exist(jsonb, text[]) RETURNS boolean AS $$
SELECT $1 ?& $2;
$$ LANGUAGE SQL;

CREATE OR REPLACE FUNCTION jsonb_any_array_strings_exist(jsonb, text[]) RETURNS boolean AS $$
SELECT $1 ?| $2;
$$ LANGUAGE SQL;
```

Generated DDL statement can be executed during integration tests or used by tools that apply changes to the database, like [Liquibase](https://www.liquibase.org/) or [Flyway](https://flywaydb.org/).

#### Apply DDL changes programmatically

It is posible also to add DDL programmatically by using DatabaseOperationExecutorFacade type.
Below there is example on how to apply DDL changes in application with Spring framework context.

```java
import com.github.starnowski.posjsonhelper.core.Context;
import com.github.starnowski.posjsonhelper.core.DatabaseOperationExecutorFacade;
import com.github.starnowski.posjsonhelper.core.DatabaseOperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.sql.DataSource;

@Configuration
public class SQLFunctionsConfiguration implements
        ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private Context context;
    @Autowired
    private DataSource dataSource;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        DatabaseOperationExecutorFacade facade = new DatabaseOperationExecutorFacade();
        try {
            facade.execute(dataSource, context, DatabaseOperationType.LOG_ALL);
            facade.execute(dataSource, context, DatabaseOperationType.CREATE);
            facade.execute(dataSource, context, DatabaseOperationType.VALIDATE);
        } catch (Exception e) {
            throw new RuntimeException("Error during initialization of sql functions for jsonb type operations", e);
        }
    }
}
```

There are a few operations that can be executed by the DatabaseOperationExecutorFacade object

| Property name |   Description    |
|---------------|------------------|
|CREATE         | Applies DDL changes to database |
|VALIDATE       | Validates if DDL changes were applied to database |
|DROP           | Drops DDL changes in database |
|LOG_ALL        | Displays DDL scripts for CREATE, VALIDATE and DROP operations |

### How to use query helper

For easier explanation let's assume that we have a database table with one column that stores jsonb type.

```sql
create table item (
       id int8 not null,
        jsonb_content jsonb,
        primary key (id)
    )
```

For this table, we can insert row with any json, like in example below:

```sql 
INSERT INTO item (id, jsonb_content) VALUES (1, '{"top_element_with_set_of_values":["TAG1","TAG2","TAG11","TAG12","TAG21","TAG22"]}');
INSERT INTO item (id, jsonb_content) VALUES (2, '{"top_element_with_set_of_values":["TAG3"]}');

-- item without any properties, just an empty json
INSERT INTO item (id, jsonb_content) VALUES (6, '{}');

-- int values
INSERT INTO item (id, jsonb_content) VALUES (7, '{"integer_value": 132}');

-- double values
INSERT INTO item (id, jsonb_content) VALUES (10, '{"double_value": 353.01}');
INSERT INTO item (id, jsonb_content) VALUES (11, '{"double_value": -1137.98}');

-- enum values
INSERT INTO item (id, jsonb_content) VALUES (13, '{"enum_value": "SUPER"}');

-- string values
INSERT INTO item (id, jsonb_content) VALUES (18, '{"string_value": "the end of records"}');
```

#### JsonBExtractPath - jsonb_extract_path

The "jsonb_extract_path" is postgresql function that returns jsonb value pointed to by path elements passed as "text[]" (equivalent to #> operator).
It is useful because a lot of functions use the "jsonb" type for execution.
Please check [postgresql documentation](https://www.postgresql.org/docs/10/functions-json.html) for more information.
Below there is an example of a method that returns a list of items object for which json content property "top_element_with_set_of_values" contains an exact set of values.
The example use [JsonbAllArrayStringsExistPredicate](#jsonballarraystringsexistpredicate).

```java
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
```

For the above method, Hibernate will execute the HQL query:

```hql 
select
        generatedAlias0 
    from
        Item as generatedAlias0 
    where
        jsonb_all_array_strings_exist( jsonb_extract_path( generatedAlias0.jsonbContent , :param0 ) , json_function_json_array(:param1)) = TRUE
```

Native sql is going to have below form:

```sql 
select
            item0_.id as id1_0_,
            item0_.jsonb_content as jsonb_co2_0_ 
        from
            item item0_ 
        where
            jsonb_all_array_strings_exist(jsonb_extract_path(item0_.jsonb_content,?), array[?])=true
```

For more details please check the [DAO](/hibernate5/src/test/java/com/github/starnowski/posjsonhelper/hibernate5/demo/dao/ItemDao.java) used in tests.

#### JsonBExtractPathText - jsonb_extract_path_text

The "jsonb_extract_path_text" is postgresql function that returns JSON value as text pointed to by path elements passed as "text[]" (equivalent to #>> operator).
Please check [postgresql documentation](https://www.postgresql.org/docs/10/functions-json.html) for more information.
Below is an example of a method that looks for items containing specific string values matched by the "LIKE" operator.

```java
    public List<Item> findAllByStringValueAndLikeOperator(String expression) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> query = cb.createQuery(Item.class);
        Root<Item> root = query.from(Item.class);
        query.select(root);
        query.where(cb.like(new JsonBExtractPathText((CriteriaBuilderImpl) cb, singletonList("string_value"), root.get("jsonbContent")), expression));
        return entityManager.createQuery(query).getResultList();
    }
```

For the above method, Hibernate will execute the HQL query:

```hql
select
        generatedAlias0 
    from
        Item as generatedAlias0 
    where
        jsonb_extract_path_text( generatedAlias0.jsonbContent , :param0 ) like :param1
```
Native sql is going to have below form:

```sql
select
            item0_.id as id1_0_,
            item0_.jsonb_content as jsonb_co2_0_ 
        from
            item item0_ 
        where
            jsonb_extract_path_text(item0_.jsonb_content,?) like ?
```

For more details and examples with the IN operator or how to use numeric values please check the [DAO](/hibernate5/src/test/java/com/github/starnowski/posjsonhelper/hibernate5/demo/dao/ItemDao.java) used in tests.

#TODO

#### JsonbAllArrayStringsExistPredicate

The JsonbAllArrayStringsExistPredicate type represents predicate that checks if passed string arrays exist in json array property.
First example for this predicate was introduce in ["JsonBExtractPath - jsonb_extract_path"](#jsonbextractpath---jsonb_extract_path) section.
Below example ....


```java
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
```

#### JsonbAnyArrayStringsExistPredicate

### Properties