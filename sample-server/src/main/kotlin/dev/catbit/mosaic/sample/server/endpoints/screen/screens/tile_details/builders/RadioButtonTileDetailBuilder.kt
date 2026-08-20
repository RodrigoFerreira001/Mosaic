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
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.inputs.RadioButton
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

private val frequencies = listOf("daily" to "Daily", "weekly" to "Weekly", "monthly" to "Monthly")

object RadioButtonTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "RadioButton"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A Material 3 radio button — tiles sharing the same groupId form a single-" +
                    "selection group. On tap, the client fires OnSelect with the tapped tile's id and groupId, " +
                    "but the server still decides who ends up selected: you need an UpdateTiles that manually " +
                    "deselects the other members of the group."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Pick a frequency — the other two get deselected") {
                frequencies.forEach { (id, label) ->
                    Row(arrangement = arrangeHorizontallySpacedBy(12), alignment = alignVerticallyToCenter()) {
                        RadioButton(
                            id = "radio_demo_$id",
                            groupId = "radio_demo_group",
                            selected = id == "daily",
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onSelect(),
                                    updates = {
                                        frequencies.forEach { (otherId, _) ->
                                            update(
                                                tileId = "radio_demo_$otherId",
                                                updateData = inlineTileUpdateData("selected" to (otherId == id))
                                            )
                                        }
                                        update(
                                            tileId = "radio_demo_selected_label",
                                            updateData = inlineTileUpdateData("text" to "Selected: $label")
                                        )
                                    }
                                )
                            }
                        )
                        SimpleText(text = label)
                    }
                }
                SimpleText(id = "radio_demo_selected_label", text = "Selected: Daily")
            }

            ShowroomSectionTitle("A second, independent group + enabled = false")
            ShowroomDemoCard(title = "Different groupId — selecting here doesn't touch the group above") {
                Row(arrangement = arrangeHorizontallySpacedBy(12), alignment = alignVerticallyToCenter()) {
                    RadioButton(id = "radio_group_b_a", groupId = "radio_demo_group_b", selected = true)
                    SimpleText(text = "Option A")
                    RadioButton(id = "radio_group_b_b", groupId = "radio_demo_group_b", selected = false)
                    SimpleText(text = "Option B")
                    RadioButton(id = "radio_group_b_disabled", groupId = "radio_demo_group_b", selected = false, enabled = false)
                    SimpleText(text = "Disabled")
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                listOf("daily", "weekly", "monthly").forEachIndexed { i, freq ->
                    RadioButton(
                        id = "freq_${'$'}freq",
                        groupId = "frequency_group",
                        selected = i == 0,
                        events = {
                            UpdateTiles(
                                trigger = EventTriggers.onSelect(),
                                updates = {
                                    listOf("daily", "weekly", "monthly").forEach { f ->
                                        update(
                                            tileId = "freq_${'$'}f",
                                            updateData = inlineTileUpdateData("selected" to (f == freq))
                                        )
                                    }
                                }
                            )
                        }
                    )
                }
                """
            )

            ShowroomRelated(
                names = listOf("Checkbox", "DropdownList", "Switch"),
                destination = "tileDetails"
            )
        }
    }
}
