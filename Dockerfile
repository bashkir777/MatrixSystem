FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /system
COPY . .
RUN mvn clean package
FROM openjdk:17
WORKDIR /system
VOLUME ["/system/db"]
VOLUME ["/system/db/images"]
COPY --from=build /system/target/matrix-system.jar .
CMD ["java", "-jar", "matrix-system.jar"]
