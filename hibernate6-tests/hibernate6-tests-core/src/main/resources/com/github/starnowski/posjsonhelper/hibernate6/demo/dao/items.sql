
INSERT INTO item (id, jsonb_content) VALUES (1, '{"top_element_with_set_of_values":["TAG1","TAG2","TAG11","TAG12","TAG21","TAG22"]}');
INSERT INTO item (id, jsonb_content) VALUES (2, '{"top_element_with_set_of_values":["TAG3"]}');
INSERT INTO item (id, jsonb_content) VALUES (3, '{"top_element_with_set_of_values":["TAG1","TAG3"]}');
INSERT INTO item (id, jsonb_content) VALUES (4, '{"top_element_with_set_of_values":["TAG22","TAG21"]}');
INSERT INTO item (id, jsonb_content) VALUES (5, '{"top_element_with_set_of_values":["TAG31","TAG32"]}');

-- item without any properties, just an empty json
INSERT INTO item (id, jsonb_content) VALUES (6, '{}');

-- int values
INSERT INTO item (id, jsonb_content) VALUES (7, '{"integer_value": 132}');
INSERT INTO item (id, jsonb_content) VALUES (8, '{"integer_value": 562}');
INSERT INTO item (id, jsonb_content) VALUES (9, '{"integer_value": 1322}');

-- double values
INSERT INTO item (id, jsonb_content) VALUES (10, '{"double_value": 353.01}');
INSERT INTO item (id, jsonb_content) VALUES (11, '{"double_value": -1137.98}');
INSERT INTO item (id, jsonb_content) VALUES (12, '{"double_value": 20490.04}');

-- enum values
INSERT INTO item (id, jsonb_content) VALUES (13, '{"enum_value": "SUPER"}');
INSERT INTO item (id, jsonb_content) VALUES (14, '{"enum_value": "USER"}');
INSERT INTO item (id, jsonb_content) VALUES (15, '{"enum_value": "ANONYMOUS"}');

-- string values
INSERT INTO item (id, jsonb_content) VALUES (16, '{"string_value": "this is full sentence"}');
INSERT INTO item (id, jsonb_content) VALUES (17, '{"string_value": "this is part of sentence"}');
INSERT INTO item (id, jsonb_content) VALUES (18, '{"string_value": "the end of records"}');

-- inner elements
INSERT INTO item (id, jsonb_content) VALUES (19, '{"child": {"pets" : ["dog"]}}');
INSERT INTO item (id, jsonb_content) VALUES (20, '{"child": {"pets" : ["cat"]}}');
INSERT INTO item (id, jsonb_content) VALUES (21, '{"child": {"pets" : ["dog", "cat"]}}');
INSERT INTO item (id, jsonb_content) VALUES (22, '{"child": {"pets" : ["hamster"]}}');
INSERT INTO item (id, jsonb_content) VALUES (23, '{"child": {"pets" : ["dog"]}, "inventory": ["mask", "fins"], "nicknames": {"school": "bambo", "childhood": "bob"} }');
INSERT INTO item (id, jsonb_content) VALUES (24, '{"child": {"pets" : ["crab", "chameleon"]}, "inventory": ["mask", "fins", "compass"]}');