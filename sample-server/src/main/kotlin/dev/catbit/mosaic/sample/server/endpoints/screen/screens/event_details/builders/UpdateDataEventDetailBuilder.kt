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
import dev.catbit.mosaic.server.builder.event.builders.data.explicitUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.mappedIncomingTileUpdateData
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.inputs.Checkbox
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object UpdateDataEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "UpdateData"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Writes key-value data into one or more sources — a screen's in-memory " +
                    "DataHolder or a persistent database — to be read back later with GetData. Use it after " +
                    "receiving a value that needs to be stored — a session token, a user preference, form " +
                    "state. Always prefer explicitUpdateData(dataId, value) / " +
                    "explicitIncomingUpdateData(dataId) to write a single value or an entire record under one " +
                    "dataId. The legacy variants inlineUpdateData(...) / incomingUpdateData() explode each map " +
                    "key into a separate dataId — only correct when the map genuinely represents independent " +
                    "dataId → value pairs."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Check the box, save to the DataHolder, and read it back with GetData") {
                Column(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeVerticallySpacedBy(12)
                ) {
                    Row(arrangement = arrangeHorizontallySpacedBy(8), alignment = alignVerticallyToCenter()) {
                        Checkbox(
                            id = "ud_checkbox",
                            checked = false,
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onCheck(),
                                    updates = {
                                        update(tileId = "ud_checkbox", updateData = inlineTileUpdateData("checked" to true))
                                    }
                                )
                                UpdateData(
                                    trigger = EventTriggers.onCheck(),
                                    updates = {
                                        update(
                                            dataSource = screenPlainData(),
                                            updateData = explicitUpdateData(dataId = "ud_flag", value = true)
                                        )
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onUncheck(),
                                    updates = {
                                        update(tileId = "ud_checkbox", updateData = inlineTileUpdateData("checked" to false))
                                    }
                                )
                                UpdateData(
                                    trigger = EventTriggers.onUncheck(),
                                    updates = {
                                        update(
                                            dataSource = screenPlainData(),
                                            updateData = explicitUpdateData(dataId = "ud_flag", value = false)
                                        )
                                    }
                                )
                            }
                        )
                        SimpleText(text = "I accept the terms")
                    }
                    Button(
                        text = "Read",
                        events = {
                            GetData(
                                trigger = EventTriggers.onClick(),
                                readings = {
                                    reading(
                                        dataSource = screenPlainData(),
                                        accessMode = singleAccessMode("ud_flag")
                                    )
                                },
                                events = {
                                    UpdateTiles(
                                        trigger = EventTriggers.onSuccess(),
                                        updates = {
                                            update(
                                                tileId = "ud_status",
                                                updateData = mappedIncomingTileUpdateData("text" to "Stored in the DataHolder: <//>")
                                            )
                                        }
                                    )
                                    UpdateTiles(
                                        trigger = EventTriggers.onFailure(),
                                        updates = {
                                            update(
                                                tileId = "ud_status",
                                                updateData = inlineTileUpdateData("text" to "Nothing stored yet.")
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    )
                    SimpleText(id = "ud_status", text = "Check the box and click \"Read\".")
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                UpdateData(
                    trigger = EventTriggers.onCheck(),
                    updates = {
                        update(
                            dataSource = screenPlainData(),
                            updateData = explicitUpdateData(dataId = "accepted_terms", value = true)
                        )
                    }
                )
                """
            )

            ShowroomNote(
                "The ScreenNavigationData and Tile sources are ignored as a write target (no-op). " +
                    "explicitNullUpdateData/explicitUpdateData(dataId, null) only work on in-memory sources — " +
                    "persistent databases silently discard null writes."
            )

            ShowroomRelated(
                names = listOf("GetData", "RemoveData", "SendData"),
                destination = "eventDetails"
            )
        }
    }
}
