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
* [Modify JSON](#modify-json)
  * [jsonb_set function wrapper](#jsonb_set-function-wrapper)
  * [Concatenation operator wrapper '||'](#concatenation-operator-wrapper-)
  * [Deletes the field or array element at the specified path '#-'](#deletes-the-field-or-array-element-at-the-specified-path--)
  * [Hibernate6JsonUpdateStatementBuilder - How to combine multiple modification operations with one update statement?](#hibernate6jsonupdatestatementbuilder---how-to-combine-multiple-modification-operations-with-one-update-statement)
* [Properties](#properties)
* [Reporting issues](#reporting-issues)
* [Project contribution](#project-contribution)

# Introduction
Posjsonhelper library is an open-source project that adds support of Hibernate query for [postgresql json functions](https://www.postgresql.org/docs/10/functions-json.html).
Library also has support for [postgresql text search functions](https://www.postgresql.org/docs/current/textsearch-intro.html).
To know more on how to use text search components check instructions for the [text module](/text).
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
            <version>0.3.3</version>
        </dependency>
```

**For Hibernate 6:**
```xml
        <dependency>
            <groupId>com.github.starnowski.posjsonhelper</groupId>
            <artifactId>hibernate6</artifactId>
            <version>0.3.3</version>
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
The dialect classes and FunctionContributor type use HibernateContextPropertiesSupplier component that generates HibernateContext object based on system property.
If there is no need to change default HQL function names for psojsonhelper operators then it is even to use HibernateContext created by builder component like below:

```java
    HibernateContext hibernateContext = HibernateContext.builder().build();
```

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

Below there is the same example as above but for Hibernate 6.

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

For more details please check the [DAO](/hibernate6-tests/hibernate6-tests-core/src/main/java/com/github/starnowski/posjsonhelper/hibernate6/demo/dao/ItemDao.java) used in tests.

#### JsonBExtractPathText - jsonb_extract_path_text

The "jsonb_extract_path_text" is postgresql function that returns JSON value as text pointed to by path elements passed as "text[]" (equivalent to #>> operator).
Please check [postgresql documentation](https://www.postgresql.org/docs/10/functions-json.html) for more information.
Below there is an example for Hibernate 5 of a method that looks for items containing specific string values matched by the "LIKE" operator.

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

**Hibernate 6 example**:

Below there is the same example as above but for Hibernate 6.

```java
    ....
    public List<Item> findAllByStringValueAndLikeOperator(String expression) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> query = cb.createQuery(Item.class);
        Root<Item> root = query.from(Item.class);
        query.select(root);
        query.where(cb.like(new JsonBExtractPathText(root.get("jsonbContent"), singletonList("string_value"), (NodeBuilder) cb), expression));
        return entityManager.createQuery(query).getResultList();
        }

```

For more details please check the [DAO](/hibernate6-tests/hibernate6-tests-core/src/main/java/com/github/starnowski/posjsonhelper/hibernate6/demo/dao/ItemDao.java) used in tests.

#### JsonbAllArrayStringsExistPredicate

The JsonbAllArrayStringsExistPredicate type represents predicate that checks if passed string arrays exist in json array property.
First example for this predicate was introduce in ["JsonBExtractPath - jsonb_extract_path"](#jsonbextractpath---jsonb_extract_path) section.
These predicates assume that the SQL function with default name jsonb_all_array_strings_exist, mentioned in the section ["Apply DDL changes"](#apply-ddl-changes) exists.
The below example with a combination with the operator NOT presents items that do not have all searched strings.
**Example valid for Hibernate 5 only!**

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

**Hibernate 6 example**:

Below there is the same example as above but for Hibernate 6.

```java
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
```

For more details please check the [DAO](/hibernate6-tests/hibernate6-tests-core/src/main/java/com/github/starnowski/posjsonhelper/hibernate6/demo/dao/ItemDao.java) used in tests.

#### JsonbAnyArrayStringsExistPredicate

The JsonbAnyArrayStringsExistPredicate type represents a predicate that checks if passed string arrays exist in json array property.
These predicates assume that the SQL function with default name jsonb_any_array_strings_exist, mentioned in the section ["Apply DDL changes"](#apply-ddl-changes) exists.
Below there is an example of a method that looks for all items that property that holds array contains at least one string passed from the array passed as method argument.
**Example valid for Hibernate 5 only!**

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
**Hibernate 6 example**:

Below there is the same example as above but for Hibernate 6.

```java

public List<Item> findAllByAnyMatchingTags(HashSet<String> tags) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> query = cb.createQuery(Item.class);
        Root<Item> root = query.from(Item.class);
        query.select(root);
        query.where(new JsonbAnyArrayStringsExistPredicate(hibernateContext, (NodeBuilder) cb, new JsonBExtractPath(root.get("jsonbContent"), (NodeBuilder) cb, singletonList("top_element_with_set_of_values")), tags.toArray(new String[0])));
        return entityManager.createQuery(query).getResultList();
        }

```

For more details and examples with the IN operator or how to use numeric values please check the [DAO](/hibernate5/src/test/java/com/github/starnowski/posjsonhelper/hibernate5/demo/dao/ItemDao.java) used in tests.

## Modify JSON

The library can also be used for JSON modification operations. By default, in Hibernate, we can always update a column with JSON content by setting its entire value.
The posjsonhelper library also allows you to modify JSON content by setting, replacing, or removing individual JSON properties without replacing its full content.
The library contains several JSON functions and operators that allow for this type of operation.

### jsonb_set function wrapper

Wrapper for [jsonb_set](https://www.postgresql.org/docs/9.5/functions-json.html) function.
The function sets or replaces the value of the JSON property based on the JSON path. 
Check out the following example of how it can be used with the CriteriaUpdate component:

```java
// GIVEN
        Long itemId = 19L;
        String property = "birthday";
        String value = "1970-01-01";
        String expectedJson = "{\"child\": {\"pets\" : [\"dog\"], \"birthday\": \"1970-01-01\"}}";
        // when
        CriteriaUpdate<Item> criteriaUpdate = entityManager.getCriteriaBuilder().createCriteriaUpdate(Item.class);
        Root<Item> root = criteriaUpdate.from(Item.class);

        // Set the property you want to update and the new value
        criteriaUpdate.set("jsonbContent", new JsonbSetFunction((NodeBuilder) entityManager.getCriteriaBuilder(), root.get("jsonbContent"), new JsonTextArrayBuilder().append("child").append(property).build().toString(), JSONObject.quote(value), hibernateContext));

        // Add any conditions to restrict which entities will be updated
        criteriaUpdate.where(entityManager.getCriteriaBuilder().equal(root.get("id"), itemId));

        // Execute the update
        entityManager.createQuery(criteriaUpdate).executeUpdate();

        // then
        Item item = tested.findById(itemId);
        assertThat((String) JsonPath.read(item.getJsonbContent(), "$.child." + property)).isEqualTo(value);
        JSONObject jsonObject = new JSONObject(expectedJson);
        DocumentContext document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$"));
        assertThat(document.jsonString()).isEqualTo(jsonObject.toString());
```

This would generate the following SQL update statement:

```sql
update
        item 
    set
        jsonb_content=jsonb_set(jsonb_content, ?::text[], ?::jsonb) 
    where
        id=?
Hibernate: 
    select
        i1_0.id,
        i1_0.jsonb_content 
    from
        item i1_0 
    where
        i1_0.id=?
```

The function can also be used in HQL statements, as in the following example:

```java
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
```

### Concatenation operator wrapper '||'

Wrapper for [concatenation operator](https://www.postgresql.org/docs/9.5/functions-json.html).
The wrapper concatenate two jsonb values into a new jsonb value.
Check out the following example of how it can be used with the CriteriaUpdate component:

```java
        // GIVEN
        Long itemId = 19l;
        String property = "birthday";
        String value = "1970-01-01";

        // WHEN
        CriteriaUpdate<Item> criteriaUpdate = entityManager.getCriteriaBuilder().createCriteriaUpdate(Item.class);
        Root<Item> root = criteriaUpdate.from(Item.class);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("child", new JSONObject());
        jsonObject.getJSONObject("child").put(property, value);
        criteriaUpdate.set("jsonbContent", new ConcatenateJsonbOperator((NodeBuilder) entityManager.getCriteriaBuilder(), root.get("jsonbContent"), jsonObject.toString(), hibernateContext));

        criteriaUpdate.where(entityManager.getCriteriaBuilder().equal(root.get("id"), itemId));

        entityManager.createQuery(criteriaUpdate).executeUpdate();

        // THEN
        Item item = tested.findById(itemId);
        assertThat((String) JsonPath.read(item.getJsonbContent(), "$.child." + property)).isEqualTo(value);
        JSONObject expectedJsonObject = new JSONObject().put(property, value);
        DocumentContext document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$.child"));
        assertThat(document.jsonString()).isEqualTo(expectedJsonObject.toString());
```

This would generate the following SQL update statement:

```sql
update
        item 
    set
        jsonb_content=jsonb_content || ?::jsonb 
    where
        id=?
Hibernate: 
    select
        i1_0.id,
        i1_0.jsonb_content 
    from
        item i1_0 
    where
        i1_0.id=?
```

The function can also be used in HQL statements, as in the following example:

```java
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
```

### Deletes the field or array element at the specified path '#-'

Wrapper for [deletes operator '#-'](https://www.postgresql.org/docs/9.5/functions-json.html).
The wrapper Deletes the field or array element at the specified path, where path elements can be either field keys or array indexes.
Check out the following example of how it can be used with the CriteriaUpdate component:

```java
        // GIVEN
        Item item = tested.findById(19L);
        JSONObject jsonObject = new JSONObject("{\"child\": {\"pets\" : [\"dog\"]}}");
        DocumentContext document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$"));
        assertThat(document.jsonString()).isEqualTo(jsonObject.toString());

        // WHEN
        CriteriaUpdate<Item> criteriaUpdate = entityManager.getCriteriaBuilder().createCriteriaUpdate(Item.class);
        Root<Item> root = criteriaUpdate.from(Item.class);

        // Set the property you want to update and the new value
        criteriaUpdate.set("jsonbContent", new DeleteJsonbBySpecifiedPathOperator((NodeBuilder) entityManager.getCriteriaBuilder(), root.get("jsonbContent"), new JsonTextArrayBuilder().append("child").append("pets").build().toString(), hibernateContext));

        // Add any conditions to restrict which entities will be updated
        criteriaUpdate.where(entityManager.getCriteriaBuilder().equal(root.get("id"), 19L));

        // Execute the update
        entityManager.createQuery(criteriaUpdate).executeUpdate();

        // THEN
        entityManager.refresh(item);
        jsonObject = new JSONObject("{\"child\": {}}");
        document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$"));
        assertThat(document.jsonString()).isEqualTo(jsonObject.toString());
```

This would generate the following SQL update statement:

```sql
update
        item 
    set
        jsonb_content=(jsonb_content #- ?::text[]) 
    where
        id=?
```

The function can also be used in HQL statements, as in the following example:

```java
    @Transactional
    public void updateJsonByDeletingSpecificPropertyForItemByHql(Long itemId, String property) {
        // Execute the update
        String hqlUpdate = "UPDATE Item SET jsonbContent = %s(jsonbContent, %s(:path, 'text[]') ) WHERE id = :id".formatted(hibernateContext.getDeleteJsonBySpecificPathOperator(), hibernateContext.getCastFunctionOperator());
        int updatedEntities = entityManager.createQuery(hqlUpdate)
                .setParameter("id", itemId)
                .setParameter("path", new JsonTextArrayBuilder().append("child").append(property).build().toString())
                .executeUpdate();
    }
```

### Hibernate6JsonUpdateStatementBuilder - How to combine multiple modification operations with one update statement?

Using a single jsonb_set function to set a single property for JSON with a single update statement can be useful,
however, it may be more useful to be able to set multiple properties at different levels of the JSON tree with a single update statement.

Lest check below code example:

```java
        // GIVEN
        Item item = tested.findById(23L);
                DocumentContext document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$"));
                assertThat(document.jsonString()).isEqualTo("{\"child\":{\"pets\":[\"dog\"]},\"inventory\":[\"mask\",\"fins\"],\"nicknames\":{\"school\":\"bambo\",\"childhood\":\"bob\"}}");
                CriteriaUpdate<Item> criteriaUpdate = entityManager.getCriteriaBuilder().createCriteriaUpdate(Item.class);
        Root<Item> root = criteriaUpdate.from(Item.class);

        Hibernate6JsonUpdateStatementBuilder hibernate6JsonUpdateStatementBuilder = new Hibernate6JsonUpdateStatementBuilder(root.get("jsonbContent"), (NodeBuilder) entityManager.getCriteriaBuilder(), hibernateContext);
        hibernate6JsonUpdateStatementBuilder.appendJsonbSet(new JsonTextArrayBuilder().append("child").append("birthday").build(), quote("2021-11-23"));
        hibernate6JsonUpdateStatementBuilder.appendJsonbSet(new JsonTextArrayBuilder().append("child").append("pets").build(), "[\"cat\"]");
        hibernate6JsonUpdateStatementBuilder.appendDeleteBySpecificPath(new JsonTextArrayBuilder().append("inventory").append("0").build());
        hibernate6JsonUpdateStatementBuilder.appendJsonbSet(new JsonTextArrayBuilder().append("parents").append(0).build(), "{\"type\":\"mom\", \"name\":\"simone\"}");
        hibernate6JsonUpdateStatementBuilder.appendJsonbSet(new JsonTextArrayBuilder().append("parents").build(), "[]");
        hibernate6JsonUpdateStatementBuilder.appendDeleteBySpecificPath(new JsonTextArrayBuilder().append("nicknames").append("childhood").build());

        // Set the property you want to update and the new value
        criteriaUpdate.set("jsonbContent", hibernate6JsonUpdateStatementBuilder.build());

        // Add any conditions to restrict which entities will be updated
        criteriaUpdate.where(entityManager.getCriteriaBuilder().equal(root.get("id"), 23L));

        // WHEN
        entityManager.createQuery(criteriaUpdate).executeUpdate();

        // THEN
        entityManager.refresh(item);
        document = JsonPath.parse((Object) JsonPath.read(item.getJsonbContent(), "$"));
        assertThat(document.jsonString()).isEqualTo("{\"child\":{\"pets\":[\"cat\"],\"birthday\":\"2021-11-23\"},\"parents\":[{\"name\":\"simone\",\"type\":\"mom\"}],\"inventory\":[\"fins\"],\"nicknames\":{\"school\":\"bambo\"}}");
```

In the above code, we want to set three JSON properties "child.birthday", "child.pets" and "parents" and delete two others, "inventory.0" and "nicknames.childhood".
The "parents" property is new property that suppose to be an array.
Although the setting new array property with some values could be done with single operation, however for demonstration purpose we use two operations.
One is for setting new property called "parents" with empty json array as value.
And another operation that set element of an array at specific index.
**If higher property does not exist then it has to be created before inner properties.**
Fortunately, the default instance of the Hibernate6JsonUpdateStatementBuilder type has appropriate sorting and filtering components to help you set the right order of operations.
So it doesn't matter whether we add the add-array-element operation before or after adding the create-array operation.
By default, operations that delete content will be added before those which add or replace content.
Of course, it is possible to disable this behavior by setting these components to null.
For more details please check javadoc for Hibernate6JsonUpdateStatementBuilder type.

This code generates below SQL statement:

```sql
update
        item 
    set
        jsonb_content=
        jsonb_set(
          jsonb_set(
            jsonb_set(
              jsonb_set(
                (
                  (jsonb_content #- ?::text[]) -- the most nested #- operator
                 #- ?::text[])
              , ?::text[], ?::jsonb) -- the most nested jsonb_set operation
            , ?::text[], ?::jsonb)
          , ?::text[], ?::jsonb)
        , ?::text[], ?::jsonb) 
    where
        id=?
```

The most inner jsonb_set function execution for this prepared statement is going to set an empty array for the "parents" property.

### Properties

| Property name | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
|---------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|com.github.starnowski.posjsonhelper.core.functions.jsonb_all_array_strings_exist   | Name of SQL function that checks if all passed elements as the text[] exist in the JSON array property. By default, the name is the jsonb_all_array_strings_exist                                                                                                                                                                                                                                                                                                     |
|com.github.starnowski.posjsonhelper.core.functions.jsonb_any_array_strings_exist   | Name of SQL function that checks if any passed elements as the text[] exist in the JSON array property. By default, the name is the jsonb_any_array_strings_exist                                                                                                                                                                                                                                                                                                     |
|com.github.starnowski.posjsonhelper.core.schema   | Name of database schema where the SQL functions should be created                                                                                                                                                                                                                                                                                                                                                                                                     |
|com.github.starnowski.posjsonhelper.core.hibernate.functions.jsonb_all_array_strings_exist   | Name of HQL function that invokes SQL function specified by the com.github.starnowski.posjsonhelper.core.functions.jsonb_all_array_strings_exist property. By default, the name is the jsonb_all_array_strings_exist                                                                                                                                                                                                                                                  |
|com.github.starnowski.posjsonhelper.core.hibernate.functions.jsonb_any_array_strings_exist   | Name of HQL function that invokes SQL function specified by the com.github.starnowski.posjsonhelper.core.functions.jsonb_any_array_strings_exist property. By default, the name is the jsonb_any_array_strings_exist                                                                                                                                                                                                                                                  |
|com.github.starnowski.posjsonhelper.core.hibernate.functions.json_function_json_array   | 	Name of HQL function that wraps the array operator in Postgresql. By default, the name is the json_function_json_array                                                                                                                                                                                                                                                                                                                                               |
|com.github.starnowski.posjsonhelper.core.hibernate.functions.sqldefinitioncontextfactory.types   | 	System property that stores list of com.github.starnowski.posjsonhelper.core.sql.ISQLDefinitionContextFactory types that should be loaded. Instead of loading types that can be found on the classpath for package "com.github.starnowski.posjsonhelper". Types on the list are separated by comma character ".".                                                                                                                                                    |
|com.github.starnowski.posjsonhelper.hibernate6.functiondescriptorregisterfactory.types   | 	(Used only in Hibernate 6) System property that stores list of com.github.starnowski.posjsonhelper.hibernate6.descriptor.FunctionDescriptorRegisterFactoriesSupplier types that should be loaded. Instead of loading types that can be found on the classpath for package "com.github.starnowski.posjsonhelper". Types on the list are separated by comma character ".".                                                                                             |
|com.github.starnowski.posjsonhelper.hibernate6.functiondescriptorregisterfactory.types.excluded   | 	(Used only in Hibernate 6) System property that stores list of com.github.starnowski.posjsonhelper.hibernate6.descriptor.FunctionDescriptorRegisterFactoriesSupplier types that should be excluded from loading. If "com.github.starnowski.posjsonhelper.hibernate6.functiondescriptorregisterfactory.types" property is also specified then "com.github.starnowski.posjsonhelper.hibernate6.functiondescriptorregisterfactory.types.excluded" has higher priority. Types on the list are separated by comma character ".". |

### Hibernate 6 version compatibility

Compatibility matrix with Hibernate 6.  

| Posjsonhelper | Hibernate 6 |
|---------------|-------------|
| 0.3.0         | 6.4.0.Final |
| 0.2.0 - 0.2.1 | 6.1.5.Final |

# Reporting issues
* Any new issues please report in [GitHub site](https://github.com/starnowski/posjsonhelper/issues)

# Project contribution
* Look for open issues or create your own
* Fork repository on Github and start applying your changes to master branch or release branch
* Follow CONTRIBUTING.md document for coding rules
* Create pull request
