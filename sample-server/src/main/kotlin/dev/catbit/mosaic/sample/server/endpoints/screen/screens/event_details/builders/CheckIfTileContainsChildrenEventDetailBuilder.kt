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
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.event.builders.tiles.AddTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.CheckIfTileContainsChildren
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

object CheckIfTileContainsChildrenEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "CheckIfTileContainsChildren"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Tile Management",
                description = "Verifica de forma síncrona se um container possui todos os filhos indicados — " +
                    "útil pra evitar duplicar um tile que já foi adicionado."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use quando a lógica precisa se ramificar dependendo se certos ids já estão presentes num " +
                    "container — por exemplo, antes de adicionar um item \"favoritado\", verificar se ele já " +
                    "não está na lista de favoritos. A checagem olha só os filhos diretos, não descendentes " +
                    "mais profundos, e uma lista vazia de childrenIds sempre resulta em sucesso."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("groupingTileId", "String", "Obrigatório. ID do container a inspecionar."),
                    ShowroomParam("childrenIds", "List<String>", "Obrigatório. IDs a verificar; lista vazia sempre resulta em sucesso."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                CheckIfTileContainsChildren(
                    trigger = EventTriggers.onClick(),
                    groupingTileId = "pinned_list",
                    childrenIds = listOf("item_42"),
                    events = {
                        // onSuccess = já está fixado; onFailure = ainda não, adiciona
                        AddTiles(trigger = EventTriggers.onFailure(), groupingTileId = "pinned_list", tiles = { /* ... */ })
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Clique duas vezes: a segunda vez detecta a duplicata") {
                Column(
                    id = "check_children_list",
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeVerticallySpacedBy(8)
                ) {
                    CheckChildrenRow(id = "check_children_seed", label = "Item semente (sempre presente)")
                }
                Button(
                    text = "Fixar \"item_42\" se ainda não estiver na lista",
                    buttonType = filledTonalButton(),
                    events = {
                        CheckIfTileContainsChildren(
                            trigger = EventTriggers.onClick(),
                            groupingTileId = "check_children_list",
                            childrenIds = listOf("check_children_item_42"),
                            events = {
                                DisplaySnackbar(
                                    trigger = EventTriggers.onSuccess(),
                                    message = "item_42 já estava na lista — nada foi adicionado"
                                )
                                AddTiles(
                                    trigger = EventTriggers.onFailure(),
                                    groupingTileId = "check_children_list",
                                    position = insertAtEnd(),
                                    tiles = {
                                        CheckChildrenRow(id = "check_children_item_42", label = "item_42 (recém fixado)")
                                    }
                                )
                            }
                        )
                    }
                )
                ShowroomNote(
                    "Na primeira vez, item_42 não existe: CheckIfTileContainsChildren falha e o AddTiles do " +
                        "onFailure o insere. Na segunda vez, ele já é filho direto do container: a checagem " +
                        "sucede e apenas o snackbar do onSuccess dispara — nada é duplicado."
                )
            }

            ShowroomRelated(
                names = listOf("AddTiles", "GetTileChildrenCount", "RemoveTiles"),
                destination = "eventDetails"
            )
        }
    }
}

private fun TileSchemaBuilderScope.CheckChildrenRow(id: String, label: String) {
    Row(
        id = id,
        style = {
            size(width = fillHorizontally(), height = wrapVertically())
            padding(horizontal = 12, vertical = 10)
            clip(roundedCornerShape(all = 12))
            background(color(themeColorSurfaceContainer()))
        },
        arrangement = arrangeHorizontallySpacedBy(8),
        alignment = alignVerticallyToCenter()
    ) {
        Icon(icon = icon("push_pin", size = 18, color = color(themeColorOnSurfaceVariant())))
        SimpleText(text = label, typography = typographyBodyMedium())
    }
}
