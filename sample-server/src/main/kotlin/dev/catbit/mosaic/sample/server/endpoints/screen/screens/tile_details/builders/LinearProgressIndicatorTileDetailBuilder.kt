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
import dev.catbit.mosaic.server.builder.tile.builders.progress.LinearProgressIndicator

object LinearProgressIndicatorTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "LinearProgressIndicator"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Progress",
                description = "Barra de progresso Material 3 de largura total — determinada (0.0–1.0) ou indeterminada (progress = null)."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Mesma semântica de CircularProgressIndicator, só que em formato de barra — ideal " +
                    "pra downloads, envios de formulário ou operações com várias etapas visíveis lado a lado."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("progress", "Float?", "null = indeterminado; 0.0–1.0 = determinado."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                LinearProgressIndicator(
                    id = "downloadBar",
                    progress = 0.45f,
                    style = { size(width = fillHorizontally(), height = wrapVertically()) }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Cada botão dispara um UpdateTiles(\"progress\" to ...) real") {
                LinearProgressIndicator(
                    id = "linear_progress_demo",
                    progress = null,
                    style = { size(width = fillHorizontally(), height = wrapVertically()) }
                )
                Row(arrangement = arrangeHorizontallySpacedBy(8)) {
                    Button(
                        text = "Indeterminado",
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

            ShowroomRelated(
                names = listOf("CircularProgressIndicator", "PullToRefresh"),
                destination = "tileDetails"
            )
        }
    }
}
