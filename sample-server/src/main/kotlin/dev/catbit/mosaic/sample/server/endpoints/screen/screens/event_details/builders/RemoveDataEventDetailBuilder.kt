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
import dev.catbit.mosaic.server.builder.data.fullAccessMode
import dev.catbit.mosaic.server.builder.data.screenSegmentedData
import dev.catbit.mosaic.server.builder.data.singleAccessMode
import dev.catbit.mosaic.server.builder.event.builders.data.GetData
import dev.catbit.mosaic.server.builder.event.builders.data.RemoveData
import dev.catbit.mosaic.server.builder.event.builders.data.TransformData
import dev.catbit.mosaic.server.builder.event.builders.data.UpdateData
import dev.catbit.mosaic.server.builder.event.builders.data.inlineUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.incomingTileUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledTonalButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium

object RemoveDataEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "RemoveData"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Erases data from a data store — a single key, a batch of keys, or the whole " +
                    "store (fullAccessMode()). Use it to clear session data on logout, remove a cached value, " +
                    "or reset a form segment. ScreenNavigationData and Tile are ignored as targets (no " +
                    "effect). fullAccessMode() erases the entire segment — use with care."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Write a value, confirm it exists, then remove it") {
                SimpleText(
                    id = "remove_data_value",
                    text = "(no value read yet)",
                    typography = typographyTitleMedium()
                )
                Row(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeHorizontallySpacedBy(8)
                ) {
                    Button(
                        text = "Write value",
                        buttonType = outlinedButton(),
                        events = {
                            UpdateData(
                                trigger = EventTriggers.onClick(),
                                updates = {
                                    update(
                                        dataSource = screenSegmentedData("remove_data_demo"),
                                        updateData = inlineUpdateData("note" to "Sample value written")
                                    )
                                }
                            )
                        }
                    )
                    Button(
                        text = "Read",
                        buttonType = outlinedButton(),
                        events = {
                            GetData(
                                trigger = EventTriggers.onClick(),
                                readings = {
                                    reading(
                                        dataSource = screenSegmentedData("remove_data_demo"),
                                        accessMode = singleAccessMode("note")
                                    )
                                },
                                events = {
                                    TransformData(
                                        trigger = EventTriggers.onSuccess(),
                                        template = "<||>",
                                        events = {
                                            UpdateTiles(
                                                trigger = EventTriggers.onSuccess(),
                                                updates = {
                                                    update("remove_data_value", incomingTileUpdateData())
                                                }
                                            )
                                        }
                                    )
                                    UpdateTiles(
                                        trigger = EventTriggers.onFailure(),
                                        updates = {
                                            update(
                                                "remove_data_value",
                                                inlineTileUpdateData("text" to "Not found — it was removed or never written")
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    )
                    Button(
                        text = "Remove with RemoveData",
                        buttonType = filledTonalButton(),
                        events = {
                            RemoveData(
                                trigger = EventTriggers.onClick(),
                                deletions = {
                                    addDeletion(
                                        dataSource = screenSegmentedData("remove_data_demo"),
                                        accessMode = fullAccessMode()
                                    )
                                }
                            )
                        }
                    )
                }
                ShowroomNote(
                    "After removing, click \"Read\" again: singleAccessMode no longer finds the \"note\" key " +
                        "and GetData fires onFailure, updating the text to the \"not found\" message."
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                RemoveData(
                    trigger = EventTriggers.onClick(),
                    deletions = {
                        addDeletion(
                            dataSource = segmentedDataBase("auth"),
                            accessMode = fullAccessMode()
                        )
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("UpdateData", "GetData"),
                destination = "eventDetails"
            )
        }
    }
}
