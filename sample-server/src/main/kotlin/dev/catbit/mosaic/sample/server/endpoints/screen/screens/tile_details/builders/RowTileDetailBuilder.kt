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
import dev.catbit.mosaic.server.builder.event.builders.navigation.Navigate
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeSpaceBetween
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object RowTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Row"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Containers",
                description = "Posiciona tiles filhos lado a lado — barras de ação, pares ícone+rótulo, linhas de formulário."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Mesma API de Column, só que no eixo horizontal: arrangement/alignment controlam o " +
                    "espaçamento e o alinhamento vertical dos filhos."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("arrangement", "Horizontal", "arrangeHorizontallyToStart() (padrão) e variantes."),
                    ShowroomParam("alignment", "Vertical", "alignVerticallyToTop() (padrão)."),
                    ShowroomParam("scrollable", "Boolean", "Padrão false."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                Row(
                    id = "actionBar",
                    arrangement = arrangeSpaceBetween(),
                    alignment = alignVerticallyToCenter()
                ) {
                    SimpleText(text = "Title")
                    IconButton(
                        id = "searchBtn",
                        icon = icon("search"),
                        events = { Navigate(trigger = EventTriggers.onClick(), navigatorId = "root", destination = "search") }
                    )
                }
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Uma barra de ação real: título + botão que navega") {
                Row(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeSpaceBetween(),
                    alignment = alignVerticallyToCenter()
                ) {
                    SimpleText(text = "Título da seção")
                    IconButton(
                        icon = icon("search"),
                        events = {
                            Navigate(trigger = EventTriggers.onClick(), navigatorId = "root", destination = "tiles")
                        }
                    )
                }
            }

            ShowroomRelated(
                names = listOf("Column", "LazyRow", "FlowRow"),
                destination = "tileDetails"
            )
        }
    }
}
