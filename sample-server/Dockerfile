# Build from the repo root: docker build -f sample-server/Dockerfile .
# sample-server depends on sibling Gradle modules (mosaic-core, mosaic-server, sample-core), so the
# whole repo is needed as build context — not just this directory.

FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

COPY . .
RUN ./gradlew :sample-server:buildFatJar --no-daemon -Pmosaic.environment=remote

# mosaic-core's jvm target compiles at JvmTarget.JVM_21 (its android target stays at 11) — the
# runtime image has to match that bytecode version. sample-server's own jvmToolchain is pinned to
# 21 too now, for the same reason, so local `./gradlew :sample-server:run` matches this image.
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

COPY --from=build /workspace/sample-server/build/libs/*-all.jar app.jar

# Cloud Run injects PORT; Application.kt falls back to 9090 for local runs.
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
