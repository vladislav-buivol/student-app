INSERT INTO students (record_book, faculty, first_name, last_name)
VALUES ('RB2025001', 'Computer Science', 'Alice', 'Johnson'),
       ('RB2025002', 'Mathematics', 'Bob', 'Smith'),
       ('RB2025003', 'Physics', 'Carol', 'Williams'),
       ('RB2025004', 'Chemistry', 'David', 'Brown'),
       ('RB2025005', 'Biology', 'Eva', 'Davis'),
       ('RB2025006', 'Economics', 'Frank', 'Miller'),
       ('RB2025007', 'History', 'Grace', 'Wilson'),
       ('RB2025008', 'Literature', 'Hank', 'Moore'),
       ('RB2025009', 'Engineering', 'Ivy', 'Taylor'),
       ('RB2025010', 'Philosophy', 'Jack', 'Anderson')
ON CONFLICT (record_book) DO NOTHING;
SELECT setval('student_id_seq', COALESCE((SELECT MAX(id) FROM students), 0) + 1, false);