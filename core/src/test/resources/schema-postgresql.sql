
--
CREATE TABLE IF NOT EXISTS public.item
(
    id bigint NOT NULL,
    jsonb_content jsonb,
    CONSTRAINT item_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.item
OWNER to "posjsonhelper-owner";

--
CREATE TABLE IF NOT EXISTS non_public_schema.item
(
    id bigint NOT NULL,
    jsonb_content jsonb,
    CONSTRAINT item_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS non_public_schema.item
OWNER to "posjsonhelper-owner";