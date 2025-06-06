FROM maven:3.9-amazoncorretto-21-alpine AS build
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests -Dliquibase.skip -Djooq.codegen.skip

FROM build AS test
RUN mvn clean install -Dliquibase.skip -Djooq.codegen.skip

FROM bellsoft/liberica-openjdk-alpine:21 AS execute
WORKDIR /app
COPY --from=build /build/target/*.jar app.jar
RUN apk add --no-cache curl && \
    curl -L https://github.com/async-profiler/async-profiler/releases/download/v3.0/async-profiler-3.0-linux-arm64.tar.gz -o async-profiler.tar.gz && \
    tar -xzf async-profiler.tar.gz && \
    mv async-profiler-3.0-linux-arm64 async-profiler && \
    rm async-profiler.tar.gz && \
    apk del curl

ENTRYPOINT ["java", "-jar", "app.jar"]