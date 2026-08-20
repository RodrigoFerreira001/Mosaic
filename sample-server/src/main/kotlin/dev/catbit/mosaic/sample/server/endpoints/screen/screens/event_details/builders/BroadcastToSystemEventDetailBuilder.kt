package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.system.BroadcastToSystem
import dev.catbit.mosaic.server.builder.event.builders.system.inlineBroadcastData
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.mappedIncomingTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.system.SystemBroadcastListener
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object BroadcastToSystemEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "BroadcastToSystem"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Emits a named broadcast (broadcastId + payload) over the SystemBroadcastChannel, " +
                    "notifying any subscriber in the app — on any screen — with no direct coupling to the event " +
                    "chain that raised it. The real-world use case is cross-screen: one screen emits, other " +
                    "screen(s) listen with onSystemBroadcast(\"id\") or the SystemBroadcastListener tile. This " +
                    "demo emits and listens on the same screen just to prove the mechanism end to end — " +
                    "behavior across different screens is identical."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Button emits BroadcastToSystem; the listener on this same page reacts for real") {
                Button(
                    text = "Emit \"event_showroom_ping\" broadcast",
                    events = {
                        BroadcastToSystem(
                            trigger = EventTriggers.onClick(),
                            broadcastId = "event_showroom_ping",
                            data = inlineBroadcastData("Ping!")
                        )
                    }
                )
                SystemBroadcastListener(
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onSystemBroadcast("event_showroom_ping"),
                            updates = {
                                update(
                                    tileId = "broadcast_to_system_demo_label",
                                    updateData = mappedIncomingTileUpdateData("text" to "Received: <||>")
                                )
                            }
                        )
                    }
                ) {
                    SimpleText(id = "broadcast_to_system_demo_label", text = "Waiting for broadcast...")
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                // Sender (any screen)
                BroadcastToSystem(
                    trigger = EventTriggers.onClick(),
                    broadcastId = "ENVIRONMENT_CHANGE",
                    data = inlineBroadcastData(environment.id)
                )

                // Receiver (any screen/tile)
                UpdateTiles(
                    trigger = EventTriggers.onSystemBroadcast("ENVIRONMENT_CHANGE"),
                    updates = { /* ... */ }
                )
                """
            )

            ShowroomRelated(
                names = listOf("CheckIfHasInternetConnection", "OpenExternalLink"),
                destination = "eventDetails"
            )
        }
    }
}
