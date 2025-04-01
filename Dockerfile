FROM maven:3.9-amazoncorretto-21-alpine as build
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

FROM bellsoft/liberica-openjdk-alpine:21-aarch64 AS execute
WORKDIR /app
COPY --from=build /build/target/*.jar app.jar
RUN ls
RUN apk update
RUN apk add --no-cache curl

ENTRYPOINT ["java", "-jar", "app.jar"]