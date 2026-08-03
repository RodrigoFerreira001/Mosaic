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
import dev.catbit.mosaic.server.builder.event.builders.popup.TogglePopup
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignToBottomCenter
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Card
import dev.catbit.mosaic.server.builder.tile.builders.popup.Popup
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object PopupTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Popup"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Popup",
                description = "Overlay livre ancorado ao conteúdo — ao contrário do Menu, popupTiles é conteúdo qualquer, não uma lista fixa de itens."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use pra tooltips, dropdowns/pickers customizados ou qualquer overlay cujo conteúdo " +
                    "não seja uma lista simples (pra isso, use Menu). Assim como Menu, expanded é " +
                    "controlado pelo servidor; TogglePopup(popupId) é o evento dedicado pra fechar/abrir."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("expanded", "Boolean", "Padrão false. Controlado pelo servidor."),
                    ShowroomParam("alignment", "TwoDimensional", "alignToTopStart() (padrão). Posição relativa à âncora."),
                    ShowroomParam("offsetX / offsetY", "Int", "Padrão 0dp."),
                    ShowroomParam("dismissOnBackPress / dismissOnClickOutside", "Boolean", "Padrão true."),
                    ShowroomParam("tiles / popupTiles", "( -> Unit)", "Âncora / conteúdo do popup, ambos obrigatórios."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                Popup(
                    id = "infoPopup",
                    expanded = false,
                    alignment = alignToBottomCenter(),
                    offsetY = 8,
                    tiles = {
                        IconButton(icon = icon("info"), events = { TogglePopup(trigger = EventTriggers.onClick(), popupId = "infoPopup") })
                    },
                    popupTiles = {
                        Card { SimpleText(text = "Informação adicional") }
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Toque no ícone — o Popup abre e fecha de verdade via TogglePopup") {
                Popup(
                    id = "popup_demo",
                    expanded = false,
                    alignment = alignToBottomCenter(),
                    offsetY = 8,
                    tiles = {
                        IconButton(
                            icon = icon("info"),
                            events = {
                                TogglePopup(trigger = EventTriggers.onClick(), popupId = "popup_demo")
                            }
                        )
                    },
                    popupTiles = {
                        Card(style = { size(width = wrapHorizontally(), height = wrapVertically()) }) {
                            SimpleText(text = "Conteúdo livre dentro do Popup", style = { padding(horizontal = 16, vertical = 12) })
                        }
                    }
                )
            }

            ShowroomRelated(
                names = listOf("Menu", "Tooltip", "Card"),
                destination = "tileDetails"
            )
        }
    }
}
