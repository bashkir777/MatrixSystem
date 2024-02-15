FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package
FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/matrix-system.jar .
CMD ["java", "-jar", "matrix-system.jar"]
