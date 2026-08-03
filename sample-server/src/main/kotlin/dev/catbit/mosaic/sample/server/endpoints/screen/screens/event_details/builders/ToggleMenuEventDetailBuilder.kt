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
import dev.catbit.mosaic.server.builder.event.builders.event.RunEvents
import dev.catbit.mosaic.server.builder.event.builders.menu.ToggleMenu
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton
import dev.catbit.mosaic.server.builder.tile.builders.menu.Menu

object ToggleMenuEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "ToggleMenu"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Menu",
                description = "Alterna (abre/fecha) o estado de um MenuTile identificado por menuId — se estiver aberto fecha, se estiver fechado abre."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "ToggleMenu não abre nem fecha explicitamente: ele inverte o estado atual do MenuTile alvo. " +
                    "O padrão mais comum é usar o mesmo ToggleMenu tanto no botão que abre o menu quanto em cada " +
                    "item de menu (para fechar depois de uma escolha)."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("menuId", "String", "Obrigatório. id do MenuTile a alternar."),
                    ShowroomParam("trigger", "EventTrigger", "Obrigatório. Ex.: onClick() no botão que abre/fecha."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                Menu(
                    id = "actions_menu",
                    items = {
                        addMenuItem(id = "actions_menu_edit", label = "Editar", leadingIcon = icon("edit"))
                        addMenuItem(id = "actions_menu_delete", label = "Excluir", leadingIcon = icon("delete"))
                    },
                    events = {
                        RunEvents(
                            trigger = EventTriggers.onMenuItemClick("actions_menu_edit"),
                            events = {
                                ToggleMenu(trigger = EventTriggers.inline(), menuId = "actions_menu")
                                // ação de editar aqui
                            }
                        )
                    }
                ) {
                    IconButton(
                        icon = icon("more_vert"),
                        events = {
                            ToggleMenu(trigger = EventTriggers.onClick(), menuId = "actions_menu")
                        }
                    )
                }
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Clique no botão para abrir o menu, escolha um item") {
                Menu(
                    id = "toggle_menu_demo",
                    items = {
                        addMenuItem(id = "toggle_menu_demo_share", label = "Compartilhar", leadingIcon = icon("share"))
                        addMenuItem(id = "toggle_menu_demo_favorite", label = "Favoritar", leadingIcon = icon("star"))
                        addMenuItem(id = "toggle_menu_demo_delete", label = "Excluir", leadingIcon = icon("delete"))
                    },
                    events = {
                        RunEvents(
                            trigger = EventTriggers.onMenuItemClick("toggle_menu_demo_share"),
                            events = {
                                ToggleMenu(trigger = EventTriggers.inline(), menuId = "toggle_menu_demo")
                                DisplaySnackbar(trigger = EventTriggers.inline(), message = "Compartilhar selecionado")
                            }
                        )
                        RunEvents(
                            trigger = EventTriggers.onMenuItemClick("toggle_menu_demo_favorite"),
                            events = {
                                ToggleMenu(trigger = EventTriggers.inline(), menuId = "toggle_menu_demo")
                                DisplaySnackbar(trigger = EventTriggers.inline(), message = "Favoritar selecionado")
                            }
                        )
                        RunEvents(
                            trigger = EventTriggers.onMenuItemClick("toggle_menu_demo_delete"),
                            events = {
                                ToggleMenu(trigger = EventTriggers.inline(), menuId = "toggle_menu_demo")
                                DisplaySnackbar(trigger = EventTriggers.inline(), message = "Excluir selecionado")
                            }
                        )
                    }
                ) {
                    IconButton(
                        icon = icon("more_vert"),
                        events = {
                            ToggleMenu(
                                trigger = EventTriggers.onClick(),
                                menuId = "toggle_menu_demo"
                            )
                        }
                    )
                }
            }

            ShowroomNote(
                "Sempre feche o menu (ToggleMenu de novo) ao reagir a um addMenuItem — senão ele permanece " +
                    "aberto depois da escolha."
            )

            ShowroomRelated(
                names = listOf("TogglePopup", "TriggerEvent", "RunEvents"),
                destination = "eventDetails"
            )
        }
    }
}
