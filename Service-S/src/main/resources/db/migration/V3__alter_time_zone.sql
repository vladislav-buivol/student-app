ALTER TABLE students
    ADD COLUMN created_at_new TIMESTAMPTZ;

UPDATE students
SET created_at_new = created_at AT TIME ZONE 'UTC';
ALTER TABLE students
    DROP COLUMN created_at;
ALTER TABLE students
    RENAME COLUMN created_at_new TO created_at;
ALTER TABLE students
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;
