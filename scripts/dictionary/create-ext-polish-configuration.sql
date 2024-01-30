CREATE TEXT SEARCH CONFIGURATION pl_ex_ispell(parser = default);

ALTER TEXT SEARCH CONFIGURATION pl_ex_ispell
  ALTER MAPPING FOR asciiword, asciihword, hword_asciipart, word, hword, hword_part
  WITH pl_ispell;

ALTER TEXT SEARCH CONFIGURATION pl_ex_ispell
   ALTER MAPPING FOR numword
   WITH simple;