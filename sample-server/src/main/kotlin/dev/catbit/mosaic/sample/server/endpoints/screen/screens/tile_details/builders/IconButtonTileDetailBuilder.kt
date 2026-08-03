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
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.defaultIconButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledIconButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledTonalIconButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedIconButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object IconButtonTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "IconButton"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Buttons",
                description = "Botão Material 3 sem rótulo, só ícone — para ações de toolbar e controles compactos (voltar, buscar, fechar)."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Igual ao Button, mas sem texto: só o ícone e o buttonType (visual) importam. " +
                    "Prefira IconButton quando o significado do ícone já é óbvio pelo contexto — " +
                    "senão use Button com icon + text."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("icon", "IconSchema", "Obrigatório. O ícone exibido."),
                    ShowroomParam("buttonType", "Type", "defaultIconButton() (padrão), filledIconButton(), filledTonalIconButton(), outlinedIconButton()."),
                    ShowroomParam("enabled", "Boolean", "Padrão true."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                IconButton(
                    id = "backButton",
                    icon = icon("arrow_back"),
                    buttonType = defaultIconButton(),
                    events = {
                        NavigateUp(trigger = EventTriggers.onClick())
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Os 4 buttonType incrementam o mesmo contador") {
                SimpleText(id = "icon_button_counter", text = "Toque em qualquer botão")
                Row(arrangement = arrangeHorizontallySpacedBy(8), alignment = alignVerticallyToCenter()) {
                    IconButton(
                        icon = icon("add_circle"),
                        buttonType = defaultIconButton(),
                        events = { iconButtonFeedback() }
                    )
                    IconButton(
                        icon = icon("add_circle"),
                        buttonType = filledIconButton(),
                        events = { iconButtonFeedback() }
                    )
                    IconButton(
                        icon = icon("add_circle"),
                        buttonType = filledTonalIconButton(),
                        events = { iconButtonFeedback() }
                    )
                    IconButton(
                        icon = icon("add_circle"),
                        buttonType = outlinedIconButton(),
                        events = { iconButtonFeedback() }
                    )
                }
            }

            ShowroomRelated(
                names = listOf("Button", "FloatingActionButton", "Tooltip"),
                destination = "tileDetails"
            )
        }
    }
}

private fun dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope.iconButtonFeedback() {
    UpdateTiles(
        trigger = EventTriggers.onClick(),
        updates = {
            update(
                tileId = "icon_button_counter",
                updateData = inlineTileUpdateData("text" to "UpdateTiles disparado pelo IconButton")
            )
        }
    )
}
