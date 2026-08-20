package dev.catbit.mosaic.sample.server

import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    val port = System.getenv("PORT")?.toIntOrNull() ?: 9090

    embeddedServer(
        factory = Netty,
        port = port,
        host = "0.0.0.0",
        module = Application::module,
        watchPaths = listOf("classes", "resources")
    ).start(wait = true)
}

fun Application.module() {
    configureCORS()
    configureSerialization()
    configureRouting()
}
