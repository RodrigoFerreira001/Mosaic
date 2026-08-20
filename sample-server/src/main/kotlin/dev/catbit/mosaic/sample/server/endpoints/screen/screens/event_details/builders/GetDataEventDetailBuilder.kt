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
import dev.catbit.mosaic.server.builder.data.screenPlainData
import dev.catbit.mosaic.server.builder.data.singleAccessMode
import dev.catbit.mosaic.server.builder.event.builders.data.GetData
import dev.catbit.mosaic.server.builder.event.builders.data.UpdateData
import dev.catbit.mosaic.server.builder.event.builders.data.explicitIncomingUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.mappedIncomingTileUpdateData
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.inputs.TextField
import dev.catbit.mosaic.server.builder.tile.builders.inputs.outlinedTextField
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object GetDataEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "GetData"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Reads one or more values from sources like a tile, a DataHolder (screen memory), " +
                    "or persistent storage, and forwards the aggregated result as incomingData. Use it whenever " +
                    "an event chain needs to read stored state before acting — for example, reading form fields " +
                    "before sending a request, or checking a session token before attaching a header. Multiple " +
                    "readings within the same GetData are merged into a single map; on key collision, the later " +
                    "reading wins."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Type something, save it to the DataHolder, and read it back with GetData") {
                Column(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeVerticallySpacedBy(12)
                ) {
                    TextField(
                        id = "gd_value",
                        kind = outlinedTextField(),
                        label = "Value",
                        placeholder = "Type something...",
                        style = { size(width = fillHorizontally(), height = wrapVertically()) },
                        events = {
                            UpdateData(
                                trigger = EventTriggers.onTextChanged(),
                                updates = {
                                    update(
                                        dataSource = screenPlainData(),
                                        updateData = explicitIncomingUpdateData(dataId = "gd_showroom_value")
                                    )
                                }
                            )
                        }
                    )
                    Row(arrangement = arrangeHorizontallySpacedBy(8)) {
                        Button(
                            text = "Read",
                            events = {
                                GetData(
                                    trigger = EventTriggers.onClick(),
                                    readings = {
                                        reading(
                                            dataSource = screenPlainData(),
                                            accessMode = singleAccessMode("gd_showroom_value")
                                        )
                                    },
                                    events = {
                                        UpdateTiles(
                                            trigger = EventTriggers.onSuccess(),
                                            updates = {
                                                update(
                                                    tileId = "gd_status",
                                                    updateData = mappedIncomingTileUpdateData("text" to "Read from DataHolder: <//>")
                                                )
                                            }
                                        )
                                        UpdateTiles(
                                            trigger = EventTriggers.onFailure(),
                                            updates = {
                                                update(
                                                    tileId = "gd_status",
                                                    updateData = inlineTileUpdateData("text" to "Nothing stored under that key yet.")
                                                )
                                            }
                                        )
                                    }
                                )
                            }
                        )
                    }
                    SimpleText(id = "gd_status", text = "Type something and click \"Read\".")
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                GetData(
                    trigger = EventTriggers.onClick(),
                    readings = {
                        reading(
                            dataSource = screenPlainData(),
                            accessMode = singleAccessMode("showroom_value")
                        )
                    },
                    events = {
                        UpdateTiles(trigger = EventTriggers.onSuccess(), updates = {
                            update("result_label", incomingTileUpdateData())
                        })
                        DisplaySnackbar(trigger = EventTriggers.onFailure(), message = "Nothing stored yet")
                    }
                )
                """
            )

            ShowroomNote(
                "singleAccessMode() fires onFailure() (DataNotFoundException) if the key doesn't exist. Use " +
                    "fullAccessMode() when you're not sure whether the value has already been written."
            )

            ShowroomRelated(
                names = listOf("UpdateData", "RemoveData", "EvaluateData"),
                destination = "eventDetails"
            )
        }
    }
}
