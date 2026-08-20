package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.system.BroadcastToSystem
import dev.catbit.mosaic.server.builder.event.builders.system.inlineBroadcastData
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.mappedIncomingTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.system.SystemBroadcastListener
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object SystemBroadcastListenerTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "SystemBroadcastListener"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A transparent container that listens for system broadcasts (emitted by " +
                    "BroadcastToSystem from any screen) and fires onSystemBroadcast. It introduces no visual " +
                    "wrapper — it renders its children directly. The real use case is cross-screen (one screen " +
                    "emits, another listens), but this demo emits and listens on the same screen to show the " +
                    "mechanism working end to end."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "The button emits a BroadcastToSystem; the listener really reacts") {
                Button(
                    text = "Emit \"ping\" broadcast",
                    events = {
                        BroadcastToSystem(
                            trigger = EventTriggers.onClick(),
                            broadcastId = "showroom_ping",
                            data = inlineBroadcastData("Ping!")
                        )
                    }
                )
                SystemBroadcastListener(
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onSystemBroadcast("showroom_ping"),
                            updates = {
                                update(
                                    tileId = "system_broadcast_demo_label",
                                    updateData = mappedIncomingTileUpdateData("text" to "Received: <||>")
                                )
                            }
                        )
                    }
                ) {
                    SimpleText(id = "system_broadcast_demo_label", text = "Waiting for broadcast...")
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                SystemBroadcastListener(
                    id = "session_listener",
                    events = {
                        Navigate(trigger = EventTriggers.onSystemBroadcast("session_expired"), destination = "login", navigatorId = "main")
                    }
                ) {
                    SimpleText(text = "Regular content")
                }
                """
            )

            ShowroomRelated(
                names = listOf("SimpleText", "Column"),
                destination = "tileDetails"
            )
        }
    }
}
