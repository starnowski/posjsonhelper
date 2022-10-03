
\c posjsonhelper_db;
-- The separate database schema, used for the test cases where there are no default privileges set, just like in case of 'public' schema.
CREATE SCHEMA non_public_schema AUTHORIZATION "posjsonhelper-owner";