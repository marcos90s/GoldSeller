FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /goldseller
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests
FROM eclipse-temurin:17
WORKDIR /goldseller
COPY --from=build /goldseller/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar", "app.jar"]
