# posjsonhelper

[![Run tests for posjsonhelper](https://github.com/starnowski/posjsonhelper/actions/workflows/ci.yml/badge.svg)](https://github.com/starnowski/posjsonhelper/actions/workflows/ci.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.starnowski.posjsonhelper/parent.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.starnowski.posjsonhelper%22%20AND%20a:%22parent%22)

* [Introduction](#introduction)
* [How to start using posjsonhelper](#how-to-start-using-posjsonhelper)
    * [Setting maven dependency](#setting-maven-dependency)
    * [Building project locally](#building-project-locally)
* [How to attach postgresql_dialect](#how-to-attach-postgresql-dialect)
* [How to attach FunctionContributor](#how-to-attach-functioncontributor)
* [Apply DDL changes](#apply-ddl-changes)
    * [Apply DDL changes programmatically](#apply-ddl-changes-programmatically)
    * [Core context](#core-context)
* [How to use query helper](#how-to-use-query-helper)
    * [Hibernate Context](#hibernate-context)
    * [JsonBExtractPath - jsonb_extract_path](#jsonbextractpath---jsonb_extract_path)
    * [JsonBExtractPathText - jsonb_extract_path_text](#jsonbextractpathtext---jsonb_extract_path_text)
    * [JsonbAllArrayStringsExistPredicate](#jsonballarraystringsexistpredicate)
    * [JsonbAnyArrayStringsExistPredicate](#jsonbanyarraystringsexistpredicate)
* [Properties](#properties)
* [Reporting issues](#reporting-issues)
* [Project contribution](#project-contribution)

# Introduction
Posjsonhelper library is an open-source project that adds support of Hibernate query for [postgresql json functions](https://www.postgresql.org/docs/10/functions-json.html).
The library is written in a java programming language.
The project for this moment supports Hibernate with version 5 and 6.
The required version of java is at least version 8 for hibernate 5 support and version 11 for hibernate 6.

### Setting maven dependency
The project is available in the central maven repository.
You can use it just by adding it as a dependency in the project descriptor file (pom.xml).
 
**For Hibernate 5:**
```xml
        <dependency>
            <groupId>com.github.starnowski.posjsonhelper</groupId>
            <artifactId>hibernate5</artifactId>
            <version>0.2.0-SNAPSHOT</version>
        </dependency>
```

**For Hibernate 6:**
```xml
        <dependency>
            <groupId>com.github.starnowski.posjsonhelper</groupId>
            <artifactId>hibernate6</artifactId>
            <version>0.2.0-SNAPSHOT</version>
        </dependency>
```

### Building project locally
If someone would like to build the project locally from the source please see the CONTRIBUTING.md file to check how to set up the project locally.

### How to attach postgresql dialect

**Important! This section is only valid for Hibernate 5.**
To be able to use the posjsonhelper library in the project there has to be specified correct hibernate dialect.
Library implements few wrappers that extends already existed hibernate dialects for postgresql:
- com.github.starnowski.posjsonhelper.hibernate5.dialects.PostgreSQL10DialectWrapper
- com.github.starnowski.posjsonhelper.hibernate5.dialects.PostgreSQL95DialectWrapper

Dialect has to be set in hibernate configuration file for example:

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

### How to attach FunctionContributor

**Important! This section is only valid for Hibernate 6.**
To use the posjsonhelper library in the project that uses Hibernate 6, there must be a specified org.hibernate.boot.model.FunctionContributor implementation.
Library has implementation of this interface, that is com.github.starnowski.posjsonhelper.hibernate6.PosjsonhelperFunctionContributor.

To use this implementation it is required to create file with name "org.hibernate.boot.model.FunctionContributor" under "resources/META-INF/services" directory.

The alternative solution is to use com.github.starnowski.posjsonhelper.hibernate6.SqmFunctionRegistryEnricher component during application start-up.
Like in the below example with the usage of the Spring framework.

```java
import com.github.starnowski.posjsonhelper.hibernate6.SqmFunctionRegistryEnricher;
import jakarta.persistence.EntityManager;
import org.hibernate.query.sqm.NodeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

@Configuration
public class FunctionDescriptorConfiguration implements
        ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private EntityManager entityManager;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        NodeBuilder nodeBuilder = (NodeBuilder) entityManager.getCriteriaBuilder();
        SqmFunctionRegistryEnricher sqmFunctionRegistryEnricher = new SqmFunctionRegistryEnricher();
        sqmFunctionRegistryEnricher.enrich(nodeBuilder.getQueryEngine().getSqmFunctionRegistry());
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
**Important!**
If there would be requirement to use similar function but with different names then this has to be specified in application [properties](#properties).
It is required because types extends hibernate dialect type, mentioned in the ["how to attach postgresql dialect"](#how-to-attach-postgresql dialect) section may not have access to application context (IoC).
However, in case if such properties should be passed in different way then the PostgreSQLDialectEnricher type has also method to pass context objects (please check [Core context](#core-context) and [Hibernate Context](#hibernate-context))

#### Core context

Context class holds names of functions used by library.
The dialect classes use CoreContextPropertiesSupplier component that generates Context object based on system property.

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

#### Hibernate Context

Most predicate components use Hibernate Context object.
It holds mostly the names of hibernate function names used in project.
The dialect classes use HibernateContextPropertiesSupplier component that generates HibernateContext object based on system property.

#### JsonBExtractPath - jsonb_extract_path

The "jsonb_extract_path" is postgresql function that returns jsonb value pointed to by path elements passed as "text[]" (equivalent to #> operator).
It is useful because a lot of functions use the "jsonb" type for execution.
Please check [postgresql documentation](https://www.postgresql.org/docs/10/functions-json.html) for more information.
**Hibernate 5 example**:
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

**Hibernate 6 example**:

```java

import org.hibernate.query.sqm.NodeBuilder;
....
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
```

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

#### JsonbAllArrayStringsExistPredicate

The JsonbAllArrayStringsExistPredicate type represents predicate that checks if passed string arrays exist in json array property.
First example for this predicate was introduce in ["JsonBExtractPath - jsonb_extract_path"](#jsonbextractpath---jsonb_extract_path) section.
These predicates assume that the SQL function with default name jsonb_all_array_strings_exist, mentioned in the section ["Apply DDL changes"](#apply-ddl-changes) exists.
The below example with a combination with the operator NOT presents items that do not have all searched strings.


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

For the above method, Hibernate will execute the HQL query:

```hql
select
        generatedAlias0 
    from
        Item as generatedAlias0 
    where
        (
            jsonb_extract_path( generatedAlias0.jsonbContent , :param0 ) is null 
        ) 
        or (
            jsonb_all_array_strings_exist( jsonb_extract_path( generatedAlias0.jsonbContent , :param1 ) , json_function_json_array(:param2, :param3)) = FALSE 
        )
```
Native sql is going to have below form:

```sql
select
            item0_.id as id1_0_,
            item0_.jsonb_content as jsonb_co2_0_ 
        from
            item item0_ 
        where
            jsonb_extract_path(item0_.jsonb_content,?) is null 
            or jsonb_all_array_strings_exist(jsonb_extract_path(item0_.jsonb_content,?), array[?,?])=false
```

#### JsonbAnyArrayStringsExistPredicate

The JsonbAnyArrayStringsExistPredicate type represents a predicate that checks if passed string arrays exist in json array property.
These predicates assume that the SQL function with default name jsonb_any_array_strings_exist, mentioned in the section ["Apply DDL changes"](#apply-ddl-changes) exists.
Below there is an example of a method that looks for all items that property that holds array contains at least one string passed from the array passed as method argument.

```java
    public List<Item> findAllByAnyMatchingTags(HashSet<String> tags) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> query = cb.createQuery(Item.class);
        Root<Item> root = query.from(Item.class);
        query.select(root);
        query.where(new JsonbAnyArrayStringsExistPredicate(hibernateContext, (CriteriaBuilderImpl) cb, new JsonBExtractPath((CriteriaBuilderImpl) cb, singletonList("top_element_with_set_of_values"), root.get("jsonbContent")), tags.toArray(new String[0])));
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
        jsonb_any_array_strings_exist( jsonb_extract_path( generatedAlias0.jsonbContent , :param0 ) , json_function_json_array(:param1, :param2)) = TRUE
```
Native sql is going to have below form:

```sql
select
            item0_.id as id1_0_,
            item0_.jsonb_content as jsonb_co2_0_ 
        from
            item item0_ 
        where
            jsonb_any_array_strings_exist(jsonb_extract_path(item0_.jsonb_content,?), array[?,?])=true
```

### Properties

| Property name |   Description |
|---------------|---------------|
|com.github.starnowski.posjsonhelper.core.functions.jsonb_all_array_strings_exist   |   Name of SQL function that checks if all passed elements as the text[] exist in the JSON array property. By default, the name is the jsonb_all_array_strings_exist |
|com.github.starnowski.posjsonhelper.core.functions.jsonb_any_array_strings_exist   |   Name of SQL function that checks if any passed elements as the text[] exist in the JSON array property. By default, the name is the jsonb_any_array_strings_exist |
|com.github.starnowski.posjsonhelper.core.schema   |   Name of database schema where the SQL functions should be created |
|com.github.starnowski.posjsonhelper.core.hibernate.functions.jsonb_all_array_strings_exist   |   Name of HQL function that invokes SQL function specified by the com.github.starnowski.posjsonhelper.core.functions.jsonb_all_array_strings_exist property. By default, the name is the jsonb_all_array_strings_exist |
|com.github.starnowski.posjsonhelper.core.hibernate.functions.jsonb_any_array_strings_exist   |   Name of HQL function that invokes SQL function specified by the com.github.starnowski.posjsonhelper.core.functions.jsonb_any_array_strings_exist property. By default, the name is the jsonb_any_array_strings_exist |
|com.github.starnowski.posjsonhelper.core.hibernate.functions.json_function_json_array   |   	Name of HQL function that wraps the array operator in Postgresql. By default, the name is the json_function_json_array |

# Reporting issues
* Any new issues please report in [GitHub site](https://github.com/starnowski/posjsonhelper/issues)

# Project contribution
* Look for open issues or create your own
* Fork repository on Github and start applying your changes to master branch or release branch
* Follow CONTRIBUTING.md document for coding rules
* Create pull request