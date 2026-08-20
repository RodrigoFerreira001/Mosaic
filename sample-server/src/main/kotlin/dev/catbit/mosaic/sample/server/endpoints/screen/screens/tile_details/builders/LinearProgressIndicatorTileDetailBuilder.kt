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
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.progress.LinearProgressIndicator

object LinearProgressIndicatorTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "LinearProgressIndicator"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A full-width Material 3 progress bar — determinate (0.0-1.0) or indeterminate " +
                    "(progress = null). Shares the same semantics as CircularProgressIndicator, just in bar form " +
                    "— ideal for downloads, form submissions, or multi-step operations shown side by side."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Each button fires a real UpdateTiles(\"progress\" to ...)") {
                LinearProgressIndicator(
                    id = "linear_progress_demo",
                    progress = null,
                    style = { size(width = fillHorizontally(), height = wrapVertically()) }
                )
                Row(arrangement = arrangeHorizontallySpacedBy(8)) {
                    Button(
                        text = "Indeterminate",
                        events = {
                            UpdateTiles(
                                trigger = EventTriggers.onClick(),
                                updates = { update(tileId = "linear_progress_demo", updateData = inlineTileUpdateData("progress" to null)) }
                            )
                        }
                    )
                    Button(
                        text = "30%",
                        events = {
                            UpdateTiles(
                                trigger = EventTriggers.onClick(),
                                updates = { update(tileId = "linear_progress_demo", updateData = inlineTileUpdateData("progress" to 0.3f)) }
                            )
                        }
                    )
                    Button(
                        text = "80%",
                        events = {
                            UpdateTiles(
                                trigger = EventTriggers.onClick(),
                                updates = { update(tileId = "linear_progress_demo", updateData = inlineTileUpdateData("progress" to 0.8f)) }
                            )
                        }
                    )
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                LinearProgressIndicator(
                    id = "downloadBar",
                    progress = 0.45f,
                    style = { size(width = fillHorizontally(), height = wrapVertically()) }
                )
                """
            )

            ShowroomRelated(
                names = listOf("CircularProgressIndicator", "PullToRefresh"),
                destination = "tileDetails"
            )
        }
    }
}
