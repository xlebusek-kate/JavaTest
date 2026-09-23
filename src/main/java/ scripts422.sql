CREATE TABLE people
(
    id         INTEGER PRIMARY KEY,
    name       char(10),
    age        INTEGER,
    permission BOOLEAN DEFAULT FALSE
);

CREATE TABLE car
(
    id    INTEGER primary key,
    name  char(10),
    model CHAR(20),
    cost  INTEGER
);

CREATE TABLE people_car
(
    people_id INTEGER REFERENCES people (id),
    car_id    INTEGER REFERENCES car (id),
)

