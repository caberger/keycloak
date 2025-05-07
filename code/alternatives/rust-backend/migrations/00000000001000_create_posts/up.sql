CREATE TABLE post (
  id SERIAL PRIMARY KEY,
  title VARCHAR NOT NULL,
  body TEXT NOT NULL,
  published BOOLEAN NOT NULL DEFAULT FALSE
);

INSERT INTO post (title, body, published) values
    ('my fist post', 'this is my first post', false),
    ('2nd Post', 'a first yet another short post :)', true),
    ('3nd Post', 'and yet another published post :)', true)
