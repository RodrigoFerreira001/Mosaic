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
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.chips.FilterChip
import dev.catbit.mosaic.server.builder.tile.builders.grouping.FlowRow

private val labels = listOf("Kotlin", "Compose", "Ktor", "SDUI", "Multiplatform", "Material 3", "Coroutines", "Serialization")

object FlowRowTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "FlowRow"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A row that automatically wraps to the next line when content exceeds the " +
                    "width — chip groups, tag clouds. Simpler than FlexBox when you only need standard " +
                    "horizontal wrapping, without direction/align control. maxItemsInEachRow limits how many " +
                    "items fit per row."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "8 FilterChip really wrapping lines — all clickable") {
                FlowRow(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    horizontalArrangement = arrangeHorizontallySpacedBy(8),
                    verticalArrangement = arrangeVerticallySpacedBy(8)
                ) {
                    labels.forEach { label ->
                        FilterChip(
                            id = "flow_row_demo_$label",
                            text = label,
                            selected = false,
                            leadingIcon = icon("check"),
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onCheck(),
                                    updates = { update(tileId = "flow_row_demo_$label", updateData = inlineTileUpdateData("selected" to true)) }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onUncheck(),
                                    updates = { update(tileId = "flow_row_demo_$label", updateData = inlineTileUpdateData("selected" to false)) }
                                )
                            }
                        )
                    }
                }
            }

            ShowroomSectionTitle("maxItemsInEachRow — forcing a fixed number of columns")
            ShowroomDemoCard(title = "Same 8 items, maxItemsInEachRow = 3 — always exactly 3 per row") {
                FlowRow(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    horizontalArrangement = arrangeHorizontallySpacedBy(8),
                    verticalArrangement = arrangeVerticallySpacedBy(8),
                    maxItemsInEachRow = 3
                ) {
                    labels.forEach { label ->
                        FilterChip(id = "flow_row_maxitems_demo_$label", text = label, selected = false)
                    }
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                FlowRow(
                    id = "filterChips",
                    horizontalArrangement = arrangeHorizontallySpacedBy(8),
                    verticalArrangement = arrangeVerticallySpacedBy(8)
                ) {
                    filters.forEach { f -> FilterChip(id = "filter_${'$'}{f.id}", text = f.label, selected = f.isActive) }
                }
                """
            )

            ShowroomRelated(
                names = listOf("Row", "FlexBox", "FilterChip"),
                destination = "tileDetails"
            )
        }
    }
}
