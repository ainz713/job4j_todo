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


create table marki (
                        id serial primary key,
                        name varchar(2000)
);

create table models (
                        id serial primary key,
                        name varchar(2000)
);

create table authors (
                       id serial primary key,
                       name varchar(2000)
);

create table books (
                        id serial primary key,
                        name varchar(2000),
);