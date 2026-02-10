package dev.catbit.mosaic

import dev.catbit.mosaic.core.serialization.MosaicSerializer
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(MosaicSerializer().json)
    }
}
