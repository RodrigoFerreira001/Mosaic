package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

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
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnPrimaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Card
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium

object UpdateTilesEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "UpdateTiles"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Tile Management",
                description = "Aplica patches de dados em tiles já existentes, sem alterar a estrutura da árvore " +
                    "— muda texto, cor, visibilidade, etc. sem substituir o tile."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "É o event mais usado para reagir a interações: mudar o texto de um label, o estado de um " +
                    "TextField, a visibilidade de um botão. Aceita várias atualizações em uma única chamada, cada " +
                    "uma mirando um tileId."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("updates", "UpdateTilesUpdateBuilderScope.() -> Unit", "Obrigatório. Um update(tileId, updateData) por tile alvo."),
                    ShowroomParam("updateData", "—", "inlineTileUpdateData(\"key\" to value) para valores estáticos, ou incomingTileUpdateData() para usar o incomingData do evento pai."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                UpdateTiles(
                    trigger = EventTriggers.onSuccess(),
                    updates = {
                        update(
                            tileId = "result_count",
                            updateData = inlineTileUpdateData("text" to "${'$'}count results")
                        )
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Clique no card para atualizar seu texto e cor") {
                Card(
                    id = "update_tiles_card",
                    style = {
                        size(width = fillHorizontally(), height = wrapVertically())
                        clip(roundedCornerShape(all = 16))
                        padding(horizontal = 16, vertical = 16)
                    },
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onClick(),
                            updates = {
                                update(
                                    tileId = "update_tiles_label",
                                    updateData = inlineTileUpdateData(
                                        "text" to "Atualizado via UpdateTiles!",
                                        "color" to color(themeColorOnPrimaryContainer())
                                    )
                                )
                                update(
                                    tileId = "update_tiles_sublabel",
                                    updateData = inlineTileUpdateData(
                                        "text" to "O tile foi só corrigido, não recriado."
                                    )
                                )
                            }
                        )
                    }
                ) {
                    Column(arrangement = arrangeVerticallySpacedBy(4)) {
                        SimpleText(
                            id = "update_tiles_label",
                            text = "Clique neste card",
                            typography = typographyTitleMedium()
                        )
                        SimpleText(
                            id = "update_tiles_sublabel",
                            text = "UpdateTiles muda texto e cor sem recriar o tile",
                            typography = typographyBodyMedium(),
                            color = color(themeColorOnSurfaceVariant())
                        )
                    }
                }
            }

            ShowroomRelated(
                names = listOf("AddTiles", "ReplaceTiles", "UpdateEvents"),
                destination = "eventDetails"
            )
        }
    }
}
