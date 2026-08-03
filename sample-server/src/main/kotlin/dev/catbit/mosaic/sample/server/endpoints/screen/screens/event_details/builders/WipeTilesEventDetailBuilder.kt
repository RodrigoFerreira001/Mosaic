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
import dev.catbit.mosaic.server.builder.event.builders.tiles.WipeTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.insertAtEnd
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

object WipeTilesEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "WipeTiles"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Tile Management",
                description = "Remove todos os filhos de um container numa única operação, deixando-o vazio — " +
                    "mais direto que RemoveTiles quando o objetivo é limpar tudo."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use quando o objetivo é esvaziar um container por completo, sem precisar enumerar os ids " +
                    "dos filhos (o que RemoveTiles exigiria). Esvaziar um container já vazio é um no-op. " +
                    "Depois de um WipeTiles, use AddTiles ou ReplaceTiles para repopular o container."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("groupingTileId", "String", "Obrigatório. ID do container a esvaziar."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                WipeTiles(
                    trigger = EventTriggers.onClick(),
                    groupingTileId = "search_results"
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Esvazie a lista e reponha os itens") {
                Column(
                    id = "wipe_tiles_list",
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeVerticallySpacedBy(8)
                ) {
                    WipeTilesListItem(label = "Item 1")
                    WipeTilesListItem(label = "Item 2")
                    WipeTilesListItem(label = "Item 3")
                }
                Row(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeHorizontallySpacedBy(8)
                ) {
                    Button(
                        text = "Esvaziar com WipeTiles",
                        buttonType = outlinedButton(),
                        events = {
                            WipeTiles(
                                trigger = EventTriggers.onClick(),
                                groupingTileId = "wipe_tiles_list"
                            )
                        }
                    )
                    Button(
                        text = "Repor com AddTiles",
                        events = {
                            AddTiles(
                                trigger = EventTriggers.onClick(),
                                groupingTileId = "wipe_tiles_list",
                                position = insertAtEnd(),
                                tiles = {
                                    WipeTilesListItem(label = "Item reposto")
                                }
                            )
                        }
                    )
                }
                ShowroomNote(
                    "Depois de esvaziar, a lista fica vazia até que outro evento (AddTiles ou ReplaceTiles) " +
                        "a repopule — WipeTiles não tem posição nem conteúdo para repor sozinho."
                )
            }

            ShowroomRelated(
                names = listOf("RemoveTiles", "ReplaceTiles", "AddTiles"),
                destination = "eventDetails"
            )
        }
    }
}

private fun TileSchemaBuilderScope.WipeTilesListItem(label: String) {
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
