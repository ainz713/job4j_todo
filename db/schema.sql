CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);

create table items (
                       id serial primary key,
                       description text,
                       created TIMESTAMP NOT NULL,
                       done BOOLEAN NOT NULL,
                       user_id INT REFERENCES users(id) NOT NULL
);

CREATE TABLE IF NOT EXISTS filters (
                                       id SERIAL PRIMARY KEY,
                                       name VARCHAR(255)
    );

INSERT INTO filters(name)
VALUES ('All'), ('Completed'), ('Not completed');