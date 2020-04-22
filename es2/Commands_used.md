# Old
`docker run -d --mount source=es2vol,target=/es2app -p 3306:3306 --name es2 -e MYSQL_ROOT_PASSWORD=root -d mariadb`

# How to delete DB rows
mysql> SET foreign_key_checks = 0;
mysql> DROP table ...;
mysql> SET foreign_key_checks = 1;

# Current
`docker run -d -v /home/myes2mariadb:/var/lib/mysql -p 3306:3306 --name es2 -e MYSQL_ROOT_PASSWORD=root -d mariadb`

`CREATE DATABASE teams;`

