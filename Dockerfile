# Use Maven with OpenJDK 21 for building
FROM maven:3.9.5-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

# Use lightweight JDK image to run the app
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=build /app/target/scm2.0-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
