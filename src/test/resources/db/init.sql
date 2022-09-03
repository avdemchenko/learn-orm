CREATE TABLE persons
(
    id         BIGINT PRIMARY KEY,
    first_name VARCHAR(32),
    last_name  VARCHAR(32)
);

CREATE TABLE notes
(
    id        BIGINT PRIMARY KEY,
    body      VARCHAR(128),
    person_id BIGINT REFERENCES persons
);

INSERT INTO persons(id, first_name, last_name)
VALUES (1, 'John', 'McClane'),
       (2, 'John', 'Rambo');

INSERT INTO notes(id, body, person_id)
VALUES (1, 'Yippee-ki-yay', 1),
       (2, 'Yeah. I got invited to a Christmas party by mistake. Who knew?', 1),
       (3, 'Welcome to the party, pal', 1),
       (4, 'They drew first blood, not me', 2);
