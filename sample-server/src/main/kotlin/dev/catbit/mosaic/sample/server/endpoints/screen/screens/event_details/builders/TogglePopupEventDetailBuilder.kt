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
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainerHigh
import dev.catbit.mosaic.server.builder.event.builders.popup.TogglePopup
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignToBottomCenter
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.popup.Popup
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodySmall

object TogglePopupEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "TogglePopup"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Popup",
                description = "Alterna (abre/fecha) o estado de um PopupTile identificado por popupId — ideal para tooltips ricos, mini formulários flutuantes e menus customizados."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "PopupTile renderiza dois grupos de tiles: tiles (a âncora, sempre visível) e popupTiles " +
                    "(o conteúdo flutuante, mostrado quando expanded = true). TogglePopup inverte esse estado. " +
                    "Diferente do Menu, o conteúdo do Popup é 100% livre — qualquer árvore de tiles."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("popupId", "String", "Obrigatório. id do PopupTile a alternar."),
                    ShowroomParam("trigger", "EventTrigger", "Obrigatório. Ex.: onClick() no botão âncora."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                Popup(
                    id = "info_popup",
                    alignment = alignToBottomCenter(),
                    dismissOnClickOutside = true,
                    tiles = {
                        IconButton(
                            icon = icon("info"),
                            events = {
                                TogglePopup(trigger = EventTriggers.onClick(), popupId = "info_popup")
                            }
                        )
                    },
                    popupTiles = {
                        Column(style = { padding(horizontal = 12, vertical = 8) }) {
                            SimpleText(text = "Conteúdo flutuante livre aqui")
                        }
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Clique no botão para abrir/fechar o popup") {
                Popup(
                    id = "toggle_popup_demo",
                    alignment = alignToBottomCenter(),
                    dismissOnClickOutside = true,
                    tiles = {
                        Button(
                            text = "Alternar popup",
                            icon = icon("info"),
                            events = {
                                TogglePopup(
                                    trigger = EventTriggers.onClick(),
                                    popupId = "toggle_popup_demo"
                                )
                            }
                        )
                    },
                    popupTiles = {
                        Column(
                            style = {
                                size(width = fixedHorizontally(220), height = wrapVertically())
                                padding(horizontal = 16, vertical = 12)
                                clip(roundedCornerShape(all = 12))
                                background(color(themeColorSurfaceContainerHigh()))
                            },
                            arrangement = arrangeVerticallySpacedBy(4)
                        ) {
                            SimpleText(
                                text = "Popup aberto!",
                                typography = typographyBodySmall()
                            )
                            SimpleText(
                                text = "Clique fora ou no botão de novo para fechar.",
                                typography = typographyBodySmall(),
                                color = color(themeColorOnSurfaceVariant())
                            )
                        }
                    }
                )
            }

            ShowroomNote(
                "dismissOnClickOutside = true (padrão) fecha o popup automaticamente ao tocar fora dele — " +
                    "sem precisar de outro TogglePopup para isso."
            )

            ShowroomRelated(
                names = listOf("ToggleMenu", "DisplayDialog", "DisplayBottomSheet"),
                destination = "eventDetails"
            )
        }
    }
}
