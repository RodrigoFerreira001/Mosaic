//package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders
//
//import dev.catbit.mosaic.core.extensions.randomId
//import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
//import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
//import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
//import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParagraph
//import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParam
//import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParamsTable
//import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
//import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
//import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
//import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
//import dev.catbit.mosaic.server.builder.color.color
//import dev.catbit.mosaic.server.builder.color.themeColorPrimaryContainer
//import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
//import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
//import dev.catbit.mosaic.server.builder.tile.builders.grouping.Grid
//import dev.catbit.mosaic.server.builder.tile.builders.grouping.gridColumnFraction
//import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
//
//object GridTileDetailBuilder : TileDetailBuilder {
//
//    override fun canBuild(tileName: String) = tileName == "Grid"
//
//    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
//        ShowroomScaffold {
//            ShowroomHero(
//                category = "Containers",
//                description = "Layout bidimensional estilo CSS grid — colunas e linhas definidas como listas de tracks tipados."
//            )
//
//            ShowroomSectionTitle("Visão geral")
//            ShowroomParagraph(
//                "Use pra layouts 2D de verdade — grades de imagens, dashboards, formulários complexos. " +
//                    "Filhos são compostos de forma eager (não lazy). Expõe LocalGridScope pra modifiers " +
//                    "columnSpan/rowSpan."
//            )
//
//            ShowroomSectionTitle("Parâmetros")
//            ShowroomParamsTable(
//                listOf(
//                    ShowroomParam("columns", "List<GridTrackSchema>", "Obrigatório. gridColumnFixed/Fraction/Flexible/Auto/MaxContent/MinContent."),
//                    ShowroomParam("rows", "List<GridTrackSchema>", "Padrão []. Mesmos helpers, prefixo gridRow."),
//                    ShowroomParam("columnGap / rowGap", "Int", "Padrão 0dp."),
//                    ShowroomParam("flow", "GridFlowSchema", "flowGridThroughRows() (padrão) ou flowGridThroughColumns()."),
//                )
//            )
//
//            ShowroomSectionTitle("Exemplo de código")
//            ShowroomCode(
//                """
//                Grid(
//                    id = "dashboardGrid",
//                    columns = listOf(gridColumnFraction(0.5f), gridColumnFraction(0.5f)),
//                    columnGap = 8,
//                    rowGap = 8
//                ) {
//                    metrics.forEach { m -> Card(id = "metric_${'$'}{m.id}") { SimpleText(text = m.label) } }
//                }
//                """
//            )
//
//            ShowroomSectionTitle("Demo interativa")
//            ShowroomDemoCard(title = "Grade 3 colunas × 2 linhas, 6 células reais") {
//                Grid(
//                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
//                    columns = listOf(gridColumnFraction(1f / 3), gridColumnFraction(1f / 3), gridColumnFraction(1f / 3)),
//                    columnGap = 8,
//                    rowGap = 8,
//                    rows =
//                ) {
//                    repeat(6) { i ->
//                        Box(
//                            id = randomId(),
//                            style = {
//                                size(width = fillHorizontally(), height = fixedVertically(56))
//                                clip(roundedCornerShape(all = 12))
//                                background(color(themeColorPrimaryContainer()))
//                            }
//                        ) {
//                            SimpleText(text = "${i + 1}")
//                        }
//                    }
//                }
//            }
//
//            ShowroomRelated(
//                names = listOf("FlexBox", "FlowRow", "Column"),
//                destination = "tileDetails"
//            )
//        }
//    }
//}
