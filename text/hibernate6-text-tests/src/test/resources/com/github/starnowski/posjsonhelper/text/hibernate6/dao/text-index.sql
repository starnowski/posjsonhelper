
CREATE INDEX pgweb_eng_idx ON tweet_with_locale USING GIN (to_tsvector('english', title || ' ' || short_content));
CREATE INDEX pgweb_pol_idx ON tweet_with_locale USING GIN (to_tsvector('pl_ispell', title || ' ' || short_content));
