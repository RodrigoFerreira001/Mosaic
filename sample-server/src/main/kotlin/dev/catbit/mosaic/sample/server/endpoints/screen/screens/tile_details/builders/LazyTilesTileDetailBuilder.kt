package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.BuildConfig
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.SkeletonBlock
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnErrorContainer
import dev.catbit.mosaic.server.builder.color.themeColorErrorContainer
import dev.catbit.mosaic.server.builder.event.builders.tiles.ReloadLazyTiles
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.LazyTiles
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object LazyTilesTileDetailBuilder : TileDetailBuilder {

    // sendHttpRequest (which backs both LazyTiles and SendNetworkRequest) never prepends the
    // client's baseUrl — url must be a fully-qualified address, matching the sample-client's own
    // hardcoded baseUrl in App.kt. A relative path here silently fails to resolve on every target.
    private val SAMPLE_SERVER_BASE_URL = BuildConfig.BASE_URL

    override fun canBuild(tileName: String) = tileName == "LazyTiles"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A self-sufficient container that fetches its own child tiles from a URL on " +
                    "first composition — sections delegated to another endpoint. The network call is a " +
                    "SingleEffect (fires once). If tiles is already populated (e.g. via UpdateTiles), the " +
                    "fetch is skipped; to force a reload, use the ReloadLazyTiles event. This demo hits a real " +
                    "sample-server route — /lazy-tiles/recommendations — that returns an actual JSON array of " +
                    "tile schemas, exactly the shape LazyTiles expects back."
            )

            ShowroomSectionTitle("Interactive demo — real network call")
            ShowroomDemoCard(title = "Fetches from /lazy-tiles/recommendations for real") {
                LazyTiles(
                    id = "lazy_tiles_demo",
                    url = "$SAMPLE_SERVER_BASE_URL/lazy-tiles/recommendations",
                    placeholderTiles = {
                        Column(arrangement = arrangeVerticallySpacedBy(10)) {
                            repeat(4) {
                                SkeletonBlock(width = null, height = 56)
                            }
                        }
                    },
                    failureTiles = {
                        Row(
                            style = {
                                size(width = fillHorizontally(), height = wrapVertically())
                                clip(roundedCornerShape(all = 12))
                                background(color(themeColorErrorContainer()))
                                padding(horizontal = 16, vertical = 16)
                            },
                            arrangement = arrangeHorizontallySpacedBy(12),
                            alignment = alignVerticallyToCenter()
                        ) {
                            Icon(icon = icon("error", color = color(themeColorOnErrorContainer())))
                            SimpleText(text = "Failed to load recommendations", color = color(themeColorOnErrorContainer()))
                        }
                    }
                )
                Row(arrangement = arrangeHorizontallySpacedBy(8)) {
                    IconButton(
                        icon = icon("refresh"),
                        events = {
                            ReloadLazyTiles(trigger = EventTriggers.onClick(), lazyTileId = "lazy_tiles_demo")
                        }
                    )
                    SimpleText(text = "Reload (retries the same request)")
                }
            }

            ShowroomSectionTitle("Failure state — same tile, forced to fail")
            ShowroomDemoCard(title = "url points at ?fail=true, so failureTiles renders instead") {
                LazyTiles(
                    id = "lazy_tiles_failure_demo",
                    url = "$SAMPLE_SERVER_BASE_URL/lazy-tiles/recommendations?fail=true",
                    placeholderTiles = {
                        Column(arrangement = arrangeVerticallySpacedBy(10)) {
                            repeat(2) { SkeletonBlock(width = null, height = 56) }
                        }
                    },
                    failureTiles = {
                        Row(
                            style = {
                                size(width = fillHorizontally(), height = wrapVertically())
                                clip(roundedCornerShape(all = 12))
                                background(color(themeColorErrorContainer()))
                                padding(horizontal = 16, vertical = 16)
                            },
                            arrangement = arrangeHorizontallySpacedBy(12),
                            alignment = alignVerticallyToCenter()
                        ) {
                            Icon(icon = icon("error", color = color(themeColorOnErrorContainer())))
                            SimpleText(text = "Failed to load recommendations", color = color(themeColorOnErrorContainer()))
                        }
                    }
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                LazyTiles(
                    id = "recommendationsSection",
                    url = "$SAMPLE_SERVER_BASE_URL/lazy-tiles/recommendations",
                    placeholderTiles = { repeat(4) { SkeletonBlock(width = null, height = 56) } },
                    failureTiles = { SimpleText(text = "Failed to load recommendations") },
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onLoadTilesSuccess(),
                            updates = { /* e.g. hide a section header while empty */ }
                        )
                    }
                )
                """
            )

            ShowroomNote(
                text = "The server side of this demo lives in sample-server's Routing.kt / " +
                    "endpoints/lazy_tiles/LazyTiles.kt — a real Ktor route building tiles with the same " +
                    "TileSchemaBuilderScope DSL used everywhere else, then responding with the built list " +
                    "directly (not wrapped in a Screen)."
            )

            ShowroomRelated(
                names = listOf("Shimmer", "PullToRefresh", "LazyColumn"),
                destination = "tileDetails"
            )
        }
    }
}
