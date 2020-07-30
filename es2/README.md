# AI 2020 Progetto parte server side

# How to delete DB rows
mysql> SET foreign_key_checks = 0;
mysql> DROP table ...;
mysql> SET foreign_key_checks = 1;
GET http://localhost:8080/users/add/user/pass/
# Docker
## Old
`docker run -d --mount source=es2vol,target=/es2app -p 3306:3306 --name es2 -e MYSQL_ROOT_PASSWORD=root -d mariadb`
## Current
`docker run -d -v /home/myes2mariadb:/var/lib/mysql -p 3306:3306 --name es2 -e MYSQL_ROOT_PASSWORD=root -d mariadb`
### Docker Toolbox Windows -> problema, toolbox non scrive dati su volume directory (dont use rm! Kills container when stopped)
`docker run -d --rm -v "/c/Users/dockertbx:/var/mariadb" -p 3306:3306 --name es2 -e MYSQL_ROOT_PASSWORD=root -d mariadb`
`docker run -d --rm -v "//c/Users/dockertbx:/var/mariadb" -p 3306:3306 --name es2 -e MYSQL_ROOT_PASSWORD=root -d mariadb`
#### Toolbox: Se errore Protocol Error cant create dir (last used)
`docker run -d -v "//v/dockertbx:/var/mariadb" -p 3306:3306 --name es2 -e MYSQL_ROOT_PASSWORD=root -d mariadb`

# SQL
`CREATE DATABASE teams;`
--> For mariaDb, use data dump sql script

Select *
FROM team t JOIN course c on t.course_id = c.name NATURAL JOIN teams_students ts JOIN student st ON ts.student_id = st.id ;

# SQL VIEW
create definer = root@`%` view MyTeamSummary as
select `c`.`idname` AS `corso`, `t`.`name` AS `team`, `st`.`id` AS `studente`, tk.id
from (((`teams`.`team` `t` join `teams`.`course` `c` on (`t`.`course_id` = `c`.`idname`)) join `teams`.`teams_students` `ts` on (`t`.`id` = `ts`.`team_id`))
         join `teams`.`student` `st` on (`ts`.`student_id` = `st`.`id`)) left join token tk on (tk.student_id=st.id AND tk.team_id=t.id)
order by `c`.`idname`, `t`.`name`, `st`.`id`;

