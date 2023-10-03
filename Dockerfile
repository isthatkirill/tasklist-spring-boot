FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /
COPY /src /src
COPY pom.xml /
ENV MAVEN_OPTS="-DskipTests=true"
RUN mvn -f /pom.xml clean package

FROM openjdk:19-jdk-slim
WORKDIR /
COPY /src /src
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]