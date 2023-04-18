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
#TODO
* [How to start using posjsonhelper](#how-to-start-using-posjsonhelper)
    * [Setting maven dependency](#setting-maven-dependency)
    * [Building project locally](#building-project-locally)
* [How to attach postgresql dialect](#how-to-attach-postgresql dialect)
* [How to use query helper](#how-to-use-query-helper)
* [Reporting issues](#reporting-issues)
* [Project contribution](#project-contribution)

# Introduction
Posjsonhelper library is an open-source project that adds support of Hibernate query for [postgresql json functions](https://www.postgresql.org/docs/10/functions-json.html).
The library is written in a java programming language.
The project for this moment supports only Hibernate with version 5.
The required version of java is at least version 8.

#TODO