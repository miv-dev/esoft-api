CREATE TABLE IF NOT EXISTS deals
(
    deal_id        uuid primary key,
    offer_id  uuid references offers on delete restrict,
    demand_id uuid references demands on delete restrict
)