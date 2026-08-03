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
import dev.catbit.mosaic.server.builder.color.themeColorOnSecondaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorSecondaryContainer
import dev.catbit.mosaic.server.builder.event.builders.scroll.pager.ScrollPager
import dev.catbit.mosaic.server.builder.event.builders.scroll.pager.scrollPageToNextPage
import dev.catbit.mosaic.server.builder.event.builders.scroll.pager.scrollPageToPreviousPage
import dev.catbit.mosaic.server.builder.placement.alignToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Pager
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.grouping.pageFill
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium

object ScrollPagerTileEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "ScrollPagerTile"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Scroll",
                description = "Navega imperativamente um Pager até uma página alvo via broadcast — troca de " +
                    "aba/página programática, sem depender de swipe."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use pra troca de página/aba controlada por botão — avançar um onboarding, voltar uma página " +
                    "num carrossel de passos. Se o tileId não corresponder a nenhum Pager na árvore, o " +
                    "broadcast é silenciosamente ignorado."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("tileId", "String", "Obrigatório. ID do Pager a navegar."),
                    ShowroomParam("where", "Where", "Obrigatório. scrollPageToBegin(), scrollPageToEnd(), scrollPageToNextPage() ou scrollPageToPreviousPage()."),
                    ShowroomParam("smoothly", "Boolean", "true (padrão). Transição animada vs instantânea."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                ScrollPager(trigger = EventTriggers.onClick(), tileId = "onboarding_pager", where = scrollPageToNextPage(), smoothly = true)
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Avance e volte páginas com botões, sem arrastar") {
                Pager(
                    id = "scroll_pager_demo",
                    pageSize = pageFill(),
                    style = { size(width = fillHorizontally(), height = fixedVertically(120)) }
                ) {
                    (1..3).forEach { pageNumber ->
                        Box(
                            alignment = alignToCenter(),
                            style = {
                                size(width = fillHorizontally(), height = fillVertically())
                                clip(roundedCornerShape(all = 16))
                                background(color(themeColorSecondaryContainer()))
                            }
                        ) {
                            SimpleText(
                                text = "Página $pageNumber",
                                typography = typographyTitleMedium(),
                                color = color(themeColorOnSecondaryContainer())
                            )
                        }
                    }
                }
                Row(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeHorizontallySpacedBy(8)
                ) {
                    Button(
                        text = "Anterior",
                        buttonType = outlinedButton(),
                        events = {
                            ScrollPager(
                                trigger = EventTriggers.onClick(),
                                tileId = "scroll_pager_demo",
                                where = scrollPageToPreviousPage(),
                                smoothly = true
                            )
                        }
                    )
                    Button(
                        text = "Próxima",
                        buttonType = outlinedButton(),
                        events = {
                            ScrollPager(
                                trigger = EventTriggers.onClick(),
                                tileId = "scroll_pager_demo",
                                where = scrollPageToNextPage(),
                                smoothly = true
                            )
                        }
                    )
                }
                ShowroomNote(
                    "scrollPageToNextPage()/scrollPageToPreviousPage() não fazem nada (silenciosamente) ao " +
                        "chegar na última/primeira página — não há wrap-around."
                )
            }

            ShowroomRelated(
                names = listOf("ScrollColumnTile", "ScrollRowTile", "Pager"),
                destination = "eventDetails"
            )
        }
    }
}
