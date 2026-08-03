package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.extensions.randomId
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
import dev.catbit.mosaic.server.builder.color.themeColorTertiaryContainer
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.FlexBox
import dev.catbit.mosaic.server.builder.tile.builders.grouping.flexJustifySpaceEvenly
import dev.catbit.mosaic.server.builder.tile.builders.grouping.flexWrap
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object FlexBoxTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "FlexBox"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Containers",
                description = "Container estilo CSS flexbox — direção, justify/align e wrap opcional, igual ao flexbox da web."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Mais flexível que Row/Column puros quando você precisa de wrap + justify/align " +
                    "combinados. Expõe LocalFlexBoxScope pra flexGrow/flexShrink/alignSelf nos filhos."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("direction", "FlexDirectionSchema", "flexDirectionRow() (padrão), Column, RowReverse, ColumnReverse."),
                    ShowroomParam("justifyContent", "FlexJustifyContentSchema", "flexJustifyStart() (padrão), Center, SpaceBetween, SpaceEvenly..."),
                    ShowroomParam("alignItems", "FlexAlignItemsSchema", "flexAlignItemsStart() (padrão), Center, Stretch, Baseline..."),
                    ShowroomParam("wrap", "FlexWrapSchema", "flexNoWrap() (padrão), flexWrap(), flexWrapReverse()."),
                    ShowroomParam("columnGap / rowGap", "Int", "Padrão 0dp."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                FlexBox(
                    id = "tagCloud",
                    direction = flexDirectionRow(),
                    wrap = flexWrap(),
                    columnGap = 8,
                    rowGap = 8,
                    justifyContent = flexJustifyStart()
                ) {
                    tags.forEach { tag -> AssistChip(id = "tag_${'$'}{tag.id}", text = tag.name) }
                }
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "8 blocos com wrap + espaçamento uniforme entre eles") {
                FlexBox(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    wrap = flexWrap(),
                    justifyContent = flexJustifySpaceEvenly(),
                    columnGap = 8,
                    rowGap = 8
                ) {
                    repeat(8) { i ->
                        Box(
                            id = randomId(),
                            style = {
                                size(width = fixedHorizontally(72), height = fixedVertically(48))
                                clip(roundedCornerShape(all = 8))
                                background(color(themeColorTertiaryContainer()))
                            }
                        ) {
                            SimpleText(text = "${i + 1}")
                        }
                    }
                }
            }

            ShowroomRelated(
                names = listOf("FlowRow", "Grid", "Row"),
                destination = "tileDetails"
            )
        }
    }
}
