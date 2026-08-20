package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPageChangedEventTrigger
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.color.themeColorPrimaryContainer
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.mappedIncomingTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Pager
import dev.catbit.mosaic.server.builder.tile.builders.grouping.pageFill
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object PagerTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Pager"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A swipeable horizontal pager — each child tile occupies a full page. Great for " +
                    "onboarding, galleries, and wizards. OnPageChanged fires with a Direction (Any/Start/End/" +
                    "Index(n)) every time the page settles on a new one (the initial page is skipped), carrying " +
                    "the new 0-based page index as incomingData."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Swipe between the 3 pages — the indicator below reacts for real") {
                Pager(
                    id = "pager_demo",
                    pageSize = pageFill(),
                    style = { size(width = fillHorizontally(), height = fixedVertically(100)) },
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onPageChanged(OnPageChangedEventTrigger.Direction.Any),
                            updates = {
                                update(
                                    tileId = "pager_demo_indicator",
                                    updateData = mappedIncomingTileUpdateData("text" to "Current page index: <||>")
                                )
                            }
                        )
                    }
                ) {
                    listOf("0", "1", "2").forEach { page ->
                        Box(
                            style = {
                                size(width = fillHorizontally(), height = fillVertically())
                                background(color(themeColorPrimaryContainer()))
                            }
                        ) {
                            SimpleText(text = "Page index $page")
                        }
                    }
                }
                SimpleText(id = "pager_demo_indicator", text = "Current page index: 0", color = color(themeColorOnSurfaceVariant()))
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                Pager(
                    id = "onboardingPager",
                    pageSize = pageFill(),
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onPageChanged(OnPageChangedEventTrigger.Direction.Any),
                            updates = {
                                update(
                                    tileId = "pageIndicator",
                                    updateData = mappedIncomingTileUpdateData("text" to "Page <||>")
                                )
                            }
                        )
                    }
                ) {
                    onboardingPages.forEach { page -> Column(id = "page_${'$'}{page.index}") { SimpleText(text = page.title) } }
                }
                """
            )

            ShowroomRelated(
                names = listOf("Carousel", "Tabs", "LazyRow"),
                destination = "tileDetails"
            )
        }
    }
}
