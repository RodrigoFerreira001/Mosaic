package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.tiles.AddTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.insertAtEnd
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.LazyColumn
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object LazyColumnTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "LazyColumn"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A lazy vertical list — it only composes the visible items. Ideal for long or " +
                    "paginated lists. scrollThreshold sets how many items from the end OnScrollThresholdReached " +
                    "fires — Mosaic's standard pagination mechanism: the server reacts by adding more items " +
                    "via AddTiles(insertAtEnd())."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Scroll to the end — 3 new items arrive via a real AddTiles") {
                LazyColumn(
                    id = "lazy_column_demo",
                    scrollThreshold = 1,
                    style = { size(width = fillHorizontally(), height = fixedVertically(160)) },
                    events = {
                        AddTiles(
                            trigger = EventTriggers.onScrollThresholdReached(),
                            groupingTileId = "lazy_column_demo",
                            position = insertAtEnd(),
                            tiles = {
                                repeat(3) {
                                    SimpleText(id = randomId(), text = "Item loaded via pagination")
                                }
                            },
                            events = {
                                // The 3 ids above are baked into this event's schema once, at screen-build
                                // time — firing AddTiles a second time would insert the same ids again and
                                // crash the LazyColumn. Disabling the threshold after one page mirrors a
                                // real "last page reached" pagination end-state.
                                UpdateTiles(
                                    trigger = EventTriggers.onSuccess(),
                                    updates = {
                                        update(
                                            tileId = "lazy_column_demo",
                                            updateData = inlineTileUpdateData("scrollThreshold" to null)
                                        )
                                    }
                                )
                            }
                        )
                    }
                ) {
                    repeat(6) { i -> SimpleText(id = randomId(), text = "Initial item ${i + 1}") }
                }
            }
            ShowroomNote(
                text = "This demo disables scrollThreshold after the one page load above. AddTiles's tiles " +
                    "block is evaluated once when the screen is built, so the same ids would be reinserted " +
                    "(and crash the list) if the threshold fired again — a real backend paginating indefinitely " +
                    "should generate fresh ids per page instead of relying on a single static AddTiles."
            )

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                LazyColumn(
                    id = "itemList",
                    scrollThreshold = 3,
                    style = { size(width = fillHorizontally(), height = fillVertically()) },
                    events = {
                        // Real pagination: fetch the next page and splice its tiles in — each
                        // response builds fresh ids server-side, so repeated fetches never collide.
                        SendNetworkRequest(
                            trigger = EventTriggers.onScrollThresholdReached(),
                            request = get(url = "/items", queryParameters = { "page" to nextPage }),
                            events = {
                                AddTiles(
                                    trigger = EventTriggers.onSuccess(),
                                    groupingTileId = "itemList",
                                    position = insertAtEnd(),
                                    tiles = { /* build tiles from the response's incomingData */ }
                                )
                            }
                        )
                    }
                ) {
                    initialItems.forEach { item -> SimpleText(id = item.id, text = item.title) }
                }
                """
            )

            ShowroomRelated(
                names = listOf("Column", "LazyRow", "PullToRefresh"),
                destination = "tileDetails"
            )
        }
    }
}
