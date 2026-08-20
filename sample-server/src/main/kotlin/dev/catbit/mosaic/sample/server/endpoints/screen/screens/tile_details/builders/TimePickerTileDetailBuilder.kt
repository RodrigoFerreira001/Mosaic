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
import dev.catbit.mosaic.server.builder.tile.builders.inputs.TimePicker
import dev.catbit.mosaic.server.builder.tile.builders.inputs.errorTimePicker
import dev.catbit.mosaic.server.builder.tile.builders.inputs.filledTimePicker
import dev.catbit.mosaic.server.builder.tile.builders.inputs.outlinedTimePicker
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object TimePickerTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "TimePicker"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A TextField-styled field that opens a native TimePickerDialog when tapped. " +
                    "selectedTime is a \"HH:mm\" (24h) String or null. Same contract as DatePicker: the client " +
                    "controls the dialog, the server just receives OnTimeSelected with the chosen time in " +
                    "incomingData."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Pick a time — the text below shows what the server received") {
                TimePicker(
                    id = "time_picker_demo",
                    selectedTime = null,
                    kind = outlinedTimePicker(),
                    confirmLabel = "OK",
                    cancelLabel = "Cancel",
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onTimeSelected(),
                            updates = {
                                update(
                                    tileId = "time_picker_demo",
                                    updateData = mappedIncomingTileUpdateData("selectedTime" to "<||>")
                                )
                                update(
                                    tileId = "time_picker_demo_label",
                                    updateData = mappedIncomingTileUpdateData("text" to "Chosen time: <||>")
                                )
                            }
                        )
                    }
                )
                SimpleText(id = "time_picker_demo_label", text = "Chosen time: none yet")
            }

            ShowroomSectionTitle("kind, state, supportingText")
            ShowroomDemoCard(title = "filledTimePicker(), errorTimePicker() with supportingText, pre-filled value") {
                Row(arrangement = arrangeHorizontallySpacedBy(12)) {
                    TimePicker(selectedTime = null, kind = filledTimePicker(), confirmLabel = "OK", cancelLabel = "Cancel")
                    TimePicker(
                        selectedTime = null,
                        kind = outlinedTimePicker(),
                        state = errorTimePicker(),
                        supportingText = "Time is required",
                        confirmLabel = "OK",
                        cancelLabel = "Cancel"
                    )
                    TimePicker(selectedTime = "09:00", kind = outlinedTimePicker(), confirmLabel = "OK", cancelLabel = "Cancel")
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                TimePicker(
                    id = "tp_reminder",
                    selectedTime = "09:00",
                    kind = outlinedTimePicker(),
                    confirmLabel = "OK",
                    cancelLabel = "Cancel",
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onTimeSelected(),
                            updates = { update(tileId = "tp_reminder", updateData = mappedIncomingTileUpdateData("selectedTime" to "<||>")) }
                        )
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("DatePicker", "TextField"),
                destination = "tileDetails"
            )
        }
    }
}
