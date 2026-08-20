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
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.inputs.Checkbox
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object CheckboxTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Checkbox"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A Material 3 checkbox whose checked/unchecked state is fully controlled by the " +
                    "server. It's a 100% controlled component: the visual always reverts to the checked value " +
                    "the server last sent. A click fires OnCheck or OnUncheck — it's up to the server to " +
                    "respond with UpdateTiles confirming (or denying) the change."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Accepting the terms unlocks the button — just like a real form") {
                Row(arrangement = arrangeHorizontallySpacedBy(12), alignment = alignVerticallyToCenter()) {
                    Checkbox(
                        id = "checkbox_demo_terms",
                        checked = false,
                        events = {
                            UpdateTiles(
                                trigger = EventTriggers.onCheck(),
                                updates = {
                                    update(tileId = "checkbox_demo_terms", updateData = inlineTileUpdateData("checked" to true))
                                    update(tileId = "checkbox_demo_submit", updateData = inlineTileUpdateData("enabled" to true))
                                }
                            )
                            UpdateTiles(
                                trigger = EventTriggers.onUncheck(),
                                updates = {
                                    update(tileId = "checkbox_demo_terms", updateData = inlineTileUpdateData("checked" to false))
                                    update(tileId = "checkbox_demo_submit", updateData = inlineTileUpdateData("enabled" to false))
                                }
                            )
                        }
                    )
                    SimpleText(text = "I accept the terms of use")
                }
                Button(
                    id = "checkbox_demo_submit",
                    text = "Submit",
                    enabled = false
                )
            }

            ShowroomSectionTitle("enabled = false")
            ShowroomDemoCard(title = "Checked and unchecked, both disabled") {
                Row(arrangement = arrangeHorizontallySpacedBy(24), alignment = alignVerticallyToCenter()) {
                    Row(arrangement = arrangeHorizontallySpacedBy(8), alignment = alignVerticallyToCenter()) {
                        Checkbox(checked = true, enabled = false)
                        SimpleText(text = "Checked, disabled")
                    }
                    Row(arrangement = arrangeHorizontallySpacedBy(8), alignment = alignVerticallyToCenter()) {
                        Checkbox(checked = false, enabled = false)
                        SimpleText(text = "Unchecked, disabled")
                    }
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                Checkbox(
                    id = "termsCheckbox",
                    checked = false,
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onCheck(),
                            updates = {
                                update(tileId = "termsCheckbox", updateData = inlineTileUpdateData("checked" to true))
                                update(tileId = "submitButton", updateData = inlineTileUpdateData("enabled" to true))
                            }
                        )
                        UpdateTiles(
                            trigger = EventTriggers.onUncheck(),
                            updates = {
                                update(tileId = "termsCheckbox", updateData = inlineTileUpdateData("checked" to false))
                                update(tileId = "submitButton", updateData = inlineTileUpdateData("enabled" to false))
                            }
                        )
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("Switch", "RadioButton", "FilterChip"),
                destination = "tileDetails"
            )
        }
    }
}
