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
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.inputs.DropdownList
import dev.catbit.mosaic.server.builder.tile.builders.inputs.errorDropdownList
import dev.catbit.mosaic.server.builder.tile.builders.inputs.filledDropdownList
import dev.catbit.mosaic.server.builder.tile.builders.inputs.outlinedDropdownList
import dev.catbit.mosaic.server.builder.tile.builders.inputs.selectOption
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object DropdownListTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "DropdownList"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Material 3's ExposedDropdownMenuBox — choosing one option from a predefined " +
                    "list. The client always sends expanded = false — whether the menu is open is controlled " +
                    "by the client itself, not the server. The server only needs to react to " +
                    "OnDropdownListItemSelected(id) and keep selectedOptionId in sync."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Pick a status — each option fires its own trigger") {
                DropdownList(
                    id = "dropdown_demo",
                    options = listOf(
                        selectOption("active", "Active"),
                        selectOption("inactive", "Inactive"),
                        selectOption("pending", "Pending"),
                    ),
                    selectedOptionId = "active",
                    kind = outlinedDropdownList(),
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onDropdownListItemSelected("active"),
                            updates = {
                                update(tileId = "dropdown_demo", updateData = inlineTileUpdateData("selectedOptionId" to "active"))
                                update(tileId = "dropdown_demo_label", updateData = inlineTileUpdateData("text" to "Status: Active"))
                            }
                        )
                        UpdateTiles(
                            trigger = EventTriggers.onDropdownListItemSelected("inactive"),
                            updates = {
                                update(tileId = "dropdown_demo", updateData = inlineTileUpdateData("selectedOptionId" to "inactive"))
                                update(tileId = "dropdown_demo_label", updateData = inlineTileUpdateData("text" to "Status: Inactive"))
                            }
                        )
                        UpdateTiles(
                            trigger = EventTriggers.onDropdownListItemSelected("pending"),
                            updates = {
                                update(tileId = "dropdown_demo", updateData = inlineTileUpdateData("selectedOptionId" to "pending"))
                                update(tileId = "dropdown_demo_label", updateData = inlineTileUpdateData("text" to "Status: Pending"))
                            }
                        )
                    }
                )
                SimpleText(id = "dropdown_demo_label", text = "Status: Active")
            }

            ShowroomSectionTitle("kind, state, supportingText, enabled = false")
            ShowroomDemoCard(title = "filledDropdownList(), errorDropdownList() with supportingText, disabled") {
                Row(arrangement = arrangeHorizontallySpacedBy(12)) {
                    DropdownList(
                        options = listOf(selectOption("a", "Option A"), selectOption("b", "Option B")),
                        selectedOptionId = "a",
                        kind = filledDropdownList()
                    )
                    DropdownList(
                        options = listOf(selectOption("a", "Option A"), selectOption("b", "Option B")),
                        selectedOptionId = "a",
                        kind = outlinedDropdownList(),
                        state = errorDropdownList(),
                        supportingText = "Selection required"
                    )
                    DropdownList(
                        options = listOf(selectOption("a", "Option A"), selectOption("b", "Option B")),
                        selectedOptionId = "a",
                        kind = outlinedDropdownList(),
                        enabled = false
                    )
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                DropdownList(
                    id = "dd_status",
                    options = listOf(
                        selectOption("active", "Active"),
                        selectOption("inactive", "Inactive"),
                        selectOption("pending", "Pending"),
                    ),
                    selectedOptionId = "active",
                    kind = outlinedDropdownList(),
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onDropdownListItemSelected("inactive"),
                            updates = {
                                update(tileId = "warning_text", updateData = inlineTileUpdateData("visibility" to "VISIBLE"))
                            }
                        )
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("RadioButton", "TextField", "Menu"),
                destination = "tileDetails"
            )
        }
    }
}
