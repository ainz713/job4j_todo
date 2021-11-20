create table driver(
    id serial primary key
);

create table engine(
    id serial primary key
);

create table car(
                    id serial primary key,
                    engine_id int not null unique references engine(id)
);

create table history_owner(
                              id serial primary key,
                              driver_id int not null references driver(id),
                              car_id int not null references car(id)
);