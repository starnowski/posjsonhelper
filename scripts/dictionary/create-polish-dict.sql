CREATE TEXT SEARCH DICTIONARY pl_ispell (
  Template = ispell,
  DictFile = polish,
  AffFile = polish,
  StopWords = polish
);

CREATE TEXT SEARCH CONFIGURATION pl_ispell(parser = default);

ALTER TEXT SEARCH CONFIGURATION pl_ispell
  ALTER MAPPING FOR asciiword, asciihword, hword_asciipart, word, hword, hword_part
  WITH pl_ispell;