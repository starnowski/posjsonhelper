CREATE TEXT SEARCH CONFIGURATION pl_ispell(parser = default);

ALTER TEXT SEARCH CONFIGURATION pl_ispell
  ALTER MAPPING FOR asciiword, asciihword, hword_asciipart, word, hword, hword_part
  WITH pl_ispell;