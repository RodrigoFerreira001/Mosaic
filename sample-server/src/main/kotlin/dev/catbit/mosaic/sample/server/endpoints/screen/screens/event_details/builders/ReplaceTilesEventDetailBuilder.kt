package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParagraph
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParam
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParamsTable
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainer
import dev.catbit.mosaic.server.builder.event.builders.tiles.ReplaceTiles
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium

object ReplaceTilesEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "ReplaceTiles"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Tile Management",
                description = "Substitui atomicamente toda a lista de filhos de um container por um novo conjunto " +
                    "— um wipe-then-add em uma única operação."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use quando o conteúdo inteiro de um container precisa mudar de uma vez — trocar o conteúdo de " +
                    "uma aba, recarregar uma lista com dados novos vindos do servidor. Diferente de AddTiles, não " +
                    "existe posição: os novos tiles sempre substituem completamente a lista anterior."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("groupingTileId", "String", "Obrigatório. ID do container alvo."),
                    ShowroomParam("tiles", "TileSchemaBuilderScope.() -> Unit", "Obrigatório. Nova lista de tiles; lista vazia = esvaziar."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                ReplaceTiles(
                    trigger = EventTriggers.onSuccess(),
                    groupingTileId = "tab_content",
                    tiles = {
                        items.forEach { item ->
                            Card(id = item.id) { /* ... */ }
                        }
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Troque o conteúdo inteiro do container abaixo") {
                Row(arrangement = arrangeHorizontallySpacedBy(8)) {
                    Button(
                        text = "Conjunto A",
                        buttonType = outlinedButton(),
                        events = {
                            ReplaceTiles(
                                trigger = EventTriggers.onClick(),
                                groupingTileId = "replace_tiles_container",
                                tiles = {
                                    ReplaceTilesRow(icon = "looks_one", label = "Conteúdo A — item 1")
                                    ReplaceTilesRow(icon = "looks_two", label = "Conteúdo A — item 2")
                                }
                            )
                        }
                    )
                    Button(
                        text = "Conjunto B",
                        buttonType = outlinedButton(),
                        events = {
                            ReplaceTiles(
                                trigger = EventTriggers.onClick(),
                                groupingTileId = "replace_tiles_container",
                                tiles = {
                                    ReplaceTilesRow(icon = "star", label = "Conteúdo B — destaque único")
                                }
                            )
                        }
                    )
                }
                Column(
                    id = "replace_tiles_container",
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeVerticallySpacedBy(8)
                ) {
                    ReplaceTilesRow(icon = "looks_one", label = "Conteúdo A — item 1")
                    ReplaceTilesRow(icon = "looks_two", label = "Conteúdo A — item 2")
                }
                ShowroomNote("Cada clique substitui 100% dos filhos de replace_tiles_container — não há posição a configurar, como haveria em AddTiles.")
            }

            ShowroomRelated(
                names = listOf("AddTiles", "WipeTiles", "ReloadLazyTiles"),
                destination = "eventDetails"
            )
        }
    }
}

private fun TileSchemaBuilderScope.ReplaceTilesRow(icon: String, label: String) {
    Row(
        style = {
            size(width = fillHorizontally(), height = wrapVertically())
            padding(horizontal = 12, vertical = 10)
            clip(roundedCornerShape(all = 12))
            background(color(themeColorSurfaceContainer()))
        },
        arrangement = arrangeHorizontallySpacedBy(8),
        alignment = alignVerticallyToCenter()
    ) {
        Icon(icon = icon(icon, size = 18, color = color(themeColorOnSurfaceVariant())))
        SimpleText(text = label, typography = typographyBodyMedium())
    }
}
