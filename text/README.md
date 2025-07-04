# posjsonhelper text

* [Introduction](#introduction)
* [How to start using posjsonhelper](#how-to-start-using-posjsonhelper)
    * [Setting maven dependency](#setting-maven-dependency)
    * [Building project locally](#building-project-locally)
* [How to attach FunctionContributor](#how-to-attach-functioncontributor)
* [How to use query helper](#how-to-use-query-helper)
  * [Search context configuration parameter](#search-context-configuration-parameter)
  * [Hibernate Context](#hibernate-context)
  * [Text operator wrapper '@@'](#text-operator-wrapper--)
  * [Vector function 'to_tsvector'](#vector-function--totsvector)
  * [Function 'plainto_tsquery'](#function-plainto_tsquery)
    * [Cast operator and text search configuration](#cast-operator-and-text-search-configuration)
  * [Function 'phraseto_tsquery'](#function-phraseto_tsquery)
  * [Function 'websearch_to_tsquery'](#function-websearch_to_tsquery)
  * [Function 'to_tsquery'](#function-to_tsquery)
* [Properties](#properties)
* [Reporting issues](#reporting-issues)
* [Project contribution](#project-contribution)

# Introduction
Posjsonhelper text module adds support of Hibernate query for [postgresql text search functions](https://www.postgresql.org/docs/current/textsearch-intro.html).
The library is written in a java programming language.
The project for this moment supports Hibernate in version 6.
The required version of java is at least version 11.

### Setting maven dependency
The project is available in the central maven repository.
You can use it just by adding it as a dependency in the project descriptor file (pom.xml).

```xml
        <dependency>
            <groupId>com.github.starnowski.posjsonhelper.text</groupId>
            <artifactId>hibernate6-text</artifactId>
            <version>0.4.3</version>
        </dependency>
```

### Building project locally
If someone would like to build the project locally from the source please see the [CONTRIBUTING.md](../CONTRIBUTING.md) file to check how to set up the project locally.

### How to attach FunctionContributor
Please see the section in the main [README.md file](../README.md#how-to-attach-functioncontributor) to know how to attach FunctionContributor.
For the text module it is not required to [apply DDL changes](../README.md#apply-ddl-changes).
The text search components do not require custom SQL functions.

### How to use query helper

For easier explanation let's assume that we have a database table with one table with column that holds short description.

```sql
create table tweet (
        id bigint not null,
        short_content varchar(255),
        title varchar(255),
        primary key (id)
    )
```

And database we have such data for example:

```sql
INSERT INTO tweet (id, title, short_content) VALUES (1, 'Cats', 'Cats rules the world');
INSERT INTO tweet (id, title, short_content) VALUES (2, 'Rats', 'Rats rules in the sewers');
INSERT INTO tweet (id, title, short_content) VALUES (3, 'Rats vs Cats', 'Rats and Cats hates each other');

INSERT INTO tweet (id, title, short_content) VALUES (4, 'Feature', 'This project is design to wrap already existed functions of Postgres');
INSERT INTO tweet (id, title, short_content) VALUES (5, 'Postgres database', 'Postgres is one of the widly used database on the market');
INSERT INTO tweet (id, title, short_content) VALUES (6, 'Database', 'On the market there is a lot of database that have similar features like Oracle');
```

#### Hibernate Context

Most predicate components use Hibernate Context object.
It holds mostly the names of hibernate function names used in project.
To know how to create it please see [the right section in main document](../README.md#hibernate-context)

#### Search context configuration parameter

Most of the query components in text module have constructor with the name of search text configuration.
Based on [postgres documentation](https://www.postgresql.org/docs/9.4/textsearch-configuration.html)

`
A text search configuration specifies all options necessary to transform a document into a tsvector: the parser to use to break text into tokens,
and the dictionaries to use to transform each token into a lexeme.
Every call of to_tsvector or to_tsquery needs a text search configuration to perform its processing.
`

There are also constructors without search text configuration name that will create document in SQL query based on default configuration specified in postgres.

Below components have such constructor with configuration parameter:
* [Vector function 'to_tsvector'](#vector-function--totsvector)
* [Function 'plainto_tsquery'](#function-plainto_tsquery)
* [Function 'phraseto_tsquery'](#function-phraseto_tsquery)
* [Function 'websearch_to_tsquery'](#function-websearch_to_tsquery)

#### Text operator wrapper  '@@'

TextOperatorFunction is component that wraps Postgres [text operator](https://www.postgresql.org/docs/9.4/textsearch-intro.html).
Beside of the to_tsvector function, this operator is necessary to do text search.
The usage is going to be presented in below examples.

#### Vector function 'to_tsvector'

TSVectorFunction wraps the [to_tsvector](https://www.postgresql.org/docs/9.4/textsearch-intro.html) function.
The usage is going to be presented in all below examples.

#### Function plainto_tsquery

PlainToTSQueryFunction wraps the [plainto_tsquery](https://www.postgresql.org/docs/9.4/textsearch-intro.html) function.
Let's check below code example:
```java
    public List<Tweet> findBySinglePlainQueryInDescriptionForConfiguration(String textQuery, String configuration) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tweet> query = cb.createQuery(Tweet.class);
        Root<Tweet> root = query.from(Tweet.class);
        query.select(root);
        query.where(new TextOperatorFunction((NodeBuilder) cb, new TSVectorFunction(root.get("shortContent"), configuration, (NodeBuilder) cb), new PlainToTSQueryFunction((NodeBuilder) cb, configuration, textQuery), hibernateContext));
        return entityManager.createQuery(query).getResultList();
    }
```

For above code and configuration 'english' hibernate is going to generate below sql:

```sql
select
        t1_0.id,
        t1_0.short_content,
        t1_0.title 
    from
        tweet t1_0 
    where
        to_tsvector('english', t1_0.short_content) @@ plainto_tsquery('english', ?);
```

Similar code but implemented with usage of HQL language:

```java
    public List<Tweet> findBySinglePlainQueryInDescriptionForConfigurationWithHQL(String phrase, String configuration) {
        //plainto_tsquery
        String statement = String.format("from Tweet as tweet where text_operator_function(to_tsvector('%1$s', tweet.shortContent), plainto_tsquery('%1$s', :phrase))", configuration);
        TypedQuery<Tweet> query = entityManager.createQuery(statement, Tweet.class);
        query.setParameter("phrase", phrase);
        return query.getResultList();
    }
```

#### Cast operator and text search configuration

One more example that pass also text search configuration name but in different way.
On below example the configuration is being passed as object of type RegconfigTypeCastOperatorFunction.

```java
    public List<Tweet> findBySinglePlainQueryInDescriptionForConfigurationAndRegconfigTypeCastOperatorFunctionObjectInstance(String phrase, String configuration) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tweet> query = cb.createQuery(Tweet.class);
        Root<Tweet> root = query.from(Tweet.class);
        query.select(root);
        query.where(new TextOperatorFunction((NodeBuilder) cb, new TSVectorFunction(root.get("shortContent"), new RegconfigTypeCastOperatorFunction((NodeBuilder) cb, configuration, hibernateContext), (NodeBuilder) cb), new PlainToTSQueryFunction((NodeBuilder) cb, new RegconfigTypeCastOperatorFunction((NodeBuilder) cb, configuration, hibernateContext), phrase), hibernateContext));
        return entityManager.createQuery(query).getResultList();
    }
```

For such code hibernate is going to generate below sql:

```sql
select
        t1_0.id,
        t1_0.short_content,
        t1_0.title 
    from
        tweet t1_0 
    where
        to_tsvector(?::regconfig, t1_0.short_content) @@ plainto_tsquery(?::regconfig, ?)
```

As that could be observer on example the configuration is passed as prepared statement parameter.
Same code could be implemented with usage of HQL language as on below example:

```java
public List<Tweet> findBySinglePlainQueryInDescriptionForConfigurationAndRegconfigTypeCastOperatorFunctionObjectInstanceWithHQL(String phrase, String configuration) {
        //plainto_tsquery regconfig cast_operator_function
        String statement = "from Tweet as tweet where text_operator_function(to_tsvector(cast_operator_function(:configuration,'regconfig'), tweet.shortContent), plainto_tsquery(cast_operator_function(:configuration,'regconfig'), :phrase))";
        TypedQuery<Tweet> query = entityManager.createQuery(statement, Tweet.class);
        query.setParameter("phrase", phrase);
        query.setParameter("configuration", configuration);
        return query.getResultList();
    }
```

HQL:

```hql
from
    Tweet as tweet 
where
    text_operator_function(to_tsvector(cast_operator_function(:configuration, 'regconfig'), tweet.shortContent), plainto_tsquery(cast_operator_function(:configuration, 'regconfig'), :phrase))
```

Components [PhraseToTSQueryFunction](#function-phraseto_tsquery) and [WebsearchToTSQueryFunction](#function-websearch_to_tsquery) also have such constructor with configuration parameter passed as object of type RegconfigTypeCastOperatorFunction.

#### Function phraseto_tsquery

PhraseToTSQueryFunction wraps the [phraseto_tsquery](https://www.postgresql.org/docs/9.4/textsearch-intro.html) function.
Let's check below code example:
```java
    public List<Tweet> findBySinglePhraseInDescriptionForConfiguration(String textQuery, String configuration) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tweet> query = cb.createQuery(Tweet.class);
        Root<Tweet> root = query.from(Tweet.class);
        query.select(root);
        query.where(new TextOperatorFunction((NodeBuilder) cb, new TSVectorFunction(root.get("shortContent"), configuration, (NodeBuilder) cb), new PhraseToTSQueryFunction((NodeBuilder) cb, configuration, textQuery), hibernateContext));
        return entityManager.createQuery(query).getResultList();
        }
```

This code is going to produce for the 'english' configuration below SQL statement:

```sql
select
        t1_0.id,
        t1_0.short_content,
        t1_0.title 
    from
        tweet t1_0 
    where
        to_tsvector('english', t1_0.short_content) @@ phraseto_tsquery('english', ?)
```

And the same example but with HQL:

```hql
    public List<Tweet> findBySinglePhraseInDescriptionForConfigurationWithHQL(String phrase, String configuration) {
        //phraseto_tsquery
        String statement = String.format("from Tweet as tweet where text_operator_function(to_tsvector('%1$s', tweet.shortContent), phraseto_tsquery('%1$s', :phrase))", configuration);
        TypedQuery<Tweet> query = entityManager.createQuery(statement, Tweet.class);
        query.setParameter("phrase", phrase);
        return query.getResultList();
    }
```

Component has also constructor to which developer can pass [the cast operator](#cast-operator-and-text-search-configuration)

#### Function websearch_to_tsquery

WebsearchToTSQueryFunction wraps the [websearch_to_tsquery](https://www.postgresql.org/docs/11/textsearch-controls.html) function.
Please be in mind that the websearch_to_tsquery function was added in version 11 of Postgres so this component is going to return error for database version earlier than 11.
Let's check below code example:

```java
public List<Tweet> findCorrectTweetsByWebSearchToTSQueryInDescription(String phrase, String configuration) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tweet> query = cb.createQuery(Tweet.class);
        Root<Tweet> root = query.from(Tweet.class);
        query.select(root);
        query.where(new TextOperatorFunction((NodeBuilder) cb, new TSVectorFunction(root.get("shortContent"), configuration, (NodeBuilder) cb), new WebsearchToTSQueryFunction((NodeBuilder) cb, configuration, phrase), hibernateContext));
        return entityManager.createQuery(query).getResultList();
    }
```

For such code hibernate is going to generate below sql:

```sql
select
        t1_0.id,
        t1_0.short_content,
        t1_0.title 
    from
        tweet t1_0 
    where
        to_tsvector('english', t1_0.short_content) @@ websearch_to_tsquery('english', ?)
```

And the same example but with HQL:

```hql
public List<Tweet> findCorrectTweetsByWebSearchToTSQueryInDescriptionWithHQL(String phrase, String configuration) {
        //websearch_to_tsquery
        String statement = String.format("from Tweet as tweet where text_operator_function(to_tsvector('%1$s', tweet.shortContent), websearch_to_tsquery('%1$s', :phrase))", configuration);
        TypedQuery<Tweet> query = entityManager.createQuery(statement, Tweet.class);
        query.setParameter("phrase", phrase);
        return query.getResultList();
    }
```

Component has also constructor to which developer can pass [the cast operator](#cast-operator-and-text-search-configuration)

#### Function to_tsquery

ToTSQueryFunction wraps the [to_tsquery](https://www.postgresql.org/docs/11/textsearch-controls.html) function.
Let's check below code example:

```java
public List<Tweet> findBySingleToTSQueryFunctionInDescriptionForDefaultConfiguration(String phrase) {
  CriteriaBuilder cb = entityManager.getCriteriaBuilder();
  CriteriaQuery<Tweet> query = cb.createQuery(Tweet.class);
  Root<Tweet> root = query.from(Tweet.class);
  query.select(root);
  query.where(new TextOperatorFunction((NodeBuilder) cb, new TSVectorFunction((SqmTypedNode) cb.coalesce(root.get("shortContent"), " "), (NodeBuilder) cb), new ToTSQueryFunction((NodeBuilder) cb, (String) null, phrase), hibernateContext));
  return entityManager.createQuery(query).getResultList();
}
```

For such code hibernate is going to generate below sql:

```sql
select
        t1_0.id,
        t1_0.short_content,
        t1_0.title 
    from
        tweet t1_0 
    where
        to_tsvector(coalesce(t1_0.short_content, ?)) @@ to_tsquery(?)
```

And the same example but with HQL:

```hql
    public List<Tweet> findBySingleToTSQueryFunctionInDescriptionForDefaultConfigurationWithHQL(String phrase) {
        //to_tsquery
        String statement = "from Tweet as tweet where text_operator_function(to_tsvector(coalesce(tweet.shortContent, \" \")), to_tsquery(:phrase))";
        TypedQuery<Tweet> query = entityManager.createQuery(statement, Tweet.class);
        query.setParameter("phrase", phrase);
        return query.getResultList();
    }
```

Component has also constructor to which developer can pass [the cast operator](#cast-operator-and-text-search-configuration)

### Properties

Please check the [properties](../README.md#properties) section in main document.

### Reporting issues

Please check the [reporting issues](../README.md#reporting-issues) section in main document.

### Project contribution

Please check the [project contribution](../README.md#project-contribution) section in main document.