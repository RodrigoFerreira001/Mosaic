package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.SkeletonBlock
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.SkeletonListEntry
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Shimmer

object ShimmerTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Shimmer"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A loading (shimmer) effect layered over its child tiles — skeleton screens while " +
                    "real data loads. It stays animated the whole time it's visible — there's no built-in " +
                    "\"stop\" state. To remove it, swap it out (e.g. via AddTiles/UpdateTiles/re-navigating) " +
                    "once the real data arrives. Skeleton shapes here (SkeletonBlock/SkeletonListEntry) aren't " +
                    "separate tiles — they're plain Box/Row compositions, the same technique any Mosaic screen " +
                    "uses to build a reusable chunk of UI."
            )

            ShowroomSectionTitle("Interactive demo — a real detail-screen skeleton")
            ShowroomDemoCard(title = "Header row + hero image + list, all shimmering together") {
                Shimmer(
                    style = {
                        size(width = fillHorizontally(), height = fixedVertically(420))
                    }
                ) {
                    Column(
                        style = { size(width = fillHorizontally(), height = fillVertically()) },
                        arrangement = arrangeVerticallySpacedBy(20)
                    ) {
                        Row(
                            style = { size(width = fillHorizontally(), height = fixedVertically(64)) },
                            arrangement = arrangeHorizontallySpacedBy(16),
                            alignment = alignVerticallyToCenter()
                        ) {
                            SkeletonBlock(width = 40, height = 40, circular = true)
                            SkeletonBlock(width = 160, height = 24)
                        }
                        Column(
                            style = { size(width = fillHorizontally(), height = fillVertically()) },
                            arrangement = arrangeVerticallySpacedBy(16)
                        ) {
                            SkeletonBlock(width = 120, height = 20)
                            SkeletonBlock(width = null, height = 180)
                            SkeletonBlock(width = 120, height = 20)
                            repeat(3) { SkeletonListEntry() }
                        }
                    }
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                Shimmer(
                    style = {
                        size(width = fillHorizontally(), height = fillVertically())
                        windowInsets(windowInsetsSystemBars())
                    }
                ) {
                    Column(style = { size(width = fillHorizontally(), height = fillVertically()) }) {
                        Row(
                            style = { size(width = fillHorizontally(), height = fixedVertically(64)); padding(horizontal = 16) },
                            arrangement = arrangeHorizontallySpacedBy(16),
                            alignment = alignVerticallyToCenter()
                        ) {
                            SkeletonBlock(style = { size(width = fixedHorizontally(40), height = fixedVertically(40)); clip(circleShape()) })
                            SkeletonBlock(style = { size(width = fixedHorizontally(160), height = fixedVertically(24)) })
                        }
                        Column(
                            style = { size(width = fillHorizontally(), height = fillVertically()); padding(horizontal = 24) },
                            arrangement = arrangeVerticallySpacedBy(16)
                        ) {
                            SkeletonBlock(style = { size(width = fixedHorizontally(120), height = fixedVertically(20)) })
                            SkeletonBlock(style = { size(width = fillHorizontally(), height = fixedVertically(400)) })
                            SkeletonBlock(style = { size(width = fixedHorizontally(120), height = fixedVertically(20)) })
                            repeat(3) { SkeletonListEntry() }
                        }
                    }
                }

                // SkeletonBlock/SkeletonListEntry aren't tiles — they're private compositions built from
                // plain Box/Row, kept next to the screens that use them. See ShowroomComponents.kt's
                // SkeletonBlock/SkeletonListEntry in this sample-server for the exact reusable version.
                """
            )

            ShowroomNote(
                "Swap the Shimmer for the real content once the data arrives — see LazyTiles and " +
                    "PullToRefresh for full loading-flow examples that actually replace a placeholder with " +
                    "fetched tiles."
            )

            ShowroomRelated(
                names = listOf("LazyTiles", "PullToRefresh", "CircularProgressIndicator"),
                destination = "tileDetails"
            )
        }
    }
}
