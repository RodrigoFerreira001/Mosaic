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
import dev.catbit.mosaic.server.builder.event.builders.tiles.AddTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.insertAtEnd
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledTonalButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium

object AddTilesEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "AddTiles"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Tile Management",
                description = "Insere um ou mais tiles novos na lista de filhos de um container, numa posição " +
                    "específica — sem recarregar a tela inteira."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use para acrescentar dinamicamente itens a um container já renderizado: um novo card numa " +
                    "lista após o usuário confirmar uma ação, um item ao final de um carrinho, etc. A posição de " +
                    "inserção é controlada pelo parâmetro position."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("groupingTileId", "String", "Obrigatório. ID do container (Column, Row, LazyColumn etc.) alvo."),
                    ShowroomParam("tiles", "TileSchemaBuilderScope.() -> Unit", "Obrigatório. Os novos tiles a inserir."),
                    ShowroomParam("position", "InsertionPosition", "insertAtEnd() (padrão), insertAtStart(), insertBeforeTile(id), insertAfterTile(id), insertAtIndex(n)."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                AddTiles(
                    trigger = EventTriggers.onClick(),
                    groupingTileId = "list_container",
                    position = insertAtEnd(),
                    tiles = {
                        SimpleText(id = randomId(), text = "New item")
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Clique para adicionar um item novo à lista") {
                Column(
                    id = "add_tiles_list",
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeVerticallySpacedBy(8)
                ) {
                    AddTilesListItem(label = "Item 1")
                    AddTilesListItem(label = "Item 2")
                }
                Button(
                    text = "Adicionar item",
                    icon = icon("add"),
                    buttonType = filledTonalButton(),
                    events = {
                        AddTiles(
                            trigger = EventTriggers.onClick(),
                            groupingTileId = "add_tiles_list",
                            position = insertAtEnd(),
                            tiles = {
                                AddTilesListItem(label = "Item novo")
                            }
                        )
                    }
                )
                ShowroomNote(
                    "Nesta demo estática o servidor descreve uma árvore fixa, então o item inserido tem sempre " +
                        "o mesmo id — clicar várias vezes duplica o id. Em produção, gere um id único por item " +
                        "(ex: randomId() dentro de um loop sobre dados vindos do banco) a cada execução real do evento."
                )
            }

            ShowroomRelated(
                names = listOf("RemoveTiles", "ReplaceTiles", "WipeTiles"),
                destination = "eventDetails"
            )
        }
    }
}

private fun TileSchemaBuilderScope.AddTilesListItem(label: String) {
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
        Icon(icon = icon("check_circle", size = 18, color = color(themeColorOnSurfaceVariant())))
        SimpleText(text = label, typography = typographyBodyMedium())
    }
}
