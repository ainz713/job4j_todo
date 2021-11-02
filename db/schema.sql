create table items (
                       id serial primary key,
                       description text
);

CREATE TABLE IF NOT EXISTS filters (
                                       id SERIAL PRIMARY KEY,
                                       name VARCHAR(255)
    );

INSERT INTO filters(name)
VALUES ('All'), ('Completed'), ('Not completed');