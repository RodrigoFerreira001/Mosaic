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
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton
import dev.catbit.mosaic.server.builder.tile.builders.menu.Menu

object MenuTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Menu"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Menu",
                description = "Box que sobrepõe um DropdownMenu por cima do seu conteúdo âncora — menus de ação contextual, seletores em dropdown."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "expanded é controlado pelo servidor. Quando o usuário toca fora, o renderer dispara " +
                    "um evento de toggle pedindo que o servidor feche o menu — é sua responsabilidade " +
                    "responder com expanded = false."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("expanded", "Boolean", "Padrão false. Controlado pelo servidor."),
                    ShowroomParam("items", "MenuItemSchemaBuilderScope.() -> Unit", "Obrigatório. Use addMenuItem(id, label, leadingIcon, trailingIcon)."),
                    ShowroomParam("tiles", "TileSchemaBuilderScope.() -> Unit", "Obrigatório. Conteúdo âncora (ex: um IconButton)."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                Menu(
                    id = "actionsMenu",
                    expanded = false,
                    items = {
                        addMenuItem(id = "edit", label = "Editar", leadingIcon = icon("edit"))
                        addMenuItem(id = "delete", label = "Excluir", leadingIcon = icon("delete"))
                    },
                    tiles = {
                        IconButton(
                            icon = icon("more_vert"),
                            events = {
                                UpdateTiles(trigger = EventTriggers.onClick(), updates = {
                                    update(tileId = "actionsMenu", updateData = inlineTileUpdateData("expanded" to true))
                                })
                            }
                        )
                    },
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onMenuItemClick(itemId = "edit"),
                            updates = { update(tileId = "actionsMenu", updateData = inlineTileUpdateData("expanded" to false)) }
                        )
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Toque no ícone, escolha um item — dispara evento real") {
                Menu(
                    id = "menu_demo",
                    expanded = false,
                    items = {
                        addMenuItem(id = "edit", label = "Editar", leadingIcon = icon("edit"))
                        addMenuItem(id = "share", label = "Compartilhar", leadingIcon = icon("share"))
                        addMenuItem(id = "delete", label = "Excluir", leadingIcon = icon("delete"))
                    },
                    tiles = {
                        IconButton(
                            icon = icon("more_vert"),
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onClick(),
                                    updates = { update(tileId = "menu_demo", updateData = inlineTileUpdateData("expanded" to true)) }
                                )
                            }
                        )
                    },
                    events = {
                        listOf("edit", "share", "delete").forEach { id ->
                            UpdateTiles(
                                trigger = EventTriggers.onMenuItemClick(itemId = id),
                                updates = { update(tileId = "menu_demo", updateData = inlineTileUpdateData("expanded" to false)) }
                            )
                            DisplaySnackbar(
                                trigger = EventTriggers.onMenuItemClick(itemId = id),
                                message = "Item \"$id\" clicado"
                            )
                        }
                    }
                )
            }

            ShowroomRelated(
                names = listOf("Popup", "DropdownList", "IconButton"),
                destination = "tileDetails"
            )
        }
    }
}
