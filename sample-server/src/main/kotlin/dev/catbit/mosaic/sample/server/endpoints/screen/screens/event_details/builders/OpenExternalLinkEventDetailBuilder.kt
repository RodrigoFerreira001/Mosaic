package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.system.OpenExternalLink
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button

object OpenExternalLinkEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "OpenExternalLink"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Opens a URL outside the app — the system browser, or whatever app is registered " +
                    "to handle it — without leaving the Mosaic event chain. This is exactly the mechanism " +
                    "behind the \"Open code reference\" button in this page's own app bar."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Open a real page in the system browser") {
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

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                OpenExternalLink(
                    trigger = EventTriggers.onClick(),
                    url = "https://www.google.com"
                )
                """
            )

            ShowroomRelated(
                names = listOf("Navigate", "SendNetworkRequest"),
                destination = "eventDetails"
            )
        }
    }
}
