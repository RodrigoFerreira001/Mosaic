# Build from the repo root: docker build -f sample-server/Dockerfile .
# sample-server depends on sibling Gradle modules (mosaic-core, mosaic-server, sample-core), so the
# whole repo is needed as build context — not just this directory.

FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

COPY . .
RUN ./gradlew :sample-server:buildFatJar --no-daemon

FROM eclipse-temurin:11-jre AS runtime
WORKDIR /app

COPY --from=build /workspace/sample-server/build/libs/*-all.jar app.jar

# Cloud Run injects PORT; Application.kt falls back to 9090 for local runs.
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
