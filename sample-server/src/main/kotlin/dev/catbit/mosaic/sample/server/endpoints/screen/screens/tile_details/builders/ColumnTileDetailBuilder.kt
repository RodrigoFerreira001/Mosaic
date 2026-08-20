package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.data.TransformData
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.incomingTileUpdateData
import dev.catbit.mosaic.server.builder.placement.alignHorizontallyToCenter
import dev.catbit.mosaic.server.builder.placement.alignHorizontallyToEnd
import dev.catbit.mosaic.server.builder.placement.alignHorizontallyToStart
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.search.SearchBar
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object ColumnTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Column"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Stacks child tiles from top to bottom — Mosaic's most-used vertical container. " +
                    "scrollable = true turns on vertical scrolling when every item is already known upfront " +
                    "(otherwise, use LazyColumn). filterChildrenByTerm filters children by their " +
                    "searchableTerms — exactly the mechanism this showroom's Tile and Event catalogs use."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Search below — filterChildrenByTerm filters the items for real") {
                SearchBar(
                    id = "column_demo_search",
                    placeholder = "Filter fruits",
                    events = {
                        TransformData(
                            trigger = EventTriggers.onQueryChanged(),
                            template = mapOf("filterChildrenByTerm" to "<||>"),
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onSuccess(),
                                    updates = {
                                        update(tileId = "column_demo_list", updateData = incomingTileUpdateData())
                                    }
                                )
                            }
                        )
                    }
                )
                Column(
                    id = "column_demo_list",
                    arrangement = arrangeVerticallySpacedBy(4)
                ) {
                    listOf("Apple", "Banana", "Orange", "Grape", "Mango").forEach { fruit ->
                        SimpleText(text = fruit, searchableTerms = listOf(fruit))
                    }
                }
            }

            ShowroomSectionTitle("alignment — start/center/end")
            ShowroomDemoCard(title = "Same 2 short items, alignHorizontallyToStart/Center/End()") {
                Row(arrangement = arrangeHorizontallySpacedBy(16)) {
                    listOf(
                        "Start" to alignHorizontallyToStart(),
                        "Center" to alignHorizontallyToCenter(),
                        "End" to alignHorizontallyToEnd()
                    ).forEach { (label, alignment) ->
                        Column(
                            style = { size(width = fixedHorizontally(100), height = wrapVertically()) },
                            alignment = alignment,
                            arrangement = arrangeVerticallySpacedBy(4)
                        ) {
                            SimpleText(text = label)
                            SimpleText(text = "Hi")
                        }
                    }
                }
            }

            ShowroomSectionTitle("scrollable = true")
            ShowroomDemoCard(title = "20 items in a fixed 150dp-tall box — scrolls vertically") {
                Column(
                    style = { size(width = fillHorizontally(), height = fixedVertically(150)) },
                    scrollable = true,
                    arrangement = arrangeVerticallySpacedBy(6)
                ) {
                    repeat(20) { i -> SimpleText(text = "Item ${i + 1}") }
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                Column(
                    id = "environmentList",
                    arrangement = arrangeVerticallyToTop(),
                    style = { size(width = fillHorizontally(), height = fillVertically()) }
                ) {
                    environments.forEach { env -> EnvironmentTile(env) }
                }
                """
            )

            ShowroomRelated(
                names = listOf("Row", "LazyColumn", "Box"),
                destination = "tileDetails"
            )
        }
    }
}
