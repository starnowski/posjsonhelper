
INSERT INTO item (id, jsonb_content) VALUES (1, '{"top_element_with_set_of_values":["TAG1","TAG2","TAG11","TAG12","TAG21","TAG22"]}');
INSERT INTO item (id, jsonb_content) VALUES (2, '{"top_element_with_set_of_values":["TAG3"]}');
INSERT INTO item (id, jsonb_content) VALUES (3, '{"top_element_with_set_of_values":["TAG1","TAG3"]}');
INSERT INTO item (id, jsonb_content) VALUES (4, '{"top_element_with_set_of_values":["TAG22","TAG21"]}');
INSERT INTO item (id, jsonb_content) VALUES (5, '{"top_element_with_set_of_values":["TAG31","TAG32"]}');

-- item without any properties, just an empty json
INSERT INTO item (id, jsonb_content) VALUES (6, '{}');


INSERT INTO item (id, jsonb_content) VALUES (7, '{"integer_value": 132}');
INSERT INTO item (id, jsonb_content) VALUES (8, '{"integer_value": 562}');
INSERT INTO item (id, jsonb_content) VALUES (9, '{"integer_value": 1322}');
