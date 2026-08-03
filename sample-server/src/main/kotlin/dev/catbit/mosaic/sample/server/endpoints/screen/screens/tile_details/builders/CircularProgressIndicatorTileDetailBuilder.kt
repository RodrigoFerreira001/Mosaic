package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParagraph
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParam
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParamsTable
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
import dev.catbit.mosaic.server.builder.tile.builders.progress.CircularProgressIndicator

object CircularProgressIndicatorTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "CircularProgressIndicator"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Progress",
                description = "Indicador circular Material 3 — determinado (0.0–1.0) ou indeterminado (progress = null, gira sem parar)."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use indeterminado enquanto não se sabe a duração da operação, e determinado quando " +
                    "há uma fração real de progresso (upload, download). progress é atualizável via " +
                    "UpdateTiles a qualquer momento."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("progress", "Float?", "null = indeterminado (girando); 0.0–1.0 = determinado."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                CircularProgressIndicator(
                    id = "uploadProgress",
                    progress = null,
                    style = { size(fixedHorizontally(48), fixedVertically(48)) }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Cada botão dispara um UpdateTiles(\"progress\" to ...) real") {
                CircularProgressIndicator(
                    id = "circular_progress_demo",
                    progress = null
                )
                Row(arrangement = arrangeHorizontallySpacedBy(8)) {
                    Button(
                        text = "Indeterminado",
                        events = {
                            UpdateTiles(
                                trigger = EventTriggers.onClick(),
                                updates = {
                                    update(tileId = "circular_progress_demo", updateData = inlineTileUpdateData("progress" to null))
                                }
                            )
                        }
                    )
                    Button(
                        text = "25%",
                        events = {
                            UpdateTiles(
                                trigger = EventTriggers.onClick(),
                                updates = {
                                    update(tileId = "circular_progress_demo", updateData = inlineTileUpdateData("progress" to 0.25f))
                                }
                            )
                        }
                    )
                    Button(
                        text = "75%",
                        events = {
                            UpdateTiles(
                                trigger = EventTriggers.onClick(),
                                updates = {
                                    update(tileId = "circular_progress_demo", updateData = inlineTileUpdateData("progress" to 0.75f))
                                }
                            )
                        }
                    )
                    Button(
                        text = "100%",
                        events = {
                            UpdateTiles(
                                trigger = EventTriggers.onClick(),
                                updates = {
                                    update(tileId = "circular_progress_demo", updateData = inlineTileUpdateData("progress" to 1f))
                                }
                            )
                        }
                    )
                }
            }

            ShowroomRelated(
                names = listOf("LinearProgressIndicator", "PullToRefresh"),
                destination = "tileDetails"
            )
        }
    }
}
