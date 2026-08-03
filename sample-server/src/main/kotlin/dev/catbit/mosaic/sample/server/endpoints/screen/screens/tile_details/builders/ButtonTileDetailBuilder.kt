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
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.elevatedButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledTonalButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.textButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.FlowRow

object ButtonTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Button"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Buttons",
                description = "Botão Material 3 em cinco estilos visuais (buttonType), com suporte a ícone, estado de loading e forma."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use para a ação principal de uma tela, submissão de formulário ou qualquer ação " +
                    "clicável rotulada. loading = true troca texto/ícone por um spinner sem desabilitar " +
                    "o toque — só enabled = false bloqueia interação de fato."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("text", "String", "Obrigatório. Rótulo do botão."),
                    ShowroomParam("icon", "IconSchema?", "Opcional. Ícone acompanhando o texto."),
                    ShowroomParam("buttonType", "Type", "filledButton() (padrão), elevatedButton(), filledTonalButton(), outlinedButton(), textButton()."),
                    ShowroomParam("shape", "Shape", "roundedButton() (padrão) ou squareButton()."),
                    ShowroomParam("loading", "Boolean", "Padrão false. Mostra spinner no lugar do conteúdo."),
                    ShowroomParam("enabled", "Boolean", "Padrão true."),
                    ShowroomParam("iconPosition", "IconPosition", "iconAtStart() (padrão) ou iconAtEnd()."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                Button(
                    id = "loginButton",
                    text = "Entrar",
                    buttonType = filledButton(),
                    style = {
                        size(width = fillHorizontally(max = 400), height = fixedVertically(56))
                    },
                    events = {
                        TriggerEvent(eventId = "loginEvent", trigger = EventTriggers.onClick())
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Os 5 buttonType, lado a lado — clique em qualquer um") {
                FlowRow(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    horizontalArrangement = arrangeHorizontallySpacedBy(8)
                ) {
                    Button(
                        text = "Filled",
                        buttonType = filledButton(),
                        events = { buttonClickSnackbar("Filled", "filledButton()") }
                    )
                    Button(
                        text = "Elevated",
                        buttonType = elevatedButton(),
                        events = { buttonClickSnackbar("Elevated", "elevatedButton()") }
                    )
                    Button(
                        text = "Tonal",
                        buttonType = filledTonalButton(),
                        events = { buttonClickSnackbar("Tonal", "filledTonalButton()") }
                    )
                    Button(
                        text = "Outlined",
                        buttonType = outlinedButton(),
                        events = { buttonClickSnackbar("Outlined", "outlinedButton()") }
                    )
                    Button(
                        text = "Text",
                        buttonType = textButton(),
                        events = { buttonClickSnackbar("Text", "textButton()") }
                    )
                }
                Button(
                    id = "button_loading_demo",
                    text = "Clique para carregar",
                    icon = icon("hourglass_top"),
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onClick(),
                            updates = {
                                update(
                                    tileId = "button_loading_demo",
                                    updateData = inlineTileUpdateData("loading" to true)
                                )
                            }
                        )
                    }
                )
            }

            ShowroomRelated(
                names = listOf("IconButton", "FloatingActionButton", "AssistChip"),
                destination = "tileDetails"
            )
        }
    }
}

private fun dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope.buttonClickSnackbar(
    label: String,
    code: String,
) {
    DisplaySnackbar(
        trigger = EventTriggers.onClick(),
        message = "$label → buttonType = $code"
    )
}
