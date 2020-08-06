INSERT INTO users (username, password, enabled)
values ('user',
        '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a',
        true);

INSERT INTO authorities (username, authority)
values ('user', 'ROLE_USER');

INSERT INTO virtuallabs.student (id, first_name, last_name, profile_photo, email)
VALUES ('1', 'Pietro', 'Giasone', null, 'pg@polito.it');
INSERT INTO virtuallabs.student (id, first_name, last_name, profile_photo, email)
VALUES ('10', 'Luca', 'Manni', null, 'lm@polito.it');
INSERT INTO virtuallabs.student (id, first_name, last_name, profile_photo, email)
VALUES ('11', 'Valentina', 'Guano', null, 'vg@polito.it');
INSERT INTO virtuallabs.student (id, first_name, last_name, profile_photo, email)
VALUES ('2', 'Paolo', 'Ferri', null, 'pf@polito.it');
INSERT INTO virtuallabs.student (id, first_name, last_name, profile_photo, email)
VALUES ('3', 'Gennaro', 'Esposito', null, 'ge@polito.it');
INSERT INTO virtuallabs.student (id, first_name, last_name, profile_photo, email)
VALUES ('4', 'Alberto', 'Muti', null, 'am@polito.it');
INSERT INTO virtuallabs.student (id, first_name, last_name, profile_photo, email)
VALUES ('5', 'Gino', 'Rossi', null, 'gr@polito.it');
INSERT INTO virtuallabs.student (id, first_name, last_name, profile_photo, email)
VALUES ('6', 'Giovanni', 'Bianchi', null, 'gb@polito.it');
INSERT INTO virtuallabs.student (id, first_name, last_name, profile_photo, email)
VALUES ('7', 'Tizio', 'Tizio', null, 'tt@polito.it');
INSERT INTO virtuallabs.student (id, first_name, last_name, profile_photo, email)
VALUES ('8', 'Caio', 'Caio', null, 'cc@polito.it');
INSERT INTO virtuallabs.student (id, first_name, last_name, profile_photo, email)
VALUES ('9', 'Alberto', 'Terri', null, 'at@polito.it');

INSERT INTO virtuallabs.course (id, enabled, full_name, max_enrolled, min_enrolled)
VALUES ('1', true, 'Applicazioni Internet', 300, 1);
INSERT INTO virtuallabs.course (id, enabled, full_name, max_enrolled, min_enrolled)
VALUES ('2', true, 'Mobile Development', 200, 1);
INSERT INTO virtuallabs.course (id, enabled, full_name, max_enrolled, min_enrolled)
VALUES ('3', true, 'Programmazione di sistema', 10000, 10);
INSERT INTO virtuallabs.course (id, enabled, full_name, max_enrolled, min_enrolled)
VALUES ('4', false, 'Advanced Genetic Coding', 10, 1);

INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('1', '1');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('2', '1');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('2', '2');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('3', '2');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('4', '1');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('4', '2');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('5', '1');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('5', '2');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('5', '3');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('5', '4');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('6', '1');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('7', '1');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('8', '1');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('9', '1');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('10', '1');

INSERT INTO virtuallabs.teacher (id, first_name, last_name, profile_photo, email)
VALUES ('1', 'G', 'Malnati', null, 'gm@polito.it');
INSERT INTO virtuallabs.teacher (id, first_name, last_name, profile_photo, email)
VALUES ('2', 'A', 'Servetti', null, 'as@polito.it');
INSERT INTO virtuallabs.teacher (id, first_name, last_name, profile_photo, email)
VALUES ('3', 'U', 'Unknown', null, 'uu@polito.it');

INSERT INTO virtuallabs.teacher_course (teacher_id, course_id)
VALUES ('1', '1');
INSERT INTO virtuallabs.teacher_course (teacher_id, course_id)
VALUES ('1', '2');
INSERT INTO virtuallabs.teacher_course (teacher_id, course_id)
VALUES ('2', '1');
INSERT INTO virtuallabs.teacher_course (teacher_id, course_id)
VALUES ('3', '4');
