# Build stage: Use a pre-built Maven image with JDK 21
# (JDK 21 can compile Java 22 code perfectly fine)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage: Use JDK 22 to run the application
FROM eclipse-temurin:22-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
