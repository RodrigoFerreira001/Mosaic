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
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.chips.FilterChip
import dev.catbit.mosaic.server.builder.tile.builders.grouping.FlowRow

private val labels = listOf("Kotlin", "Compose", "Ktor", "SDUI", "Multiplatform", "Material 3", "Coroutines", "Serialization")

object FlowRowTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "FlowRow"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Containers",
                description = "Linha que quebra automaticamente pra próxima linha quando o conteúdo excede a largura — grupos de chips, nuvens de tags."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Mais simples que FlexBox quando você só precisa de wrap horizontal padrão, sem " +
                    "controle de direção/align. maxItemsInEachRow limita quantos itens cabem por linha."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("horizontalArrangement", "Horizontal", "arrangeHorizontallyToStart() (padrão)."),
                    ShowroomParam("verticalArrangement", "Vertical", "arrangeVerticallyToTop() (padrão)."),
                    ShowroomParam("maxItemsInEachRow", "Int", "Padrão Int.MAX_VALUE (sem limite)."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                FlowRow(
                    id = "filterChips",
                    horizontalArrangement = arrangeHorizontallySpacedBy(8),
                    verticalArrangement = arrangeVerticallySpacedBy(8)
                ) {
                    filters.forEach { f -> FilterChip(id = "filter_${'$'}{f.id}", text = f.label, selected = f.isActive) }
                }
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "8 FilterChip quebrando linha de verdade — todos clicáveis") {
                FlowRow(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    horizontalArrangement = arrangeHorizontallySpacedBy(8),
                    verticalArrangement = arrangeVerticallySpacedBy(8)
                ) {
                    labels.forEach { label ->
                        FilterChip(
                            id = "flow_row_demo_$label",
                            text = label,
                            selected = false,
                            leadingIcon = icon("check"),
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onCheck(),
                                    updates = { update(tileId = "flow_row_demo_$label", updateData = inlineTileUpdateData("selected" to true)) }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onUncheck(),
                                    updates = { update(tileId = "flow_row_demo_$label", updateData = inlineTileUpdateData("selected" to false)) }
                                )
                            }
                        )
                    }
                }
            }

            ShowroomRelated(
                names = listOf("Row", "FlexBox", "FilterChip"),
                destination = "tileDetails"
            )
        }
    }
}
