INSERT INTO students (record_book, faculty, last_name, first_name)
VALUES ('RB10001', 'ФКТИ', 'Иванов', 'Иван'),
       ('RB10002', 'ФКТИ', 'Петров', 'Пётр'),
       ('RB10003', 'ФКТИ', 'Сидоров', 'Сидор')
ON CONFLICT (record_book) DO NOTHING;
SELECT setval('student_id_seq', COALESCE((SELECT MAX(id) FROM students), 0) + 1, false);
