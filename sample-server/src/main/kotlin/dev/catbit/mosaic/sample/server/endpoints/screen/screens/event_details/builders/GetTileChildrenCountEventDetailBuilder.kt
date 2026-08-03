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
import dev.catbit.mosaic.server.builder.event.builders.data.TransformData
import dev.catbit.mosaic.server.builder.event.builders.tiles.AddTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.GetTileChildrenCount
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.incomingTileUpdateData
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
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium

object GetTileChildrenCountEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "GetTileChildrenCount"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Tile Management",
                description = "Retorna o número atual de filhos diretos de um container como Int, entregue " +
                    "via incomingData no onSuccess."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use quando a lógica de um evento depende de saber quantos itens existem numa lista — por " +
                    "exemplo, desabilitar um botão de \"finalizar\" quando o carrinho está vazio, ou mostrar " +
                    "\"3 itens selecionados\" acima de uma lista de seleção múltipla. Um container vazio " +
                    "dispara onSuccess(0) normalmente, não é uma falha."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("groupingTileId", "String", "Obrigatório. ID do container a contar."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                GetTileChildrenCount(
                    trigger = EventTriggers.onClick(),
                    groupingTileId = "selected_items",
                    events = {
                        TransformData(
                            trigger = EventTriggers.onSuccess(),
                            template = "<||>",
                            events = {
                                UpdateTiles(trigger = EventTriggers.onSuccess(), updates = {
                                    update("count_label", incomingTileUpdateData())
                                })
                            }
                        )
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Adicione itens e conte quantos existem agora") {
                Column(
                    id = "children_count_list",
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeVerticallySpacedBy(8)
                ) {
                    ChildrenCountRow("Item inicial 1")
                    ChildrenCountRow("Item inicial 2")
                }
                SimpleText(
                    id = "children_count_label",
                    text = "?",
                    typography = typographyTitleMedium()
                )
                Row(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeHorizontallySpacedBy(8)
                ) {
                    Button(
                        text = "Adicionar item",
                        events = {
                            AddTiles(
                                trigger = EventTriggers.onClick(),
                                groupingTileId = "children_count_list",
                                position = insertAtEnd(),
                                tiles = { ChildrenCountRow("Item novo") }
                            )
                        }
                    )
                    Button(
                        text = "Contar com GetTileChildrenCount",
                        buttonType = filledTonalButton(),
                        events = {
                            GetTileChildrenCount(
                                trigger = EventTriggers.onClick(),
                                groupingTileId = "children_count_list",
                                events = {
                                    TransformData(
                                        trigger = EventTriggers.onSuccess(),
                                        template = "<||> itens na lista",
                                        events = {
                                            UpdateTiles(
                                                trigger = EventTriggers.onSuccess(),
                                                updates = {
                                                    update(
                                                        tileId = "children_count_label",
                                                        updateData = incomingTileUpdateData()
                                                    )
                                                }
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    )
                }
                ShowroomNote(
                    "O TransformData acima usa o template \"<||> itens na lista\" — como o placeholder " +
                        "<||> não está sozinho na string, o Int retornado por GetTileChildrenCount é " +
                        "coagido para texto antes de virar o novo valor do tile."
                )
            }

            ShowroomRelated(
                names = listOf("CheckIfTileContainsChildren", "AddTiles", "TransformData"),
                destination = "eventDetails"
            )
        }
    }
}

private fun TileSchemaBuilderScope.ChildrenCountRow(label: String) {
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
