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
import dev.catbit.mosaic.server.builder.event.builders.scroll.row.ScrollRow
import dev.catbit.mosaic.server.builder.event.builders.scroll.row.scrollRowToEnd
import dev.catbit.mosaic.server.builder.event.builders.scroll.row.scrollRowToStart
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium

object ScrollRowTileEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "ScrollRowTile"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Scroll",
                description = "Rola imperativamente um Row ou LazyRow até uma posição via broadcast — controle " +
                    "de navegação horizontal e carrosséis."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use pra navegação em listas horizontais — chips de filtro, carrosséis de cards — quando um " +
                    "botão externo (não um swipe) deve mover o scroll. Se o tileId não corresponder a nenhum " +
                    "Row/LazyRow na árvore, o broadcast é silenciosamente ignorado."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("tileId", "String", "Obrigatório. ID do Row/LazyRow a rolar."),
                    ShowroomParam("where", "Where", "Obrigatório. scrollRowToStart(), scrollRowTo(index) ou scrollRowToEnd()."),
                    ShowroomParam("smoothly", "Boolean", "true (padrão). Rolagem animada vs instantânea."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                ScrollRow(trigger = EventTriggers.onClick(), tileId = "chip_row", where = scrollRowToStart(), smoothly = false)
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Role uma linha horizontal até o início e até o fim") {
                Row(
                    id = "scroll_row_demo_list",
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeHorizontallySpacedBy(8),
                    scrollable = true
                ) {
                    (1..15).forEach { index ->
                        Column(
                            style = {
                                size(width = fixedHorizontally(90), height = wrapVertically())
                                padding(horizontal = 12, vertical = 16)
                                clip(roundedCornerShape(all = 12))
                                background(color(themeColorSurfaceContainer()))
                            }
                        ) {
                            SimpleText(
                                text = "Chip $index",
                                typography = typographyBodyMedium(),
                                color = color(themeColorOnSurfaceVariant())
                            )
                        }
                    }
                }
                Row(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeHorizontallySpacedBy(8)
                ) {
                    Button(
                        text = "Rolar ao início",
                        buttonType = outlinedButton(),
                        events = {
                            ScrollRow(
                                trigger = EventTriggers.onClick(),
                                tileId = "scroll_row_demo_list",
                                where = scrollRowToStart(),
                                smoothly = true
                            )
                        }
                    )
                    Button(
                        text = "Rolar ao fim",
                        buttonType = outlinedButton(),
                        events = {
                            ScrollRow(
                                trigger = EventTriggers.onClick(),
                                tileId = "scroll_row_demo_list",
                                where = scrollRowToEnd(),
                                smoothly = true
                            )
                        }
                    )
                }
            }

            ShowroomRelated(
                names = listOf("ScrollColumnTile", "ScrollPagerTile"),
                destination = "eventDetails"
            )
        }
    }
}
