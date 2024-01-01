# posjsonhelper text

* [Introduction](#introduction)
* [How to start using posjsonhelper](#how-to-start-using-posjsonhelper)
    * [Setting maven dependency](#setting-maven-dependency)
    * [Building project locally](#building-project-locally)
* [How to attach FunctionContributor](#how-to-attach-functioncontributor)
* [How to use query helper](#how-to-use-query-helper)
  * [Hibernate Context](#hibernate-context)
  * [Text operator wrapper '@@'](#text-operator-wrapper--)

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
            <version>0.3.0</version>
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

#### Text operator wrapper  '@@'

TextOperatorFunction is component that wraps Postgres [text operator](https://www.postgresql.org/docs/9.4/textsearch-intro.html).
Beside of the to_tsvector function, this operator is necessary to do text search.
The usage is going to be presented in below examples.


