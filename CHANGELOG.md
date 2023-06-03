# Posjsonhelper changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

https://keepachangelog.com/en/1.0.0/
https://www.markdownguide.org/basic-syntax/


* [Unreleased](#unreleased)
* [0.2.0](#020---2023-06-03)
* [0.1.2](#012---2023-05-12)
* [0.1.1](#011---2023-05-11)
* [0.1.0](#010---2023-05-06)

## [Unreleased]

## [0.2.0] - 2023-06-03

-   Added type com.github.starnowski.posjsonhelper.hibernate6.descriptor.AbstractConditionalFunctionDescriptorRegister [24](https://github.com/starnowski/posjsonhelper/issues/24)
-   Added type com.github.starnowski.posjsonhelper.hibernate6.descriptor.FunctionByNameRegister [24](https://github.com/starnowski/posjsonhelper/issues/24)
-   Added type com.github.starnowski.posjsonhelper.hibernate6.descriptor.JsonArrayFunctionDescriptor [24](https://github.com/starnowski/posjsonhelper/issues/24)
-   Added type com.github.starnowski.posjsonhelper.hibernate6.descriptor.JsonArrayFunctionDescriptorRegister [24](https://github.com/starnowski/posjsonhelper/issues/24)

## [0.1.2] - 2023-05-12

### Changed
    
- Change scope for dependency com.github.starnowski.posjsonhelper:test-utils to the test  [39](https://github.com/starnowski/posjsonhelper/issues/39)

## [0.1.1] - 2023-05-11

### Changed
    
- Change scope for dependency com.github.starnowski.posjsonhelper:test-utils to the test  [39](https://github.com/starnowski/posjsonhelper/issues/39)

## [0.1.0] - 2023-05-06

### Added

-   Added type com.github.starnowski.posjsonhelper.hibernate5.PostgreSQLDialectEnricher
-   Added type com.github.starnowski.posjsonhelper.hibernate5.JsonBExtractPathText
-   Added type com.github.starnowski.posjsonhelper.hibernate5.JsonBExtractPath
-   Added type com.github.starnowski.posjsonhelper.hibernate5.AbstractJsonBExtractPath
-   Added type com.github.starnowski.posjsonhelper.hibernate5.predicates.AbstractJsonbArrayStringsExistPredicate
-   Added type com.github.starnowski.posjsonhelper.hibernate5.predicates.JsonbAllArrayStringsExistPredicate
-   Added type com.github.starnowski.posjsonhelper.hibernate5.predicates.JsonbAnyArrayStringsExistPredicate
-   Added type com.github.starnowski.posjsonhelper.hibernate5.functions.JsonArrayFunction
-   Added type com.github.starnowski.posjsonhelper.hibernate5.dialects.PostgreSQL10DialectWrapper
-   Added type com.github.starnowski.posjsonhelper.hibernate5.dialects.PostgreSQL95DialectWrapper
-   Added type com.github.starnowski.posjsonhelper.core.SystemPropertyReader
-   Added type com.github.starnowski.posjsonhelper.core.HibernateContextPropertiesSupplier
-   Added type com.github.starnowski.posjsonhelper.core.HibernateContext
-   Added type com.github.starnowski.posjsonhelper.core.DatabaseOperationType
-   Added type com.github.starnowski.posjsonhelper.core.DatabaseOperationExecutorFacade
-   Added type com.github.starnowski.posjsonhelper.core.DatabaseOperationExecutor
-   Added type com.github.starnowski.posjsonhelper.core.CoreContextPropertiesSupplier
-   Added type com.github.starnowski.posjsonhelper.core.Context
-   Added type com.github.starnowski.posjsonhelper.core.Constants
-   Added type com.github.starnowski.posjsonhelper.core.util.Pair
-   Added type com.github.starnowski.posjsonhelper.core.sql.SQLDefinitionFactoryFacade
-   Added type com.github.starnowski.posjsonhelper.core.sql.JsonbAnyArrayStringsExistFunctionContextFactory
-   Added type com.github.starnowski.posjsonhelper.core.sql.JsonbAllArrayStringsExistFunctionContextFactory
-   Added type com.github.starnowski.posjsonhelper.core.sql.ISQLDefinitionFactoryParameters
-   Added type com.github.starnowski.posjsonhelper.core.sql.ISQLDefinitionFactory
-   Added type com.github.starnowski.posjsonhelper.core.sql.ISQLDefinitionContextFactory
-   Added type com.github.starnowski.posjsonhelper.core.sql.ISQLDefinition
-   Added type com.github.starnowski.posjsonhelper.core.sql.DefaultSQLDefinition
-   Added type com.github.starnowski.posjsonhelper.core.sql.functions.JsonbAnyArrayStringsExistFunctionProducer
-   Added type com.github.starnowski.posjsonhelper.core.sql.functions.JsonbAllArrayStringsExistFunctionProducer
-   Added type com.github.starnowski.posjsonhelper.core.sql.functions.IFunctionFactoryParameters
-   Added type com.github.starnowski.posjsonhelper.core.sql.functions.IFunctionArgument
-   Added type com.github.starnowski.posjsonhelper.core.sql.functions.DefaultFunctionFactoryParameters
-   Added type com.github.starnowski.posjsonhelper.core.sql.functions.DefaultFunctionArgument
-   Added type com.github.starnowski.posjsonhelper.core.sql.functions.AbstractFunctionDefinitionFactory
-   Added type com.github.starnowski.posjsonhelper.core.sql.functions.AbstractDefaultFunctionDefinitionFactory
-   Added type com.github.starnowski.posjsonhelper.core.operations.ValidateOperationsProcessor
-   Added type com.github.starnowski.posjsonhelper.core.operations.IDatabaseOperationsProcessor
-   Added type com.github.starnowski.posjsonhelper.core.operations.DropOperationsProcessor
-   Added type com.github.starnowski.posjsonhelper.core.operations.DatabaseOperationsLoggerProcessor
-   Added type com.github.starnowski.posjsonhelper.core.operations.CreateOperationsProcessor
-   Added type com.github.starnowski.posjsonhelper.core.operations.util.SQLUtil
-   Added type com.github.starnowski.posjsonhelper.core.operations.exceptions.ValidationDatabaseOperationsException
-   Added type com.github.starnowski.posjsonhelper.core.operations.exceptions.AbstractDatabaseOperationsException
