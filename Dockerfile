FROM maven:3.9-amazoncorretto-25 AS builder

WORKDIR /usr/src/guest-book
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY . .
RUN mvn package -DskipTests

FROM amazoncorretto:25

WORKDIR /app
COPY target/guest_book-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]