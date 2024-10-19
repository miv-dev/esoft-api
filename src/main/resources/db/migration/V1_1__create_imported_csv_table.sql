CREATE TABLE IF NOT EXISTS imported_csv
(
    id       serial primary key,
    filename varchar(64) NOT NULL
)