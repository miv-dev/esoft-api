CREATE TABLE IF NOT EXISTS demands
(
    id              uuid primary key,
    client_id       uuid references users on delete cascade,
    realtor_id      uuid references users on delete cascade,
    real_state_type integer default 0 not null,
    min_price       integer,
    max_price       integer,

    min_area        integer,
    max_area        integer,

    min_rooms       integer,
    max_rooms       integer,

    min_floor       integer,
    max_floor       integer,

    min_floors      integer,
    max_floors      integer
);

CREATE TABLE IF NOT EXISTS offers
(
    id            uuid primary key,
    client_id     uuid references users on delete cascade,
    realtor_id    uuid references users on delete cascade,
    real_state_id uuid references real_states on delete cascade,
    price         integer not null
);
