package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.mappedIncomingTileUpdateData
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.inputs.DatePicker
import dev.catbit.mosaic.server.builder.tile.builders.inputs.errorDatePicker
import dev.catbit.mosaic.server.builder.tile.builders.inputs.filledDatePicker
import dev.catbit.mosaic.server.builder.tile.builders.inputs.outlinedDatePicker
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object DatePickerTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "DatePicker"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A TextField-styled field that opens a native DatePickerDialog when tapped. " +
                    "selectedDate is an ISO-8601 String (\"2026-07-07\") or null. The client controls opening " +
                    "and closing the dialog — the server only receives OnDateSelected with the chosen date in " +
                    "incomingData."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Pick a date — the text below shows what reached the server") {
                DatePicker(
                    id = "date_picker_demo",
                    selectedDate = null,
                    kind = outlinedDatePicker(),
                    confirmLabel = "OK",
                    cancelLabel = "Cancel",
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onDateSelected(),
                            updates = {
                                update(
                                    tileId = "date_picker_demo",
                                    updateData = mappedIncomingTileUpdateData("selectedDate" to "<||>")
                                )
                                update(
                                    tileId = "date_picker_demo_label",
                                    updateData = mappedIncomingTileUpdateData("text" to "Chosen date: <||>")
                                )
                            }
                        )
                    }
                )
                SimpleText(id = "date_picker_demo_label", text = "Chosen date: none yet")
            }

            ShowroomSectionTitle("kind, state, supportingText")
            ShowroomDemoCard(title = "filledDatePicker(), errorDatePicker() with supportingText, pre-filled value") {
                Row(arrangement = arrangeHorizontallySpacedBy(12)) {
                    DatePicker(
                        selectedDate = null,
                        kind = filledDatePicker(),
                        confirmLabel = "OK",
                        cancelLabel = "Cancel"
                    )
                    DatePicker(
                        selectedDate = null,
                        kind = outlinedDatePicker(),
                        state = errorDatePicker(),
                        supportingText = "Date is required",
                        confirmLabel = "OK",
                        cancelLabel = "Cancel"
                    )
                    DatePicker(
                        selectedDate = "1990-01-01",
                        kind = outlinedDatePicker(),
                        confirmLabel = "OK",
                        cancelLabel = "Cancel"
                    )
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                DatePicker(
                    id = "dp_birthdate",
                    selectedDate = "1990-01-01",
                    kind = outlinedDatePicker(),
                    confirmLabel = "OK",
                    cancelLabel = "Cancel",
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onDateSelected(),
                            updates = { update(tileId = "dp_birthdate", updateData = mappedIncomingTileUpdateData("selectedDate" to "<||>")) }
                        )
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("TimePicker", "TextField"),
                destination = "tileDetails"
            )
        }
    }
}
