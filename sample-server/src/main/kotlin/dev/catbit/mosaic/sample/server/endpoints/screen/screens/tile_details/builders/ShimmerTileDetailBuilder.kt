package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

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
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainerHighest
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Shimmer

object ShimmerTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Shimmer"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Containers",
                description = "Efeito de loading (shimmer) sobre os tiles filhos — telas esqueleto enquanto dados reais carregam."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Fica animado o tempo todo enquanto visível — não tem um estado \"parar\" próprio. " +
                    "Pra remover, troque ou esconda via UpdateTiles/ReplaceTiles quando os dados reais " +
                    "chegarem."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("tiles", "TileSchemaBuilderScope.() -> Unit", "Obrigatório. As formas placeholder que recebem o brilho animado."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                Shimmer(id = "listShimmer") {
                    repeat(5) { i ->
                        Row(id = "shimmer_row_${'$'}i") {
                            Box(id = "shimmer_avatar_${'$'}i", style = { size(fixedHorizontally(40), fixedVertically(40)) }) {}
                            Column(id = "shimmer_text_${'$'}i") {
                                Box(id = "shimmer_title_${'$'}i", style = { size(fixedHorizontally(120), fixedVertically(16)) }) {}
                            }
                        }
                    }
                }
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Skeleton de lista animado ao vivo") {
                Shimmer {
                    Column(arrangement = arrangeVerticallySpacedBy(12)) {
                        repeat(3) {
                            Row(arrangement = arrangeHorizontallySpacedBy(12)) {
                                Box(
                                    style = {
                                        size(width = fixedHorizontally(40), height = fixedVertically(40))
                                        clip(roundedCornerShape(all = 20))
                                        background(color(themeColorSurfaceContainerHighest()))
                                    }
                                ) {}
                                Column(arrangement = arrangeVerticallySpacedBy(6)) {
                                    Box(
                                        style = {
                                            size(width = fixedHorizontally(140), height = fixedVertically(14))
                                            clip(roundedCornerShape(all = 4))
                                            background(color(themeColorSurfaceContainerHighest()))
                                        }
                                    ) {}
                                    Box(
                                        style = {
                                            size(width = fixedHorizontally(90), height = fixedVertically(12))
                                            clip(roundedCornerShape(all = 4))
                                            background(color(themeColorSurfaceContainerHighest()))
                                        }
                                    ) {}
                                }
                            }
                        }
                    }
                }
            }

            ShowroomNote("Troque o Shimmer pelo conteúdo real via ReplaceTiles assim que os dados chegarem — ver LazyTiles e PullToRefresh para exemplos de fluxo de loading completo.")

            ShowroomRelated(
                names = listOf("LazyTiles", "PullToRefresh", "CircularProgressIndicator"),
                destination = "tileDetails"
            )
        }
    }
}
