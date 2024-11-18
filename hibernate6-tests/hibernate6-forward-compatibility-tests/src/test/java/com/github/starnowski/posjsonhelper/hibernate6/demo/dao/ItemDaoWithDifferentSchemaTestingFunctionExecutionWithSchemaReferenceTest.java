package com.github.starnowski.posjsonhelper.hibernate6.demo.dao;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("different-schema-testing-function-execution-with-schema-reference")
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.datasource.url= jdbc:postgresql://localhost:${posjsonhelper.test.database.port:5432}/posjsonhelper_db"})
public class ItemDaoWithDifferentSchemaTestingFunctionExecutionWithSchemaReferenceTest extends AbstractItemDaoTest {
}
