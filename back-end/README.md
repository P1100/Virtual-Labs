# AI 2020 VirtualLabs Project - Back end

## Git tags push
git push origin :refs/tags/`tagname`

## Update Maven Dependencies
- IDEA Maven Run Configuration:<br>
`mvn versions:display-dependency-updates`
`mvn versions:use-latest-versions`
- Also update Maven indexed repositories on IDEA

### Others Maven Goals
mvn dependency:list 
mvn dependency:tree
mvn help:effective-pom -Dverbose

## Docker
`docker run -d -v /home/myes2mariadb:/var/lib/mysql -p 3306:3306 --name virtuallabs -e MYSQL_ROOT_PASSWORD=root -d mariadb`

## SQL
### How to manually delete DB rows
mysql> SET foreign_key_checks = 0;
mysql> DROP table ...;
mysql> SET foreign_key_checks = 1;
### DB Rebuild
DROP SCHEMA virtuallabs;
CREATE DATABASE virtuallabs;
### Query (?)
Select *
FROM team t JOIN course c on t.course_id = c.name NATURAL JOIN teams_students ts JOIN student st ON ts.student_id = st.id ;
## SQL VIEW
create definer = root@`%` view MyTeamSummary as
select `c`.`idname` AS `corso`, `t`.`name` AS `team`, `st`.`id` AS `studente`, tk.id
from (((`teams`.`team` `t` join `teams`.`course` `c` on (`t`.`course_id` = `c`.`idname`)) join `teams`.`teams_students` `ts` on (`t`.`id` = `ts`.`team_id`))
         join `teams`.`student` `st` on (`ts`.`student_id` = `st`.`id`)) left join token tk on (tk.student_id=st.id AND tk.team_id=t.id)
order by `c`.`idname`, `t`.`name`, `st`.`id`;

## SQL Scratches
DROP DATABASE virtuallabs;
CREATE DATABASE virtuallabs;
INSERT INTO virtuallabs.role (name, description) VALUES ('ROLE_ADMIN', 'Administrator');
INSERT INTO virtuallabs.role (name, description) VALUES ('ROLE_PROFESSOR', 'Course Professor');
INSERT INTO virtuallabs.role (name, description) VALUES ('ROLE_STUDENT', 'The Student');
