FROM mariadb:latest
#FROM openjdk:8-jre-alpine

EXPOSE 3307
#EXPOSE 8080

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar
#COPY target/docker1.jar app.jar

RUN addgroup -S app && adduser -S app -G app
USER app

ENTRYPOINT ["java","-jar","/app.jar"]

# PROD
#FROM ubuntu-jdk
#
#MAINTAINER 51178702+P1100@users.noreply.github.com
#
#ENV version=aws-db-usage
#
#ENV dbuser=postgres
#ENV dbpass=password
#ENV jdbcurl=jdbc:postgresql://pmadatabaseaws.cngquqqjuc8v.us-west-1.rds.amazonaws.com:5432/postgres
#
#WORKDIR /usr/local/bin
#
#ADD target/a-app.jar .
#
#ENTRYPOINT ["java", "-jar", "pma-app.jar"]
