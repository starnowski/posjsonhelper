package com.github.starnowski.posjsonhelper.hibernate6.demo.dao;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("different-schema")
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.datasource.url= jdbc:postgresql://localhost:5432/posjsonhelper_db?currentSchema=non_public_schema"})
public class ItemDaoWithDifferentSchemaTest extends AbstractItemDaoTest {
}
