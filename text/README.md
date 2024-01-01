# posjsonhelper text

* [Introduction](#introduction)
* [How to start using posjsonhelper](#how-to-start-using-posjsonhelper)
    * [Setting maven dependency](#setting-maven-dependency)
    * [Building project locally](#building-project-locally)

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
If someone would like to build the project locally from the source please see the CONTRIBUTING.md file to check how to set up the project locally.