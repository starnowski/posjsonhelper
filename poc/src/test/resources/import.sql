CREATE OR REPLACE FUNCTION jsonb_all_array_strings_exist(jsonb, text[]) RETURNS boolean AS $$
    SELECT $1 ?& $2;
$$ LANGUAGE SQL;
