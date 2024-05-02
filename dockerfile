FROM eclipse-temurin

ARG CONTAINER_NAME=test
ARG CONTAINER_VERSION=1.0

WORKDIR /opt/app
COPY target/practicalForClearSolutions-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java" , "-jar" , "app.jar"]