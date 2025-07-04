# Posjsonhelper changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

https://keepachangelog.com/en/1.0.0/
https://www.markdownguide.org/basic-syntax/


* [Unreleased](#unreleased)
* [0.4.3](#043---2025-06-29)
* [0.4.2](#042---2024-11-18)
* [0.4.1](#041---2024-06-09)
* [0.4.0](#040---2024-06-08)
* [0.3.3](#033---2024-05-22)
* [0.3.2](#032---2024-05-01)
* [0.3.1](#031---2024-03-11)
* [0.3.0](#030---2024-01-03)
* [0.2.1](#021---2023-06-20)
* [0.2.0](#020---2023-06-13)
* [0.1.2](#012---2023-05-12)
* [0.1.1](#011---2023-05-11)
* [0.1.0](#010---2023-05-06)

## [Unreleased]

## [0.4.3] - 2025-06-29

### Added

- Added support for to_tsquery function  [155](https://github.com/starnowski/posjsonhelper/issues/155)
  - Added com.github.starnowski.posjsonhelper.core.Constants#TO_TSQUERY_FUNCTION_NAME constant [155](https://github.com/starnowski/posjsonhelper/issues/155)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.descriptor.ToTSQueryFunctionDescriptor type [155](https://github.com/starnowski/posjsonhelper/issues/155)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.descriptor.ToTSQueryFunctionDescriptorRegister type [155](https://github.com/starnowski/posjsonhelper/issues/155)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.descriptor.ToTSQueryFunctionDescriptorRegisterFactory type [155](https://github.com/starnowski/posjsonhelper/issues/155)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.functions.ToTSQueryFunction type [155](https://github.com/starnowski/posjsonhelper/issues/155)

## [0.4.2] - 2024-11-18

### Added

- Added support for adding and removing items from json array  [149](https://github.com/starnowski/posjsonhelper/issues/149)
  - Added org.json.json library as optional dependency for com.github.starnowski.posjsonhelper:hibernate6 module [149](https://github.com/starnowski/posjsonhelper/issues/149)
  - Added com.github.starnowski.posjsonhelper.hibernate6.Hibernate6JsonUpdateStatementBuilder#withAddArrayItemsFunctionFactory(com.github.starnowski.posjsonhelper.hibernate6.Hibernate6JsonUpdateStatementBuilder.AddArrayItemsFunctionFactory<T, C>) method [149](https://github.com/starnowski/posjsonhelper/issues/149)
  - Added com.github.starnowski.posjsonhelper.hibernate6.Hibernate6JsonUpdateStatementBuilder#withRemoveArrayItemsFunctionFactory(com.github.starnowski.posjsonhelper.hibernate6.Hibernate6JsonUpdateStatementBuilder.RemoveArrayItemsFunctionFactory<T, C>) method [149](https://github.com/starnowski/posjsonhelper/issues/149)
  - Added com.github.starnowski.posjsonhelper.hibernate6.Hibernate6JsonUpdateStatementBuilder.AddArrayItemsFunctionFactory<T, C> interface [149](https://github.com/starnowski/posjsonhelper/issues/149)
  - Added com.github.starnowski.posjsonhelper.hibernate6.Hibernate6JsonUpdateStatementBuilder.RemoveArrayItemsFunctionFactory<T, C> interface [149](https://github.com/starnowski/posjsonhelper/issues/149)
  - Added com.github.starnowski.posjsonhelper.hibernate6.Hibernate6JsonUpdateStatementBuilder#appendRemoveArrayItems(com.github.starnowski.posjsonhelper.json.core.sql.JsonTextArray, String) method [149](https://github.com/starnowski/posjsonhelper/issues/149)
  - Added com.github.starnowski.posjsonhelper.hibernate6.Hibernate6JsonUpdateStatementBuilder#appendRemoveArrayItems(com.github.starnowski.posjsonhelper.json.core.sql.JsonTextArray, java.util.Collection) method [149](https://github.com/starnowski/posjsonhelper/issues/149)
  - Added com.github.starnowski.posjsonhelper.hibernate6.Hibernate6JsonUpdateStatementBuilder#appendAddArrayItems(com.github.starnowski.posjsonhelper.json.core.sql.JsonTextArray, String) method [149](https://github.com/starnowski/posjsonhelper/issues/149)
  - Added com.github.starnowski.posjsonhelper.hibernate6.Hibernate6JsonUpdateStatementBuilder#appendAddArrayItems(com.github.starnowski.posjsonhelper.json.core.sql.JsonTextArray, java.util.Collection) method [149](https://github.com/starnowski/posjsonhelper/issues/149)
  - Added com.github.starnowski.posjsonhelper.json.core.sql.JsonUpdateStatementOperationType.java#ADD_ARRAY_ITEMS enum value [149](https://github.com/starnowski/posjsonhelper/issues/149)
  - Added com.github.starnowski.posjsonhelper.json.core.sql.JsonUpdateStatementOperationType.java#REMOVE_ARRAY_ITEMS enum value [149](https://github.com/starnowski/posjsonhelper/issues/149)

- Added constructor to AbstractJsonBExtractPath with SqmTypedNode argument [147](https://github.com/starnowski/posjsonhelper/issues/147)
  - Added constructor com.github.starnowski.posjsonhelper.hibernate6.AbstractJsonBExtractPath#AbstractJsonBExtractPath(org.hibernate.query.sqm.tree.SqmTypedNode, org.hibernate.query.sqm.NodeBuilder, List<java.lang.String>, java.lang.String) constructor [147](https://github.com/starnowski/posjsonhelper/issues/147)
  - Added constructor com.github.starnowski.posjsonhelper.hibernate6.AbstractJsonBExtractPath#AbstractJsonBExtractPath(org.hibernate.query.sqm.tree.SqmTypedNode, List<? extends org.hibernate.query.sqm.tree.SqmTypedNode<?>>, org.hibernate.query.sqm.NodeBuilder, java.lang.String) constructor [147](https://github.com/starnowski/posjsonhelper/issues/147)
  - Added constructor com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPath#JsonBExtractPath(org.hibernate.query.sqm.tree.SqmTypedNode, List<java.lang.String>, org.hibernate.query.sqm.NodeBuilder) constructor [147](https://github.com/starnowski/posjsonhelper/issues/147)
  - Added constructor com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPath#JsonBExtractPath(org.hibernate.query.sqm.tree.SqmTypedNode, org.hibernate.query.sqm.NodeBuilder, List<? extends org.hibernate.query.sqm.tree.SqmTypedNode<?>>) constructor [147](https://github.com/starnowski/posjsonhelper/issues/147)
  - Added constructor com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPathText#JsonBExtractPathText(org.hibernate.query.sqm.tree.SqmTypedNode, List<java.lang.String>, org.hibernate.query.sqm.NodeBuilder) constructor [147](https://github.com/starnowski/posjsonhelper/issues/147)
  - Added constructor com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPathText#JsonBExtractPathText(org.hibernate.query.sqm.tree.SqmTypedNode, org.hibernate.query.sqm.NodeBuilder, List<? extends org.hibernate.query.sqm.tree.SqmTypedNode<?>>) constructor [147](https://github.com/starnowski/posjsonhelper/issues/147)

### Removed

  - Removed constructor com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPath#JsonBExtractPath(jakarta.persistence.criteria.Path, org.hibernate.query.sqm.NodeBuilder, List<? extends org.hibernate.query.sqm.tree.SqmTypedNode<?>>) constructor [147](https://github.com/starnowski/posjsonhelper/issues/147)
  - Removed constructor com.github.starnowski.posjsonhelper.hibernate6.JsonBExtractPathText#JsonBExtractPathText(jakarta.persistence.criteria.Path, org.hibernate.query.sqm.NodeBuilder, List<? extends org.hibernate.query.sqm.tree.SqmTypedNode<?>>) constructor [147](https://github.com/starnowski/posjsonhelper/issues/147)


## [0.4.1] - 2024-06-09

### Changed

- Fixed the reading the source file with procedure body in RemoveJsonValuesFromJsonArrayFunctionProducer class [143](https://github.com/starnowski/posjsonhelper/issues/143)
  - Fixed how file with SQL procedure body is read in com.github.starnowski.posjsonhelper.json.core.sql.functions.RemoveJsonValuesFromJsonArrayFunctionProducer#RemoveJsonValuesFromJsonArrayFunctionProducer() constructor [143](https://github.com/starnowski/posjsonhelper/issues/143)

## [0.4.0] - 2024-06-08

### Added

- Added custom generic (java generic type) value to com.github.starnowski.posjsonhelper.json.core.sql.JsonUpdateStatementConfiguration.JsonUpdateStatementOperation type [138](https://github.com/starnowski/posjsonhelper/issues/138)
  - Added generic C type declaration to com.github.starnowski.posjsonhelper.hibernate6.Hibernate6JsonUpdateStatementBuilder type [138](https://github.com/starnowski/posjsonhelper/issues/138)
  - Added com.github.starnowski.posjsonhelper.hibernate6.Hibernate6JsonUpdateStatementBuilder.JsonbSetFunctionFactory interface [138](https://github.com/starnowski/posjsonhelper/issues/138)
  - Added com.github.starnowski.posjsonhelper.hibernate6.Hibernate6JsonUpdateStatementBuilder.DefaultJsonbSetFunctionFactory type [138](https://github.com/starnowski/posjsonhelper/issues/138)
  - Added com.github.starnowski.posjsonhelper.hibernate6.Hibernate6JsonUpdateStatementBuilder.DeleteJsonbBySpecifiedPathOperatorFactory interface [138](https://github.com/starnowski/posjsonhelper/issues/138)
  - Added com.github.starnowski.posjsonhelper.hibernate6.Hibernate6JsonUpdateStatementBuilder.DefaultDeleteJsonbBySpecifiedPathOperatorFactory type [138](https://github.com/starnowski/posjsonhelper/issues/138)
  - Added com.github.starnowski.posjsonhelper.hibernate6.Hibernate6JsonUpdateStatementBuilder#withJsonbSetFunctionFactory(com.github.starnowski.posjsonhelper.hibernate6.Hibernate6JsonUpdateStatementBuilder#JsonbSetFunctionFactory) method [138](https://github.com/starnowski/posjsonhelper/issues/138)
  - Added com.github.starnowski.posjsonhelper.hibernate6.Hibernate6JsonUpdateStatementBuilder#withDeleteJsonbBySpecifiedPathOperatorFactory(com.github.starnowski.posjsonhelper.hibernate6.Hibernate6JsonUpdateStatementBuilder#DeleteJsonbBySpecifiedPathOperatorFactory) method [138](https://github.com/starnowski/posjsonhelper/issues/138)
  - Added com.github.starnowski.posjsonhelper.hibernate6.Hibernate6JsonUpdateStatementBuilder#appendJsonbSet(com.github.starnowski.posjsonhelper.json.core.sql.JsonTextArray, java.lang.String, <C>) method [138](https://github.com/starnowski/posjsonhelper/issues/138)
  - Added JsonbSetFunction(org.hibernate.query.sqm.NodeBuilder, org.hibernate.query.sqm.tree.SqmTypedNode, java.lang.String, org.hibernate.query.sqm.tree.SqmTypedNode, com.github.starnowski.posjsonhelper.core.HibernateContext) constructor [138](https://github.com/starnowski/posjsonhelper/issues/138)
  - Added generic T type declaration to com.github.starnowski.posjsonhelper.json.core.sql.DefaultJsonUpdateStatementOperationFilter type [138](https://github.com/starnowski/posjsonhelper/issues/138)
  - Added generic T type declaration to com.github.starnowski.posjsonhelper.json.core.sql.DefaultJsonUpdateStatementOperationSort type [138](https://github.com/starnowski/posjsonhelper/issues/138)
  - Added generic T type declaration to com.github.starnowski.posjsonhelper.json.core.sql.JsonUpdateStatementConfiguration type [138](https://github.com/starnowski/posjsonhelper/issues/138)
  - Added generic T type declaration to com.github.starnowski.posjsonhelper.json.core.sql.JsonUpdateStatementConfigurationBuilder type [138](https://github.com/starnowski/posjsonhelper/issues/138)
  - Added com.github.starnowski.posjsonhelper.json.core.sql.JsonUpdateStatementConfigurationBuilder#append(com.github.starnowski.posjsonhelper.json.core.sql.JsonUpdateStatementOperationType, com.github.starnowski.posjsonhelper.json.core.sql.JsonTextArray, java.lang.String, <T>) method [138](https://github.com/starnowski/posjsonhelper/issues/138)

- Added SQL functions that adds items to jsonb array and another that removes it [140](https://github.com/starnowski/posjsonhelper/issues/140)
  - Added com.github.starnowski.posjsonhelper.core.Constants#DEFAULT_REMOVE_VALUES_FROM_JSON_ARRAY_FUNCTION_NAME constant [140](https://github.com/starnowski/posjsonhelper/issues/140)
  - Added com.github.starnowski.posjsonhelper.core.Constants#REMOVE_VALUES_FROM_JSON_ARRAY_FUNCTION_NAME_PROPERTY constant [140](https://github.com/starnowski/posjsonhelper/issues/140)
  - Added com.github.starnowski.posjsonhelper.core.Constants#REMOVE_VALUES_FROM_JSON_ARRAY_HIBERNATE_FUNCTION_PROPERTY constant [140](https://github.com/starnowski/posjsonhelper/issues/140)
  - Added com.github.starnowski.posjsonhelper.core.Context#removeValuesFromJsonArrayFunctionReference property and getter method [140](https://github.com/starnowski/posjsonhelper/issues/140)
  - Added com.github.starnowski.posjsonhelper.core.Context.ContextBuilder#withRemoveValuesFromJsonArrayFunctionReference(java.lang.String) method [140](https://github.com/starnowski/posjsonhelper/issues/140)
  - Added com.github.starnowski.posjsonhelper.core.HibernateContext#removeJsonValuesFromJsonArrayFunction property and getter [140](https://github.com/starnowski/posjsonhelper/issues/140)
  - Added com.github.starnowski.posjsonhelper.core.HibernateContext.ContextBuilder#withRemoveJsonValuesFromJsonArrayFunction(java.lang.String) method [140](https://github.com/starnowski/posjsonhelper/issues/140)
  - Added com.github.starnowski.posjsonhelper.core.sql.functions.DefaultFunctionArgument#name property and getter method [140](https://github.com/starnowski/posjsonhelper/issues/140)
  - Added com.github.starnowski.posjsonhelper.core.sql.functions.IFunctionArgument#getName() method [140](https://github.com/starnowski/posjsonhelper/issues/140)
  - Added com.github.starnowski.posjsonhelper.hibernate6.descriptor.RemoveJsonValuesFromJsonArrayFunctionDescriptor type [140](https://github.com/starnowski/posjsonhelper/issues/140)
  - Added com.github.starnowski.posjsonhelper.hibernate6.descriptor.RemoveJsonValuesFromJsonArrayFunctionDescriptorRegister type [140](https://github.com/starnowski/posjsonhelper/issues/140)
  - Added com.github.starnowski.posjsonhelper.hibernate6.descriptor.RemoveJsonValuesFromJsonArrayFunctionDescriptorRegisterFactory type [140](https://github.com/starnowski/posjsonhelper/issues/140)
  - Added com.github.starnowski.posjsonhelper.hibernate6.functions.RemoveJsonValuesFromJsonArrayFunction type [140](https://github.com/starnowski/posjsonhelper/issues/140)
  - Added com.github.starnowski.posjsonhelper.hibernate6.functions.ConcatenateJsonbOperator#ConcatenateJsonbOperator(org.hibernate.query.sqm.NodeBuilder, org.hibernate.query.sqm.tree.SqmTypedNode, org.hibernate.query.sqm.tree.SqmTypedNode, com.github.starnowski.posjsonhelper.core.HibernateContext)  constructor [140](https://github.com/starnowski/posjsonhelper/issues/140)
  - Added com.github.starnowski.posjsonhelper.json.core.sql.JsonUpdateStatementConfiguration.JsonUpdateStatementOperation#getCustomValue() method [140](https://github.com/starnowski/posjsonhelper/issues/140)
  - Added com.github.starnowski.posjsonhelper.json.core.sql.RemoveJsonValuesFromJsonArrayFunctionContextFactory type [140](https://github.com/starnowski/posjsonhelper/issues/140)
  - Added com.github.starnowski.posjsonhelper.json.core.sql.functions.RemoveJsonValuesFromJsonArrayFunctionProducer type [140](https://github.com/starnowski/posjsonhelper/issues/140)

## [0.3.3] - 2024-05-22
 
### Added

- Added constructor to com.github.starnowski.posjsonhelper.text.hibernate6.functions.TSVectorFunction with org.hibernate.query.sqm.tree.SqmTypedNode parameter [135](https://github.com/starnowski/posjsonhelper/issues/135)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.functions.TSVectorFunction(org.hibernate.query.sqm.tree.SqmTypedNode, org.hibernate.query.sqm.NodeBuilder) constructor [135](https://github.com/starnowski/posjsonhelper/issues/135)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.functions.TSVectorFunction(org.hibernate.query.sqm.tree.SqmTypedNode, java.lang.String, org.hibernate.query.sqm.NodeBuilder) constructor [135](https://github.com/starnowski/posjsonhelper/issues/135)
  - Added com.github.starnowski.posjsonhelper.text.hibernate6.functions.TSVectorFunction(org.hibernate.query.sqm.tree.SqmTypedNode, org.hibernate.query.sqm.tree.expression.SqmExpression, org.hibernate.query.sqm.NodeBuilder) constructor [135](https://github.com/starnowski/posjsonhelper/issues/135)

## [0.3.2] - 2024-05-01

### Added

- Added support for "#-" operator to be able to use them in UPDATE statement operation [121](https://github.com/starnowski/posjsonhelper/issues/121)
  - Added com.github.starnowski.posjsonhelper.core.Constants#DEFAULT_DELETE_JSONB_BY_SPECIFIC_PATH_HIBERNATE_OPERATOR constant [121](https://github.com/starnowski/posjsonhelper/issues/121)
  - Added com.github.starnowski.posjsonhelper.core.HibernateContext.deleteJsonBySpecificPathOperator property and its setter and getter [121](https://github.com/starnowski/posjsonhelper/issues/121)
  - Added com.github.starnowski.posjsonhelper.hibernate6.descriptor.DeleteJsonbBySpecifiedPathOperatorDescriptor type [121](https://github.com/starnowski/posjsonhelper/issues/121)
  - Added com.github.starnowski.posjsonhelper.hibernate6.descriptor.DeleteJsonbBySpecifiedPathOperatorDescriptorRegister type [121](https://github.com/starnowski/posjsonhelper/issues/121)
  - Added com.github.starnowski.posjsonhelper.hibernate6.descriptor.DeleteJsonbBySpecifiedPathOperatorDescriptorRegisterFactory type [121](https://github.com/starnowski/posjsonhelper/issues/121)
  - Added com.github.starnowski.posjsonhelper.hibernate6.operators.DeleteJsonbBySpecifiedPathOperator type [121](https://github.com/starnowski/posjsonhelper/issues/121)

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
