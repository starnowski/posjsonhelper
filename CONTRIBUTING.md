* [Setup project locally](#setup-project-locally)
    * [Build project](#build-project)
    * [Run integration test locally](#run-integration-test-locally)
* [Commit message](#commit-message)
* [Branch naming convention](#branch-naming-convention)
* [Issues or suggestions](#issues-or-suggestions)

## Setup project locally
To build project you need to install maven (or at least setup user repository directory and required environment variables).
To fully build project locally it is required to have installed jdk versions 8, 11, and 17.
That is because module for hibernate 5 requires jdk 8 but module for hibernate 6 support requires version jdk 11.
The jdk 17 is required for modules that tests hibernate 6 support module.

```

**Important**, please bear in mind that your "JAVA_HOME" environment property should point to JDK with version 8!

### Build project
Execute maven wrapper script

On Unix:
```bash
mvnw clean install
```

On Windows:
```powershell
mvnw.cmd clean install
```

### Run integration test locally
TODO

## Commit message
  * Each commit message should start with prefix which contains hash and issue number, for example "#132"
  * Commits which doesn't affect project build status like updating docs files "README.md" doesn't have to build by Github action. To do that commit message should contain sufix "[skip ci]" 

## Branch naming convention
Try to name your branch based on type of issue related to it, for example:
    
  * Feature - Prefix "feature/", issue number and some short description, for example:
    
    Branch name for feature related to integration with Travis CI and with number 113.
    "feature/113_travis_integration"
  * Bugfix - Prefix "bugfix/", issue number and some short description, for example:
  
    Branch name for bugfix related to NullPointer exception and with number 227.
    "bugfix/227_nullpointer_exception"
    
## Tests
A developer who adds changes should implement tests that cover added code for any bug or a new feature.
There should be added unit tests for any new code generally (there might be exceptions).
If a developer does not have an idea for unit tests, can always ask about this in a comment for pull-request.

A developer should also implement integration tests if the applied changes for new code or already existing change somehow how the SQL is being generated.

    
## Issues or suggestions
Please report any typos or suggestions about document by creating an issue to the github project.

IN PROGRESS - suggestions are welcomed!
Please be in mind that this document is still in progress.
That means that if your have any suggestions or questions about this document or even whole project than please don't hesitate to write about.
If you don't like to report your suggestions as issue than you can also write to me on my [linkedin account](https://pl.linkedin.com/in/szymon-tarnowski-a104b4150) 

### Adding licence

To add licence header from file license.txt go to directory for specific module and execute below command.

```bash
mvn license:format
```

Check ["Adding License Information Using Maven"](https://dzone.com/articles/adding-license-information) for more details.

### Useful links

https://thorben-janssen.com/persist-postgresqls-jsonb-data-type-hibernate/
https://stackoverflow.com/questions/15974474/mapping-postgresql-json-column-to-a-hibernate-entity-property
https://prateek-ashtikar512.medium.com/how-to-handle-json-in-postgresql-5e2745d5324
https://fullstackdeveloper.guru/2020/05/29/how-to-map-json-data-in-postgresql-database-to-a-hibernate-entity-column/

https://www.postgresql.org/docs/10/functions-json.html
https://medium.com/geekculture/postgres-jsonb-usage-and-performance-analysis-cdbd1242a018
https://stackoverflow.com/questions/50464741/how-to-escape-question-mark-character-with-spring-jparepository
 