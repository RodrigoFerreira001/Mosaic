package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
import dev.catbit.mosaic.server.builder.data.singleAccessMode
import dev.catbit.mosaic.server.builder.data.tile
import dev.catbit.mosaic.server.builder.event.builders.data.CheckForReceivedData
import dev.catbit.mosaic.server.builder.event.builders.data.GetData
import dev.catbit.mosaic.server.builder.event.builders.data.SendData
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.mappedIncomingTileUpdateData
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.inputs.TextField
import dev.catbit.mosaic.server.builder.tile.builders.inputs.outlinedTextField
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object SendDataEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "SendData"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Posts a value onto the DataMailer — an in-memory bus, global to the app — " +
                    "under a dataKey, to be read later from any screen via CheckForReceivedData. Use it to " +
                    "pass data between screens during navigation: the source screen posts the value before " +
                    "navigating; the destination screen polls with CheckForReceivedData, typically in " +
                    "onDisplay(). The DataMailer isn't persistent — it's gone when the process ends — and " +
                    "each key only holds the last value posted."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Send a value, then check whether it arrived") {
                Column(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeVerticallySpacedBy(12)
                ) {
                    TextField(
                        id = "sd_value",
                        kind = outlinedTextField(),
                        label = "Value to send",
                        placeholder = "e.g. hello-mosaic",
                        style = { size(width = fillHorizontally(), height = wrapVertically()) }
                    )
                    Row(arrangement = arrangeHorizontallySpacedBy(8)) {
                        Button(
                            text = "Send",
                            events = {
                                GetData(
                                    trigger = EventTriggers.onClick(),
                                    readings = {
                                        reading(
                                            dataSource = tile("sd_value", "value"),
                                            accessMode = singleAccessMode("value")
                                        )
                                    },
                                    events = {
                                        SendData(
                                            trigger = EventTriggers.onSuccess(),
                                            dataKey = "showroom_mailbox",
                                            events = {
                                                UpdateTiles(
                                                    trigger = EventTriggers.onSuccess(),
                                                    updates = {
                                                        update(
                                                            tileId = "sd_status",
                                                            updateData = inlineTileUpdateData("text" to "Sent to the DataMailer!")
                                                        )
                                                    }
                                                )
                                            }
                                        )
                                    }
                                )
                            }
                        )
                        Button(
                            text = "Check received",
                            buttonType = outlinedButton(),
                            events = {
                                CheckForReceivedData(
                                    trigger = EventTriggers.onClick(),
                                    dataKey = "showroom_mailbox",
                                    events = {
                                        UpdateTiles(
                                            trigger = EventTriggers.onSuccess(),
                                            updates = {
                                                update(
                                                    tileId = "sd_status",
                                                    updateData = mappedIncomingTileUpdateData("text" to "Received: <//>")
                                                )
                                            }
                                        )
                                        UpdateTiles(
                                            trigger = EventTriggers.onFailure(),
                                            updates = {
                                                update(
                                                    tileId = "sd_status",
                                                    updateData = inlineTileUpdateData("text" to "Nothing received yet.")
                                                )
                                            }
                                        )
                                    }
                                )
                            }
                        )
                    }
                    SimpleText(id = "sd_status", text = "Nothing sent yet.")
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                SendData(
                    trigger = EventTriggers.onClick(),
                    dataKey = "selected_environment",
                    data = environment.id
                )
                """
            )
            ShowroomNote(
                "If data is null and there's no incomingData, the event fires onFailure() without posting " +
                    "anything. Posting again to the same dataKey overwrites the previous value."
            )

            ShowroomRelated(
                names = listOf("CheckForReceivedData", "GetData", "UpdateData"),
                destination = "eventDetails"
            )
        }
    }
}
