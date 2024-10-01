FROM bellsoft/liberica-openjdk-alpine-musl:17

COPY target/car-rest-service-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/db/migration/file.csv /src/main/resources/db/migration/file.csv

ENTRYPOINT ["java", "-jar", "/wapp.jar"]
