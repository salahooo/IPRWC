FROM eclipse-temurin:21 AS build
WORKDIR /app

# Copy all for build
COPY . .

# Ensure maven wrapper is executable (if used)
RUN chmod +x mvnw

# Build
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
