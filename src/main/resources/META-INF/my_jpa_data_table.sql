-- Create table
BEGIN;
CREATE TABLE IF NOT EXISTS my_jpa_data_table
(
    id          BIGSERIAL PRIMARY KEY,
    nme         VARCHAR(20) UNIQUE NOT NULL,
    description VARCHAR(35)        NOT NULL
);
COMMIT;

-- Add some records
BEGIN;
INSERT INTO my_jpa_data_table (nme, description)
VALUES ('record1', 'first db record');
INSERT INTO my_jpa_data_table (nme, description)
VALUES ('record2', 'second db record');
INSERT INTO my_jpa_data_table (nme, description)
VALUES ('record3', 'third db record');
COMMIT;

-- Never delete from this table 
BEGIN;
CREATE
    RULE my_jpa_data_table_protect AS ON DELETE
    TO my_jpa_data_table DO INSTEAD NOTHING;
COMMIT;