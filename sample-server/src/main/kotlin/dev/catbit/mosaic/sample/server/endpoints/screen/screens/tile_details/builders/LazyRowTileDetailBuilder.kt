package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorSecondaryContainer
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.LazyRow
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object LazyRowTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "LazyRow"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A lazy horizontal list — carousels of text items, category chips, thumbnail " +
                    "strips. It uses the same pagination mechanism as LazyColumn, just on the horizontal axis: " +
                    "scrollThreshold + OnScrollThresholdReached."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "A horizontal strip of 50 cards, with displayScrollbar = true — scroll sideways") {
                LazyRow(
                    style = { size(width = fillHorizontally(), height = fixedVertically(72)) },
                    arrangement = arrangeHorizontallySpacedBy(8),
                    displayScrollbar = true
                ) {
                    repeat(50) { i ->
                        Box(
                            style = {
                                size(width = fixedHorizontally(72), height = fixedVertically(72))
                                clip(roundedCornerShape(all = 12))
                                background(color(themeColorSecondaryContainer()))
                            }
                        ) {
                            SimpleText(text = "${i + 1}")
                        }
                    }
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                LazyRow(
                    id = "categoryRow",
                    arrangement = arrangeHorizontallySpacedBy(8)
                ) {
                    categories.forEach { cat ->
                        FilterChip(id = "cat_${'$'}{cat.id}", text = cat.name, selected = cat.isSelected)
                    }
                }
                """
            )

            ShowroomRelated(
                names = listOf("LazyColumn", "Row", "Carousel"),
                destination = "tileDetails"
            )
        }
    }
}
