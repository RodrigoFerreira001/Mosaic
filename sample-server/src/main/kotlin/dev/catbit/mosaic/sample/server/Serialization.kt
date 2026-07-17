package dev.catbit.mosaic.sample.server

import dev.catbit.mosaic.core.serialization.MosaicSerializer
import dev.catbit.mosaic.sample.core.schemas.tiles.code.CodeViewerTileSchema
import dev.catbit.mosaic.sample.core.schemas.tiles.navigation.AdaptiveNavigationTileSchema
import dev.catbit.mosaic.sample.core.schemas.triggers.OnAdaptiveNavigationItemClickEventTrigger
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(
            MosaicSerializer(
                tileSerializers = mapOf(
                    AdaptiveNavigationTileSchema::class to AdaptiveNavigationTileSchema.serializer(),
                    CodeViewerTileSchema::class to CodeViewerTileSchema.serializer(),
                ),
                eventTriggerSerializers = mapOf(
                    OnAdaptiveNavigationItemClickEventTrigger::class to OnAdaptiveNavigationItemClickEventTrigger.serializer()
                )
            ).json
        )
    }
}
