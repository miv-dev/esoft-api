CREATE TABLE IF NOT EXISTS real_states
(
    id             uuid primary key,
    type           integer default 0 not null,
    latitude       float not null,
    longitude      float not null,
    address_city   varchar(100),
    address_street varchar(100),
    address_house  varchar(100),
    address_number varchar(100)
);


CREATE TABLE IF NOT EXISTS districts
(
    id   serial primary key,
    name varchar(255) not null,
    area text         not null
);


CREATE TABLE IF NOT EXISTS state_district
(
    real_state uuid references real_states on delete cascade,
    district   serial references districts on delete cascade
);


CREATE TABLE IF NOT EXISTS lands
(
    real_state uuid references real_states on delete cascade primary key,
    total_area float
);

CREATE TABLE IF NOT EXISTS apartments
(
    real_state uuid references real_states on delete cascade primary key,
    total_area float,
    rooms      integer,
    floor      integer
);

CREATE TABLE IF NOT EXISTS houses
(
    real_state   uuid references real_states on delete cascade primary key,
    total_area   float,
    total_floors integer
);