CREATE TABLE IF NOT EXISTS events
(
    id       uuid primary key,
    start_at timestamp         not null,
    type     integer default 0 not null,
    end_at   timestamp,
    name     varchar(100),
    comment  text
)