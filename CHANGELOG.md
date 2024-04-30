# Posjsonhelper changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

https://keepachangelog.com/en/1.0.0/
https://www.markdownguide.org/basic-syntax/


* [Unreleased](#unreleased)
* [0.3.2](#032---2024-05-01)
* [0.3.1](#031---2024-03-11)
* [0.3.0](#030---2024-01-03)
* [0.2.1](#021---2023-06-20)
* [0.2.0](#020---2023-06-13)
* [0.1.2](#012---2023-05-12)
* [0.1.1](#011---2023-05-11)
* [0.1.0](#010---2023-05-06)

## [Unreleased]

## [0.3.2] - 2024-05-01

### Added

- Added support for "||" operator and jsonb_set function to be able to use them in UPDATE statement operation [120](https://github.com/starnowski/posjsonhelper/issues/120)
  - Added com.github.starnowski.posjsonhelper.core.Constants#JSONB_SET_FUNCTION_NAME constant [120](https://github.com/starnowski/posjsonhelper/issues/120)
  - Added com.github.starnowski.posjsonhelper.core.Constants#DEFAULT_CONCATENATE_JSONB_HIBERNATE_OPERATOR constant [120](https://github.com/starnowski/posjsonhelper/issues/120)
  - Added com.github.starnowski.posjsonhelper.core.HibernateContext.concatenateJsonbOperator property and its setter and getter [120](https://github.com/starnowski/posjsonhelper/issues/120)
  - Added com.github.starnowski.posjsonhelper.hibernate6.Hibernate6JsonUpdateStatementBuilder type [120](https://github.com/starnowski/posjsonhelper/issues/120)
  - Added com.github.starnowski.posjsonhelper.hibernate6.descriptor.ConcatenateJsonbOperatorDescriptor type [120](https://github.com/starnowski/posjsonhelper/issues/120)
  - Added com.github.starnowski.posjsonhelper.hibernate6.descriptor.ConcatenateJsonbOperatorDescriptorRegister type [120](https://github.com/starnowski/posjsonhelper/issues/120)
  - Added com.github.starnowski.posjsonhelper.hibernate6.descriptor.ConcatenateJsonbOperatorDescriptorRegisterFactory type [120](https://github.com/starnowski/posjsonhelper/issues/120)
  - Added com.github.starnowski.posjsonhelper.hibernate6.descriptor.JsonbSetFunctionDescriptor type [120](https://github.com/starnowski/posjsonhelper/issues/120)
  - Added com.github.starnowski.posjsonhelper.hibernate6.descriptor.JsonbSetFunctionDescriptorRegister type [120](https://github.com/starnowski/posjsonhelper/issues/120)
  - Added com.github.starnowski.posjsonhelper.hibernate6.descriptor.JsonbSetFunctionDescriptorRegisterFactory type [120](https://github.com/starnowski/posjsonhelper/issues/120)
  - Added com.github.starnowski.posjsonhelper.hibernate6.functions.JsonbSetFunctionDescriptorRegisterFactory type [120](https://github.com/starnowski/posjsonhelper/issues/120)
  - Added com.github.starnowski.posjsonhelper.hibernate6.operators.ConcatenateJsonbOperator type [120](https://github.com/starnowski/posjsonhelper/issues/120)
  - Added com.github.starnowski.posjsonhelper.hibernate6.operators.JsonbCastOperatorFunction type [120](https://github.com/starnowski/posjsonhelper/issues/120)
  - Added com.github.starnowski.posjsonhelper.json.core.sql.DefaultJsonUpdateStatementOperationFilter type [120](https://github.com/starnowski/posjsonhelper/issues/120)
  - Added com.github.starnowski.posjsonhelper.json.core.sql.DefaultJsonUpdateStatementOperationSort type [120](https://github.com/starnowski/posjsonhelper/issues/120)
  - Added com.github.starnowski.posjsonhelper.json.core.sql.JsonTextArray type [120](https://github.com/starnowski/posjsonhelper/issues/120)
  - Added com.github.starnowski.posjsonhelper.json.core.sql.JsonTextArrayBuilder type [120](https://github.com/starnowski/posjsonhelper/issues/120)
  - Added com.github.starnowski.posjsonhelper.json.core.sql.JsonUpdateStatementConfiguration type [120](https://github.com/starnowski/posjsonhelper/issues/120)
  - Added com.github.starnowski.posjsonhelper.json.core.sql.JsonUpdateStatementConfigurationBuilder type [120](https://github.com/starnowski/posjsonhelper/issues/120)
  - Added com.github.starnowski.posjsonhelper.json.core.sql.JsonUpdateStatementOperationType type [120](https://github.com/starnowski/posjsonhelper/issues/120)


## [0.3.1] - 2024-03-11

### Added

- Added a set of functions that define which function should be rendered with a schema reference. [116](https://github.com/starnowski/posjsonhelper/issues/116)
  - Added com.github.starnowski.posjsonhelper.core.Context#functionsThatShouldBeExecutedWithSchemaReference property [116](https://github.com/starnowski/posjsonhelper/issues/116)
  - Added com.github.starnowski.posjsonhelper.hibernate6.descriptor.NamedSqmFunctionWithSchemaReferenceDescriptor type [116](https://github.com/starnowski/posjsonhelper/issues/116)

### Changed

  - Changed base type for com.github.starnowski.posjsonhelper.hibernate6.descriptor.AbstractJsonbArrayStringsExistPredicateDescriptor to com.github.starnowski.posjsonhelper.hibernate6.descriptor.NamedSqmFunctionWithSchemaReferenceDescriptor [116](https://github.com/starnowski/posjsonhelper/issues/116)

## [0.3.0] - 2024-01-03

### Added

- Added implementation for the websearch_to_tsquery function [76](https://github.com/starnowski/posjsonhelper/issues/76)
  - Added com.github.starnowski.posjsonhelper.core.Constants#WEBSEARCH_TO_TSQUERY_FUNCTION_NAME constant [76](https://github.com/starnowski/posjsonhelper/issues/76)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.descriptor.WebsearchToTSQueryFunctionDescriptor type [76](https://github.com/starnowski/posjsonhelper/issues/76)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.descriptor.WebsearchToTSQueryFunctionDescriptorRegister type [76](https://github.com/starnowski/posjsonhelper/issues/76)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.descriptor.WebsearchToTSQueryFunctionDescriptorRegisterFactory type [76](https://github.com/starnowski/posjsonhelper/issues/76)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.functions.WebsearchToTSQueryFunction type [76](https://github.com/starnowski/posjsonhelper/issues/76)

- Added support for the cast "::" operator [74](https://github.com/starnowski/posjsonhelper/issues/74)
  - Added com.github.starnowski.posjsonhelper.core.Constants#DEFAULT_CAST_FUNCTION_HIBERNATE_OPERATOR constant [74](https://github.com/starnowski/posjsonhelper/issues/74)
  - Added com.github.starnowski.posjsonhelper.core.HibernateContext.castFunctionOperator property and its setter and getter [74](https://github.com/starnowski/posjsonhelper/issues/74)
  - Added com.github.starnowski.posjsonhelper.hibernate6.Constants#FUNCTIONDESCRIPTORREGISTERFACTORY_TYPES_EXCLUDED_PROPERTY constant [74](https://github.com/starnowski/posjsonhelper/issues/74)
  - Added com.github.starnowski.posjsonhelper.hibernate6.descriptor.CastOperatorFunctionDescriptor type [74](https://github.com/starnowski/posjsonhelper/issues/74)
  - Added com.github.starnowski.posjsonhelper.hibernate6.descriptor.CastOperatorFunctionDescriptorRegister type [74](https://github.com/starnowski/posjsonhelper/issues/74)
  - Added com.github.starnowski.posjsonhelper.hibernate6.descriptor.CastOperatorFunctionDescriptorRegister type [74](https://github.com/starnowski/posjsonhelper/issues/74)
  - Added com.github.starnowski.posjsonhelper.hibernate6.descriptor.CastOperatorFunctionDescriptorRegisterFactory type [74](https://github.com/starnowski/posjsonhelper/issues/74)
  - Added com.github.starnowski.posjsonhelper.hibernate6.operators.CastOperatorFunction type [74](https://github.com/starnowski/posjsonhelper/issues/74)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.operators.RegconfigTypeCastOperatorFunction type [74](https://github.com/starnowski/posjsonhelper/issues/74)

- Added configuration parameter to functions [71](https://github.com/starnowski/posjsonhelper/issues/71)
  - Added com.github.starnowski.posjsonhelper.core.Constants#PHRASETO_TSQUERY_FUNCTION_NAME constant [71](https://github.com/starnowski/posjsonhelper/issues/71)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.descriptor.PhraseToTSQueryFunctionDescriptor type [71](https://github.com/starnowski/posjsonhelper/issues/71)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.descriptor.PhraseToTSQueryFunctionDescriptorRegister type [71](https://github.com/starnowski/posjsonhelper/issues/71)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.descriptor.PhraseToTSQueryFunctionDescriptorRegisterFactory type [71](https://github.com/starnowski/posjsonhelper/issues/71)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.functions.PhraseToTSQueryFunction type [71](https://github.com/starnowski/posjsonhelper/issues/71)

- Added support for the "@@" operator [67](https://github.com/starnowski/posjsonhelper/issues/67)
  - Added com.github.starnowski.posjsonhelper.core.Constants#PLAINTO_TSQUERY_FUNCTION_NAME constant [67](https://github.com/starnowski/posjsonhelper/issues/67)
  - Added com.github.starnowski.posjsonhelper.core.Constants#TO_TSVECTOR_FUNCTION_NAME constant [67](https://github.com/starnowski/posjsonhelper/issues/67)
  - Added com.github.starnowski.posjsonhelper.core.Constants#DEFAULT_TEXT_FUNCTION_HIBERNATE_OPERATOR constant [67](https://github.com/starnowski/posjsonhelper/issues/67)
  - Added com.github.starnowski.posjsonhelper.core.HibernateContext.textFunctionOperator property and its setter and getter [67](https://github.com/starnowski/posjsonhelper/issues/67)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.descriptor.AbstractFunctionWithConfigurationAndTextQueryFunctionDescriptor type [67](https://github.com/starnowski/posjsonhelper/issues/67)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.descriptor.AbstractFunctionWithConfigurationAndTextQueryFunctionDescriptorRegister type [67](https://github.com/starnowski/posjsonhelper/issues/67)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.descriptor.PlainToTSQueryFunctionDescriptor type [67](https://github.com/starnowski/posjsonhelper/issues/67)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.descriptor.PlainToTSQueryFunctionDescriptorRegister type [67](https://github.com/starnowski/posjsonhelper/issues/67)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.descriptor.PlainToTSQueryFunctionDescriptorRegisterFactory type [67](https://github.com/starnowski/posjsonhelper/issues/67)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.descriptor.TSVectorFunctionDescriptor type [67](https://github.com/starnowski/posjsonhelper/issues/67)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.descriptor.TSVectorFunctionDescriptorRegister type [67](https://github.com/starnowski/posjsonhelper/issues/67)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.descriptor.TSVectorFunctionDescriptorRegisterFactory type [67](https://github.com/starnowski/posjsonhelper/issues/67)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.descriptor.TextOperatorFunctionDescriptor type [67](https://github.com/starnowski/posjsonhelper/issues/67)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.descriptor.TextOperatorFunctionDescriptorRegister type [67](https://github.com/starnowski/posjsonhelper/issues/67)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.descriptor.TextOperatorFunctionDescriptorRegisterFactory type [67](https://github.com/starnowski/posjsonhelper/issues/67)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.functions.AbstractFunctionWithConfigurationAndTextQueryFunction type [67](https://github.com/starnowski/posjsonhelper/issues/67)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.functions.PlainToTSQueryFunction type [67](https://github.com/starnowski/posjsonhelper/issues/67)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.functions.TSVectorFunction type [67](https://github.com/starnowski/posjsonhelper/issues/67)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.functions.TextOperatorFunction type [67](https://github.com/starnowski/posjsonhelper/issues/67)

- Moved common component for hibernate 6 to hibernate6-core module [68](https://github.com/starnowski/posjsonhelper/issues/68)
  - Added com.github.starnowski.posjsonhelper.hibernate6.Constants type [68](https://github.com/starnowski/posjsonhelper/issues/68)
  - Type com.github.starnowski.posjsonhelper.hibernate6.descriptor.AbstractConditionalFunctionDescriptorRegister implements interface com.github.starnowski.posjsonhelper.hibernate6.descriptor.FunctionDescriptorRegister [68](https://github.com/starnowski/posjsonhelper/issues/68)
  - Added com.github.starnowski.posjsonhelper.hibernate6.descriptor.FunctionDescriptorRegister type [68](https://github.com/starnowski/posjsonhelper/issues/68)
  - Added com.github.starnowski.posjsonhelper.hibernate6.descriptor.FunctionDescriptorRegisterFactoriesSupplier type [68](https://github.com/starnowski/posjsonhelper/issues/68)
  - Added com.github.starnowski.posjsonhelper.hibernate6.descriptor.FunctionDescriptorRegisterFactory type [68](https://github.com/starnowski/posjsonhelper/issues/68)
  - Added com.github.starnowski.posjsonhelper.hibernate6.descriptor.JsonArrayFunctionDescriptorRegisterFactory type [68](https://github.com/starnowski/posjsonhelper/issues/68)
  - Added com.github.starnowski.posjsonhelper.hibernate6.descriptor.JsonBExtractPathDescriptorRegisterFactory type [68](https://github.com/starnowski/posjsonhelper/issues/68)
  - Added com.github.starnowski.posjsonhelper.hibernate6.descriptor.JsonBExtractPathTextDescriptorRegisterFactory type [68](https://github.com/starnowski/posjsonhelper/issues/68)
  - Added com.github.starnowski.posjsonhelper.hibernate6.descriptor.JsonbAllArrayStringsExistPredicateDescriptorRegisterFactory type [68](https://github.com/starnowski/posjsonhelper/issues/68)
  - Added com.github.starnowski.posjsonhelper.hibernate6.descriptor.JsonbAnyArrayStringsExistPredicateDescriptorRegisterFactory type [68](https://github.com/starnowski/posjsonhelper/issues/68)

- Moved stuff related to JSON operations to json-core from core [65](https://github.com/starnowski/posjsonhelper/issues/65)
  - Added com.github.starnowski.posjsonhelper.core.sql.SQLDefinitionContextFactorySupplier type [65](https://github.com/starnowski/posjsonhelper/issues/65)
  - Added com.github.starnowski.posjsonhelper.core.Constants#SQLDEFINITIONCONTEXTFACTORY_TYPES_PROPERTY constant [65](https://github.com/starnowski/posjsonhelper/issues/65)

### Changed

- Upgrade Hibernate to 6.4 [92](https://github.com/starnowski/posjsonhelper/issues/92)

## [0.2.1] - 2023-06-20

### Added

- Fixed support for hql query in Hibernate 6 [58](https://github.com/starnowski/posjsonhelper/issues/58)
  - Added constructor com.github.starnowski.posjsonhelper.hibernate6.AbstractJsonBExtractPath#AbstractJsonBExtractPath(jakarta.persistence.criteria.Path, java.util.List<? extends SqmTypedNode<?>>, org.hibernate.query.sqm.NodeBuilder, java.lang.String) [58](https://github.com/starnowski/posjsonhelper/issues/58)
  - Added constructor com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPath#JsonBExtractPath(jakarta.persistence.criteria.Path, java.util.List<? extends SqmTypedNode<?>>, org.hibernate.query.sqm.NodeBuilder) [58](https://github.com/starnowski/posjsonhelper/issues/58)
  - Added constructor com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPathText#JsonBExtractPathText(jakarta.persistence.criteria.Path, org.hibernate.query.sqm.NodeBuilder, java.util.List<? extends SqmTypedNode<?>>) [58](https://github.com/starnowski/posjsonhelper/issues/58)
  - Added type com.github.starnowski.posjsonhelper.hibernate6.descriptor.AbstractJsonBExtractPathDescriptor [58](https://github.com/starnowski/posjsonhelper/issues/58)
  - Added type com.github.starnowski.posjsonhelper.hibernate6.descriptor.AbstractJsonBExtractPathDescriptorRegister [58](https://github.com/starnowski/posjsonhelper/issues/58)
  - Added type com.github.starnowski.posjsonhelper.hibernate6.descriptor.AbstractJsonbArrayStringsExistPredicateDescriptor [58](https://github.com/starnowski/posjsonhelper/issues/58)
  - Added type com.github.starnowski.posjsonhelper.hibernate6.descriptor.AbstractJsonbArrayStringsExistPredicateDescriptorRegister [58](https://github.com/starnowski/posjsonhelper/issues/58)
  - Added type com.github.starnowski.posjsonhelper.hibernate6.descriptor.JsonBExtractPathDescriptor [58](https://github.com/starnowski/posjsonhelper/issues/58)
  - Added type com.github.starnowski.posjsonhelper.hibernate6.descriptor.JsonBExtractPathTextDescriptor [58](https://github.com/starnowski/posjsonhelper/issues/58)
  - Added type com.github.starnowski.posjsonhelper.hibernate6.descriptor.JsonbAllArrayStringsExistPredicateDescriptor [58](https://github.com/starnowski/posjsonhelper/issues/58)
  - Added type com.github.starnowski.posjsonhelper.hibernate6.descriptor.JsonbAnyArrayStringsExistPredicateDescriptor [58](https://github.com/starnowski/posjsonhelper/issues/58)
  - Added constructor com.github.starnowski.posjsonhelper.hibernate6.predicates.AbstractJsonbArrayStringsExistPredicate#AbstractJsonbArrayStringsExistPredicate(com.github.starnowski.posjsonhelper.core.HibernateContext, org.hibernate.query.sqm.NodeBuilder, com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPath, com.github.starnowski.posjsonhelper.hibernate6.operators.JsonArrayFunction, java.lang.String) [58](https://github.com/starnowski/posjsonhelper/issues/58)
  - Added constructor com.github.starnowski.posjsonhelper.hibernate6.predicates.JsonbAllArrayStringsExistPredicate#JsonbAllArrayStringsExistPredicate(com.github.starnowski.posjsonhelper.core.HibernateContext, org.hibernate.query.sqm.NodeBuilder, com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPath, com.github.starnowski.posjsonhelper.hibernate6.operators.JsonArrayFunction) [58](https://github.com/starnowski/posjsonhelper/issues/58)
  - Added constructor com.github.starnowski.posjsonhelper.hibernate6.predicates.JsonbAnyArrayStringsExistPredicate#JsonbAnyArrayStringsExistPredicate(com.github.starnowski.posjsonhelper.core.HibernateContext, org.hibernate.query.sqm.NodeBuilder, com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPath, com.github.starnowski.posjsonhelper.hibernate6.operators.JsonArrayFunction) [58](https://github.com/starnowski/posjsonhelper/issues/58)

### Changed

- Changed default constructor com.github.starnowski.posjsonhelper.hibernate6.descriptor.JsonArrayFunctionDescriptor#JsonArrayFunctionDescriptor() to com.github.starnowski.posjsonhelper.hibernate6.descriptor.JsonArrayFunctionDescriptor#JsonArrayFunctionDescriptor(com.github.starnowski.posjsonhelper.core.HibernateContext) [58](https://github.com/starnowski/posjsonhelper/issues/58)

### Removed

- Removed type com.github.starnowski.posjsonhelper.hibernate6.descriptor.FunctionByNameRegister [58](https://github.com/starnowski/posjsonhelper/issues/58)

## [0.2.0] - 2023-06-13

### Added

- Added support for hibernate 6 [24](https://github.com/starnowski/posjsonhelper/issues/24)
  -   Added type com.github.starnowski.posjsonhelper.hibernate6.descriptor.AbstractConditionalFunctionDescriptorRegister [24](https://github.com/starnowski/posjsonhelper/issues/24)
  -   Added type com.github.starnowski.posjsonhelper.hibernate6.descriptor.FunctionByNameRegister [24](https://github.com/starnowski/posjsonhelper/issues/24)
  -   Added type com.github.starnowski.posjsonhelper.hibernate6.descriptor.JsonArrayFunctionDescriptor [24](https://github.com/starnowski/posjsonhelper/issues/24)
  -   Added type com.github.starnowski.posjsonhelper.hibernate6.descriptor.JsonArrayFunctionDescriptorRegister [24](https://github.com/starnowski/posjsonhelper/issues/24)
  -   Added type com.github.starnowski.posjsonhelper.hibernate6.operators.JsonArrayFunction [24](https://github.com/starnowski/posjsonhelper/issues/24)
  -   Added type com.github.starnowski.posjsonhelper.hibernate6.predicates.AbstractJsonbArrayStringsExistPredicate [24](https://github.com/starnowski/posjsonhelper/issues/24)
  -   Added type com.github.starnowski.posjsonhelper.hibernate6.predicates.JsonbAllArrayStringsExistPredicate [24](https://github.com/starnowski/posjsonhelper/issues/24)
  -   Added type com.github.starnowski.posjsonhelper.hibernate6.predicates.JsonbAnyArrayStringsExistPredicate [24](https://github.com/starnowski/posjsonhelper/issues/24)
  -   Added type com.github.starnowski.posjsonhelper.hibernate6.AbstractJsonBExtractPath [24](https://github.com/starnowski/posjsonhelper/issues/24)
  -   Added type com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPath [24](https://github.com/starnowski/posjsonhelper/issues/24)
  -   Added type com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPathText [24](https://github.com/starnowski/posjsonhelper/issues/24)
  -   Added type com.github.starnowski.posjsonhelper.hibernate6.PosjsonhelperFunctionContributor [24](https://github.com/starnowski/posjsonhelper/issues/24)
  -   Added type com.github.starnowski.posjsonhelper.hibernate6.SqmFunctionRegistryEnricher [24](https://github.com/starnowski/posjsonhelper/issues/24)

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
