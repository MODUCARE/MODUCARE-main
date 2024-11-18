FROM openjdk:17-jdk-slim

ARG JAR_FILE=build/libs/moducare-0.0.1-SNAPSHOT.jar
RUN apt-get update && apt-get install -y iputils-ping && apt-get clean && rm -rf /var/lib/apt/lists/*
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
