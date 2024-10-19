CREATE TABLE IF NOT EXISTS users
(
    id   uuid primary key,
    role integer default 0 not null
);

CREATE TABLE IF NOT EXISTS realtors
(
    id          serial primary key,
    user_id     uuid references users on delete cascade unique,
    last_name   varchar(50) not null,
    first_name  varchar(50) not null,
    middle_name varchar(50) not null,
    deal_share  float
);

CREATE TABLE IF NOT EXISTS clients
(
    id          serial primary key,
    user_id     uuid references users on delete cascade unique,
    last_name   varchar(50),
    first_name  varchar(50),
    middle_name varchar(50),
    phone       varchar(50) unique ,
    email       varchar(50) unique
);
