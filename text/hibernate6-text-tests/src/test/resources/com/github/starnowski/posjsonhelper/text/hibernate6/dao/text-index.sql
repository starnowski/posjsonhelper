
CREATE INDEX IF NOT EXISTS pgweb_eng_idx ON tweet_with_locale USING GIN (to_tsvector('english', title || ' ' || short_content)) WHERE locale = 'english';
CREATE INDEX IF NOT EXISTS pgweb_pol_idx ON tweet_with_locale USING GIN (to_tsvector('pl_ispell', title || ' ' || short_content)) WHERE locale = 'polish';
