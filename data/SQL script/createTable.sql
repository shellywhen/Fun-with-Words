
CREATE TABLE log(
  word TEXT NOT NULL,
  user TEXT NOT NULL,
  date TEXT NOT NULL,
  level TEXT NOT NULL,
  mark TEXT NOT NULL
);
CREATE TABLE user(
  name TEXT NOT NULL,
  password TEXT NOT NULL,
  register_date TEXT NOT NULL
);
INSERT INTO user VALUES(
  "Diana",
  "pkuJAVA",
  "2019-05-26"
);
INSERT INTO log(
  "hello",
  "Diana",
  "2019-05-27",
  "1",
  "1"
);
UPDATE log SET level=2
WHERE name = "Diana" AND word = "hello";
