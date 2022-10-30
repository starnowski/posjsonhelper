CREATE DATABASE posjsonhelper_db WITH
  OWNER = "posjsonhelper-owner"
ENCODING = 'UTF8'
CONNECTION LIMIT = -1;

GRANT ALL PRIVILEGES ON DATABASE posjsonhelper_db to "posjsonhelper-owner" ;

\c posjsonhelper_db;
-- The separate database schema, used for the test cases where there are no default privileges set, just like in case of 'public' schema.
CREATE SCHEMA non_public_schema AUTHORIZATION "posjsonhelper-owner";