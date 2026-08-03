package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

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
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.tooltip.Tooltip
import dev.catbit.mosaic.server.builder.tile.builders.tooltip.tooltipPositionAbove
import dev.catbit.mosaic.server.builder.tile.builders.tooltip.tooltipPositionBelow
import dev.catbit.mosaic.server.builder.tile.builders.tooltip.tooltipPositionEnd
import dev.catbit.mosaic.server.builder.tile.builders.tooltip.tooltipPositionStart

object TooltipTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Tooltip"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Tooltip",
                description = "Envolve um tile âncora e mostra um texto ao segurar/passar o mouse — o gesto é tratado inteiramente pelo client, sem estado no servidor."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Diferente de Menu/Popup, Tooltip não tem expanded controlado pelo servidor — é " +
                    "puramente client-side. text, position e aparência (shape/cores) são fixos no schema, " +
                    "não reativos a eventos."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("text", "String", "Obrigatório. Texto exibido no tooltip."),
                    ShowroomParam("position", "Position", "tooltipPositionAbove() (padrão), Below, Left, Right, Start, End."),
                    ShowroomParam("spacing / maxWidth", "Int?", "Opcionais."),
                    ShowroomParam("showCaret", "Boolean", "Padrão false. Seta apontando pra âncora."),
                    ShowroomParam("contentColor / containerColor", "ColorSchema?", "Cores customizadas do tooltip."),
                    ShowroomParam("tiles", "TileSchemaBuilderScope.() -> Unit", "Obrigatório. O tile âncora envolvido."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                Tooltip(
                    text = "Iniciar timer",
                    position = tooltipPositionBelow(),
                    showCaret = true
                ) {
                    IconButton(icon = icon("timer_play"), events = { RunCancellableEvents(trigger = EventTriggers.onClick(), cancellableEventId = "timer") })
                }
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Segure/passe o mouse em cada ícone — 4 posições diferentes") {
                Row(arrangement = arrangeHorizontallySpacedBy(16)) {
                    Tooltip(text = "Acima", position = tooltipPositionAbove(), showCaret = true) {
                        IconButton(icon = icon("arrow_upward"))
                    }
                    Tooltip(text = "Abaixo", position = tooltipPositionBelow(), showCaret = true) {
                        IconButton(icon = icon("arrow_downward"))
                    }
                    Tooltip(text = "Início", position = tooltipPositionStart(), showCaret = true) {
                        IconButton(icon = icon("arrow_back"))
                    }
                    Tooltip(text = "Fim", position = tooltipPositionEnd(), showCaret = true) {
                        IconButton(icon = icon("arrow_forward"))
                    }
                }
            }

            ShowroomRelated(
                names = listOf("IconButton", "Popup", "Menu"),
                destination = "tileDetails"
            )
        }
    }
}
