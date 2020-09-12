<h1 align="center">Virtual Labs    <br/>
    <br/>
    <img alt="Virtuallbs" src="https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRMtrxkSwGoK3DuGR-aXg5dilh_TA1_LCYEpw&usqp=CAU" height="200px" />

</h1>
<p align="center">
 <img alt="Languages" src="https://img.shields.io/badge/Languages-Java | Typescript | HTML,CSS,JS-orange"/>
 <img alt="Framework" src="https://img.shields.io/badge/Framework-Spring | Angular-green"/>
</p>

This is a didactical learning project, made for the Internet Applications course (2020). It is a web app for handling university courses, teams, students and all their assignments/laboratories. Is it kept up to date

## Technologies
- [Angular: **10.0.14**](https://github.com/angular/angular)
- [Java 11 LTS](https://docs.oracle.com/en/java/javase/11/index.html)
- [Spring Boot: 2.3.3.RELEASE](https://github.com/spring-projects/spring-boot/releases/tag/v2.3.3.RELEASE)
  -  [Spring Data JPA](http://projects.spring.io/spring-data-jpa/)
    -    [Hibernate](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/orm.html#orm-hibernate)
  -  [Spring Security](http://projects.spring.io/spring-security/)
- [MariaDb](https://github.com/MariaDB/server)
- [Docker](https://docs.docker.com/install/)
- IDE: IDEA, Webstorm

## ER Model
![](https://i.ibb.co/g4CgcfQ/ERModel.jpg)

## Screenshots
![](https://i.ibb.co/5jZNyXX/Image-131.jpg)
![](https://i.ibb.co/zx12tjV/Image-133.jpg)
![](https://i.ibb.co/BqVyfjR/Image-129.jpg)
![](https://i.ibb.co/9wnGKNv/Image-130.jpg)
## How to build, deploy and run
- First clone the repo:  
`git clone https://github.com/P1100/VirtualLabs.git` 
- Do the steps below
- Open the app at https://localhost:4200/ (accept the certificate, it's a mock)

### DB:
-  User the following command in the terminal:   
 `docker run -d -v /home/myes2mariadb:/var/lib/mysql -p 3306:3306 --name virtuallabs -e MYSQL_ROOT_PASSWORD=root -d mariadb`
#### DB Data Init (optional)
```
docker run -d -v /home/myes2mariadb:/var/lib/mysql -p 3306:3306 --name virtuallabs -e MYSQL_ROOT_PASSWORD=root -d mariadb
docker ps -a
cp ./src/main/resources/dumbMariaDb.sql dbdata.sql
docker cp ./src/main/resources/dumbMariaDb.sql 1a00bbb17c48:/dbdata.sql
**RUN SERVER JAR NOW**  (to init db schema)  
docker exec -i e881856b55ba mysql -uroot -proot virtuallabs < dbdata.sql
```  
### Server: 
- Import project in IDEA, then set up JDK to 11 in project structure
- Make sure profile is set to prod, in application properties
- Build and run main class from IDEA, then while it is running do maven package  
`docker build -t vl-server .`  
`docker run -it --net=host --name vl-app-server-container  -p 8080:8080 vl-server`   
- To attach:  
`docker attach <container_id>`  
`docker exec -it [container-id] sh`  
### Client:
- First update all your node and npm installations to last versions (important! Check below)
- Then install the frontend using `npm install` in its folder, and run it with `npm start`

## Other info
#### DB Debug
```
docker exec -it e881856b55ba mysql -uroot -proot virtuallabs
use virtuallabs;
show tables;
SELECT * FROM course;
DROP DATABASE virtuallabs;
```
#### DB Pre-Loaded Users and Passwords
- 111111 111111
- 222222 222222
- 333333 333333
- 999999 999999
#### Updating NPM on Windows
To update npm on windows, install newer nodejs. Or:  
- `npm install npm@latest -g` 
- `npm list -g` 
- `npm update` 
#### Udating NPM on Ubuntu 18 LTS
First update your ubuntu, everything. Then:  
- `cd ~` 
- `curl -sL https://deb.nodesource.com/setup_10.x -o nodesource_setup.sh` 
- `sudo bash nodesource_setup.sh` 
- `sudo apt install nodejs` 

## Questions and Issues
Feel free to ask or report any

## License

[MIT](http://opensource.org/licenses/MIT)

Copyright (c) 2020-present, P1100
