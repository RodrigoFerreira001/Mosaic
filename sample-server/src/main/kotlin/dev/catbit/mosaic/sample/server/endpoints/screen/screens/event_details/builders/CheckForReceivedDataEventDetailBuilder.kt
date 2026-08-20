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

object CheckForReceivedDataEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "CheckForReceivedData"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Reads the DataMailer for a value posted under dataKey and forwards it as " +
                    "incomingData to the child events — the read counterpart of SendData. It's typically fired " +
                    "on the destination screen's onDisplay(), to pick up the value the previous screen posted " +
                    "via SendData before navigating. It can also be used on demand — for example, a \"Refresh\" " +
                    "button that polls manually."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Send a value, then check whether it arrived") {
                Column(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeVerticallySpacedBy(12)
                ) {
                    TextField(
                        id = "cfrd_value",
                        kind = outlinedTextField(),
                        label = "Value to send",
                        placeholder = "E.g.: hello-mosaic",
                        style = { size(width = fillHorizontally(), height = wrapVertically()) }
                    )
                    Row(arrangement = arrangeHorizontallySpacedBy(8)) {
                        Button(
                            text = "Send",
                            buttonType = outlinedButton(),
                            events = {
                                GetData(
                                    trigger = EventTriggers.onClick(),
                                    readings = {
                                        reading(
                                            dataSource = tile("cfrd_value", "value"),
                                            accessMode = singleAccessMode("value")
                                        )
                                    },
                                    events = {
                                        SendData(
                                            trigger = EventTriggers.onSuccess(),
                                            dataKey = "showroom_mailbox_cfrd",
                                            events = {
                                                UpdateTiles(
                                                    trigger = EventTriggers.onSuccess(),
                                                    updates = {
                                                        update(
                                                            tileId = "cfrd_status",
                                                            updateData = inlineTileUpdateData("text" to "Sent. Click \"Check\".")
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
                            text = "Check",
                            events = {
                                CheckForReceivedData(
                                    trigger = EventTriggers.onClick(),
                                    dataKey = "showroom_mailbox_cfrd",
                                    events = {
                                        UpdateTiles(
                                            trigger = EventTriggers.onSuccess(),
                                            updates = {
                                                update(
                                                    tileId = "cfrd_status",
                                                    updateData = mappedIncomingTileUpdateData("text" to "Received: <//>")
                                                )
                                            }
                                        )
                                        UpdateTiles(
                                            trigger = EventTriggers.onFailure(),
                                            updates = {
                                                update(
                                                    tileId = "cfrd_status",
                                                    updateData = inlineTileUpdateData("text" to "Nothing received yet.")
                                                )
                                            }
                                        )
                                    }
                                )
                            }
                        )
                    }
                    SimpleText(id = "cfrd_status", text = "Nothing sent yet.")
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                CheckForReceivedData(
                    trigger = EventTriggers.onDisplay(),
                    dataKey = "selected_environment",
                    events = {
                        UpdateTiles(trigger = EventTriggers.onDataReceived(), updates = {
                            update("env_name_label", incomingTileUpdateData())
                        })
                    }
                )
                """
            )

            ShowroomNote(
                "Fires onDataReceived(value) followed by onSuccess(value) when it finds something; " +
                    "onFailure() when nothing has been posted under that dataKey."
            )

            ShowroomRelated(
                names = listOf("SendData", "GetData", "TransformData"),
                destination = "eventDetails"
            )
        }
    }
}
