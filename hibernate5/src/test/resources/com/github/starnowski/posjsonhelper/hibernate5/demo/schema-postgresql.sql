CREATE OR REPLACE FUNCTION jsonb_all_array_strings_exist(x jsonb, y text[]) RETURNS boolean AS $$
    SELECT x ?& y;
$$ LANGUAGE SQL;

CREATE OR REPLACE FUNCTION jsonb_any_array_strings_exist(x jsonb, y text[]) RETURNS boolean AS $$
SELECT x ?| y;
$$ LANGUAGE SQL;

--CREATE OR REPLACE FUNCTION jsonb_top_level_exist(x jsonb, y text) RETURNS boolean AS $$
--    SELECT x ? y;
--$$ LANGUAGE SQL;
