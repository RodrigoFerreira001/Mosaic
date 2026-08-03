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
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorSecondaryContainer
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.LazyRow
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object LazyRowTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "LazyRow"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Containers",
                description = "Lista horizontal preguiçosa — carrosséis de itens de texto, chips de categoria, tiras de miniaturas."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Mesmo mecanismo de paginação de LazyColumn, só que no eixo horizontal: scrollThreshold " +
                    "+ OnScrollThresholdReached."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("scrollThreshold", "Int?", "Itens do fim pra disparar OnScrollThresholdReached."),
                    ShowroomParam("considerLoadingItemAtEndOnThresholdReached", "Boolean", "Padrão true."),
                    ShowroomParam("arrangement / alignment", "Horizontal / Vertical", "Mesmos padrões de Row."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                LazyRow(
                    id = "categoryRow",
                    arrangement = arrangeHorizontallySpacedBy(8)
                ) {
                    categories.forEach { cat ->
                        FilterChip(id = "cat_${'$'}{cat.id}", text = cat.name, selected = cat.isSelected)
                    }
                }
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Tira horizontal de 10 cartões — role pro lado") {
                LazyRow(
                    style = { size(width = fillHorizontally(), height = fixedVertically(72)) },
                    arrangement = arrangeHorizontallySpacedBy(8)
                ) {
                    repeat(10) { i ->
                        Box(
                            style = {
                                size(width = fixedHorizontally(72), height = fixedVertically(72))
                                clip(roundedCornerShape(all = 12))
                                background(color(themeColorSecondaryContainer()))
                            }
                        ) {
                            SimpleText(text = "${i + 1}")
                        }
                    }
                }
            }

            ShowroomRelated(
                names = listOf("LazyColumn", "Row", "Carousel"),
                destination = "tileDetails"
            )
        }
    }
}
