CREATE OR REPLACE FUNCTION {{function_reference}}(input_json jsonb, values_to_remove jsonb) RETURNS jsonb AS $$
DECLARE
    result jsonb;
BEGIN
    IF jsonb_typeof(values_to_remove) <> 'array' THEN
        RAISE EXCEPTION 'values_to_remove must be a JSON array';
    END IF;

    result := (
        SELECT jsonb_agg(element)
        FROM jsonb_array_elements(input_json) AS element
        WHERE NOT (element IN (SELECT jsonb_array_elements(values_to_remove)))
    );

    RETURN COALESCE(result, '[]'::jsonb);
END;
$$ LANGUAGE plpgsql;