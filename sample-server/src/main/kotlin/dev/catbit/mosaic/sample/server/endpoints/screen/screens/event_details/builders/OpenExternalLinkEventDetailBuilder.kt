package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.system.OpenExternalLink
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button

object OpenExternalLinkEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "OpenExternalLink"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        Button(
            text = "Open Google",
            events = {
                OpenExternalLink(
                    trigger = EventTriggers.onClick(),
                    url = "https://www.google.com"
                )
            }
        )
    }
}
