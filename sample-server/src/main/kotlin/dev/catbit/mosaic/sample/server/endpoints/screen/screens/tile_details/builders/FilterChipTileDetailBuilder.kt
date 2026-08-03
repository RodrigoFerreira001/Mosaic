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
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.chips.FilterChip
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row

private val filters = listOf("active" to "Ativos", "archived" to "Arquivados", "starred" to "Favoritos")

object FilterChipTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "FilterChip"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Chips",
                description = "Chip Material 3 com estado selected alternável — usado para filtrar conteúdo."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "selected é totalmente controlado pelo servidor: onCheck/onUncheck/onCheckChanged " +
                    "disparam, mas o visual só muda de fato quando o servidor responde com UpdateTiles."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("text", "String", "Obrigatório."),
                    ShowroomParam("selected", "Boolean", "Padrão false. Controlado pelo servidor."),
                    ShowroomParam("leadingIcon / trailingIcon", "IconSchema?", "Opcionais — geralmente um check quando selected."),
                    ShowroomParam("variant", "Variant", "defaultFilterChip() (padrão) ou elevatedFilterChip()."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                FilterChip(
                    id = "activeFilter",
                    text = "Active",
                    selected = false,
                    leadingIcon = icon("check"),
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onCheck(),
                            updates = { update(tileId = "activeFilter", updateData = inlineTileUpdateData("selected" to true)) }
                        )
                        UpdateTiles(
                            trigger = EventTriggers.onUncheck(),
                            updates = { update(tileId = "activeFilter", updateData = inlineTileUpdateData("selected" to false)) }
                        )
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Toque em vários — todos são independentes (multi-seleção)") {
                Row(arrangement = arrangeHorizontallySpacedBy(8)) {
                    filters.forEach { (id, label) ->
                        FilterChip(
                            id = "filter_chip_demo_$id",
                            text = label,
                            selected = false,
                            leadingIcon = icon("check"),
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onCheck(),
                                    updates = {
                                        update(tileId = "filter_chip_demo_$id", updateData = inlineTileUpdateData("selected" to true))
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onUncheck(),
                                    updates = {
                                        update(tileId = "filter_chip_demo_$id", updateData = inlineTileUpdateData("selected" to false))
                                    }
                                )
                            }
                        )
                    }
                }
            }

            ShowroomRelated(
                names = listOf("AssistChip", "InputChip", "Checkbox"),
                destination = "tileDetails"
            )
        }
    }
}
