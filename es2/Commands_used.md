docker run -d --mount source=es2vol,target=/es2app -p 3306:3306 --name es2 -e MYSQL_ROOT_PASSWORD=root -d mariadb

myes2mariadb

CREATE DATABASE teams;

-v /my/own/datadir:/var/lib/mysql
docker run -d -v /home/myes2mariadb:/var/lib/mysql -p 3306:3306 --name es2 -e MYSQL_ROOT_PASSWORD=root -d mariadb
