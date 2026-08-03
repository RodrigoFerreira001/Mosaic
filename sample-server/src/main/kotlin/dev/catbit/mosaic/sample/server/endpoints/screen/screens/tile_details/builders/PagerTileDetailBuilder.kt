package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPageChangedEventTrigger
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
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.color.themeColorPrimaryContainer
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.mappedIncomingTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Pager
import dev.catbit.mosaic.server.builder.tile.builders.grouping.pageFill
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object PagerTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Pager"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Containers",
                description = "Pager horizontal deslizável, cada tile filho ocupa uma página inteira — onboarding, galerias, wizards."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "OnPageChanged dispara com uma Direction (Any/Start/End/Index(n)) toda vez que a " +
                    "página muda — exceto na primeira composição, pra evitar disparo espúrio."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("pageSize", "PageSizeSchema", "pageFill() (padrão) ou pageFixed(dp)."),
                    ShowroomParam("pageSpacing / contentPadding", "Int", "Padrão 0dp."),
                    ShowroomParam("beyondViewportPageCount", "Int", "Padrão 0. Páginas extras pré-compostas."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                Pager(
                    id = "onboardingPager",
                    pageSize = pageFill(),
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onPageChanged(),
                            updates = { update(tileId = "pageIndicator", updateData = incomingTileUpdateData()) }
                        )
                    }
                ) {
                    onboardingPages.forEach { page -> Column(id = "page_${'$'}{page.index}") { SimpleText(text = page.title) } }
                }
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Arraste entre as 3 páginas — o indicador abaixo reage de verdade") {
                Pager(
                    id = "pager_demo",
                    pageSize = pageFill(),
                    style = { size(width = fillHorizontally(), height = fixedVertically(100)) },
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onPageChanged(OnPageChangedEventTrigger.Direction.Any),
                            updates = {
                                update(
                                    tileId = "pager_demo_indicator",
                                    updateData = mappedIncomingTileUpdateData("text" to "Página atual: <||>")
                                )
                            }
                        )
                    }
                ) {
                    listOf("1", "2", "3").forEach { page ->
                        Box(
                            style = {
                                size(width = fillHorizontally(), height = fillVertically())
                                background(color(themeColorPrimaryContainer()))
                            }
                        ) {
                            SimpleText(text = "Página $page")
                        }
                    }
                }
                SimpleText(id = "pager_demo_indicator", text = "Página atual: 0", color = color(themeColorOnSurfaceVariant()))
            }

            ShowroomRelated(
                names = listOf("Carousel", "Tabs", "LazyRow"),
                destination = "tileDetails"
            )
        }
    }
}
