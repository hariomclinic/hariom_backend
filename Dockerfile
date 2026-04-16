# Build stage
FROM eclipse-temurin:22-jdk-jammy AS build
WORKDIR /app
# Install Maven manually as native Maven images for JDK 22 are sometimes lagging
RUN apt-get update && apt-get install -y maven
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:22-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
