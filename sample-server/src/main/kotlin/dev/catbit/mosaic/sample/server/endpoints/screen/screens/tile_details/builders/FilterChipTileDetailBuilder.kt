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
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.chips.FilterChip
import dev.catbit.mosaic.server.builder.tile.builders.chips.defaultFilterChip
import dev.catbit.mosaic.server.builder.tile.builders.chips.elevatedFilterChip
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row

private val filters = listOf("active" to "Active", "archived" to "Archived", "starred" to "Starred")

object FilterChipTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "FilterChip"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A Material 3 chip with a toggleable selected state — used to filter content. " +
                    "selected is fully controlled by the server: onCheck/onUncheck/onCheckChanged fire, but " +
                    "the visual only actually changes once the server responds with UpdateTiles."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Tap several — they're all independent (multi-select)") {
                Row(arrangement = arrangeHorizontallySpacedBy(8)) {
                    filters.forEach { (id, label) ->
                        FilterChip(
                            id = "filter_chip_demo_$id",
                            text = label,
                            selected = false,
                            leadingIcon = icon("check"),
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onCheck(),
                                    updates = {
                                        update(tileId = "filter_chip_demo_$id", updateData = inlineTileUpdateData("selected" to true))
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onUncheck(),
                                    updates = {
                                        update(tileId = "filter_chip_demo_$id", updateData = inlineTileUpdateData("selected" to false))
                                    }
                                )
                            }
                        )
                    }
                }
            }

            ShowroomSectionTitle("variant, selected = true, enabled = false, trailingIcon")
            ShowroomDemoCard(title = "Static examples of every other configuration") {
                Row(arrangement = arrangeHorizontallySpacedBy(8)) {
                    FilterChip(text = "Default", selected = false, variant = defaultFilterChip())
                    FilterChip(text = "Elevated", selected = false, variant = elevatedFilterChip())
                    FilterChip(text = "Pre-selected", selected = true, leadingIcon = icon("check"))
                    FilterChip(text = "Trailing icon", selected = false, trailingIcon = icon("arrow_drop_down"))
                    FilterChip(text = "Disabled", selected = false, enabled = false)
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                FilterChip(
                    id = "activeFilter",
                    text = "Active",
                    selected = false,
                    leadingIcon = icon("check"),
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onCheck(),
                            updates = { update(tileId = "activeFilter", updateData = inlineTileUpdateData("selected" to true)) }
                        )
                        UpdateTiles(
                            trigger = EventTriggers.onUncheck(),
                            updates = { update(tileId = "activeFilter", updateData = inlineTileUpdateData("selected" to false)) }
                        )
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("AssistChip", "InputChip", "Checkbox"),
                destination = "tileDetails"
            )
        }
    }
}
