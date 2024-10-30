ALTER TABLE offers DROP CONSTRAINT offers_client_id_fkey;
ALTER TABLE offers DROP CONSTRAINT offers_real_state_id_fkey;
ALTER TABLE offers DROP CONSTRAINT offers_realtor_id_fkey;

ALTER TABLE offers ADD CONSTRAINT offers_client_id_fkey FOREIGN KEY (client_id) REFERENCES clients (user_id) ON DELETE RESTRICT;
ALTER TABLE offers ADD CONSTRAINT offers_real_state_id_fkey FOREIGN KEY (real_state_id) REFERENCES real_states (id) ON DELETE RESTRICT;
ALTER TABLE offers ADD CONSTRAINT offers_realtor_id_fkey FOREIGN KEY (realtor_id) REFERENCES realtors (user_id) ON DELETE RESTRICT;



ALTER TABLE demands DROP CONSTRAINT demands_client_id_fkey;
ALTER TABLE demands DROP CONSTRAINT demands_realtor_id_fkey;

ALTER TABLE demands ADD CONSTRAINT demands_client_id_fkey FOREIGN KEY (client_id) REFERENCES clients (user_id) ON DELETE RESTRICT;
ALTER TABLE demands ADD CONSTRAINT demands_realtor_id_fkey FOREIGN KEY (realtor_id) REFERENCES realtors (user_id) ON DELETE RESTRICT;

