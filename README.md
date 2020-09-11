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

## Pre Requisites
### Updating NPM on Windows
To update npm on windows, install newer nodejs. Or:
- `npm install npm@latest -g` 
- `npm list -g` 
- `npm update` 
### Udating NPM on Ubuntu 18 LTS (https://www.digitalocean.com/community/tutorials/how-to-install-node-js-on-ubuntu-18-04)
- `cd ~` 
- `curl -sL https://deb.nodesource.com/setup_10.x -o nodesource_setup.sh` 
- `sudo bash nodesource_setup.sh` 
- `sudo apt install nodejs` 

## Instructions
- First clone the repo: `git clone https://github.com/P1100/VirtualLabs.git` 
- Do the steps below, and then open the app at the url https://localhost:4200/ (accept the certificate, it is a mock)

### DB:
-  User the following command in the terminal:
`docker run -d -v /home/myes2mariadb:/var/lib/mysql -p 3306:3306 --name virtuallabs -e MYSQL_ROOT_PASSWORD=root -d mariadb`

### Server: 
- Build and run from IDEA, then while running do maven package (tested on Windows10 with docker toolbox, and on ubuntu)
`docker build -t vl-server .`
`docker run -it --net=host --name vl-app-server-container  -p 8080:8080 vl-server` 
- To attach: 
`docker attach <container_id>`
`docker exec -it [container-id] sh`

### Client:
- First update your node and npm installations to last version (important!)
- Then start the frontend using `npm start` in its folder (after doing `npm install`)


## ER Model
![](https://i.ibb.co/g4CgcfQ/ERModel.jpg)

## Questions and Issues
Feel free to ask or report any

## License

[MIT](http://opensource.org/licenses/MIT)

Copyright (c) 2020-present, P1100
