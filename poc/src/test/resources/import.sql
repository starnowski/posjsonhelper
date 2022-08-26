CREATE OR REPLACE FUNCTION jsonb_all_array_strings_exist(x jsonb, y text[]) RETURNS boolean AS $$
    SELECT x ?& y;
$$ LANGUAGE SQL;
