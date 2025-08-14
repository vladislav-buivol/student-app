CREATE SEQUENCE IF NOT EXISTS student_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS students
(
    id          BIGINT PRIMARY KEY DEFAULT nextval('student_id_seq'),
    record_book VARCHAR(32) NOT NULL UNIQUE,
    faculty     VARCHAR(64) NOT NULL,
    first_name  VARCHAR(64) NOT NULL,
    last_name   VARCHAR(64) NOT NULL,
    created_at  TIMESTAMP          DEFAULT CURRENT_TIMESTAMP
);
