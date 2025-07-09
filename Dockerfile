FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY target/ems-backend-0.0.1-SNAPSHOT.jar /app/ems-backend-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ems-backend-0.0.1-SNAPSHOT.jar"]



