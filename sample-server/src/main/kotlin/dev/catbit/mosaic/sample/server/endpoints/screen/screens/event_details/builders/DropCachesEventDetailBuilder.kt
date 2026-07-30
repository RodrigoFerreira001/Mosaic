package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

object DropCachesEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "DropCaches"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) = NotImplementedYetDetail(eventName)
}
