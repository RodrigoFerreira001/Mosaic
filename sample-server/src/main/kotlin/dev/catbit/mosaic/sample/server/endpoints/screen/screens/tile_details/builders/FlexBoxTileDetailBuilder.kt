package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.color.ColorSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.FlexBoxTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnPrimaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorOnSecondaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorOnTertiaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorPrimaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorSecondaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorTertiaryContainer
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.FlexBox
import dev.catbit.mosaic.server.builder.tile.builders.grouping.flexDirectionColumn
import dev.catbit.mosaic.server.builder.tile.builders.grouping.flexDirectionColumnReverse
import dev.catbit.mosaic.server.builder.tile.builders.grouping.flexDirectionRow
import dev.catbit.mosaic.server.builder.tile.builders.grouping.flexDirectionRowReverse
import dev.catbit.mosaic.server.builder.tile.builders.grouping.flexJustifyCenter
import dev.catbit.mosaic.server.builder.tile.builders.grouping.flexJustifyEnd
import dev.catbit.mosaic.server.builder.tile.builders.grouping.flexJustifySpaceAround
import dev.catbit.mosaic.server.builder.tile.builders.grouping.flexJustifySpaceBetween
import dev.catbit.mosaic.server.builder.tile.builders.grouping.flexJustifySpaceEvenly
import dev.catbit.mosaic.server.builder.tile.builders.grouping.flexJustifyStart
import dev.catbit.mosaic.server.builder.tile.builders.grouping.flexNoWrap
import dev.catbit.mosaic.server.builder.tile.builders.grouping.flexWrap
import dev.catbit.mosaic.server.builder.tile.builders.grouping.flexWrapReverse
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyLabelMedium

object FlexBoxTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "FlexBox"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A CSS-flexbox-style container — direction, justifyContent/alignItems/" +
                    "alignContent, and optional wrap, just like the web's flexbox. More flexible than plain " +
                    "Row/Column when you need wrap + justify/align combined. Children can override their own " +
                    "main-axis grow/shrink/basis and cross-axis alignSelf via flexHorizontally()/" +
                    "flexVertically() in their own size() — only meaningful inside a FlexBox ancestor."
            )

            ShowroomSectionTitle("direction — all 4 values")
            ShowroomDemoCard(title = "Same 3 chips, direction changes both axis and order") {
                Column(arrangement = arrangeVerticallySpacedBy(16)) {
                    DirectionRow("flexDirectionRow()", flexDirectionRow())
                    DirectionRow("flexDirectionRowReverse()", flexDirectionRowReverse())
                    DirectionRow("flexDirectionColumn()", flexDirectionColumn())
                    DirectionRow("flexDirectionColumnReverse()", flexDirectionColumnReverse())
                }
            }

            ShowroomSectionTitle("justifyContent — all 6 values (main axis distribution)")
            ShowroomDemoCard(title = "3 fixed-width chips, row direction") {
                Column(arrangement = arrangeVerticallySpacedBy(14)) {
                    JustifyRow("flexJustifyStart()", flexJustifyStart())
                    JustifyRow("flexJustifyCenter()", flexJustifyCenter())
                    JustifyRow("flexJustifyEnd()", flexJustifyEnd())
                    JustifyRow("flexJustifySpaceBetween()", flexJustifySpaceBetween())
                    JustifyRow("flexJustifySpaceAround()", flexJustifySpaceAround())
                    JustifyRow("flexJustifySpaceEvenly()", flexJustifySpaceEvenly())
                }
            }

            ShowroomSectionTitle("wrap — all 3 values")
            ShowroomDemoCard(title = "8 chips in a narrow container — noWrap overflows, wrap/wrapReverse break lines") {
                Column(arrangement = arrangeVerticallySpacedBy(16)) {
                    WrapDemo("flexWrap()", flexWrap())
                    WrapDemo("flexWrapReverse() — line order flips", flexWrapReverse())
                    WrapDemo("flexNoWrap() — overflows past the container edge", flexNoWrap())
                }
            }

            ShowroomSectionTitle("Real usage: flexHorizontally grow — a responsive toolbar")
            ShowroomDemoCard(title = "Title grows to fill remaining space, actions stay fixed-size") {
                FlexBox(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    direction = flexDirectionRow(),
                    columnGap = 8
                ) {
                    Box(
                        id = randomId(),
                        style = {
                            size(width = flexHorizontally(grow = 1f), height = fixedVertically(40))
                            clip(roundedCornerShape(all = 8))
                            background(color(themeColorPrimaryContainer()))
                        }
                    ) { SimpleText(text = "Title (grow = 1f)", style = { padding(horizontal = 12, vertical = 10) }, color = color(themeColorOnPrimaryContainer())) }
                    Box(
                        id = randomId(),
                        style = {
                            size(width = fixedHorizontally(72), height = fixedVertically(40))
                            clip(roundedCornerShape(all = 8))
                            background(color(themeColorSecondaryContainer()))
                        }
                    ) { SimpleText(text = "Fixed", style = { padding(horizontal = 12, vertical = 10) }, color = color(themeColorOnSecondaryContainer())) }
                    Box(
                        id = randomId(),
                        style = {
                            size(width = fixedHorizontally(72), height = fixedVertically(40))
                            clip(roundedCornerShape(all = 8))
                            background(color(themeColorSecondaryContainer()))
                        }
                    ) { SimpleText(text = "Fixed", style = { padding(horizontal = 12, vertical = 10) }, color = color(themeColorOnSecondaryContainer())) }
                }
            }

            ShowroomSectionTitle("Code sample")
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

            ShowroomRelated(
                names = listOf("FlowRow", "Grid", "Row"),
                destination = "tileDetails"
            )
        }
    }
}

private fun TileSchemaBuilderScope.DirectionRow(label: String, direction: FlexBoxTileSchema.FlexDirectionSchema) {
    Column(arrangement = arrangeVerticallySpacedBy(6)) {
        SimpleText(text = label, typography = typographyLabelMedium())
        FlexBox(
            style = { size(width = fillHorizontally(), height = wrapVertically()) },
            direction = direction,
            columnGap = 8,
            rowGap = 8
        ) {
            listOf("1", "2", "3").forEach { FlexChip(it, themeColorPrimaryContainer(), themeColorOnPrimaryContainer()) }
        }
    }
}

private fun TileSchemaBuilderScope.JustifyRow(label: String, justifyContent: FlexBoxTileSchema.FlexJustifyContentSchema) {
    Column(arrangement = arrangeVerticallySpacedBy(6)) {
        SimpleText(text = label, typography = typographyLabelMedium())
        FlexBox(
            style = {
                size(width = fillHorizontally(), height = wrapVertically())
                clip(roundedCornerShape(all = 8))
            },
            direction = flexDirectionRow(),
            justifyContent = justifyContent
        ) {
            listOf("A", "B", "C").forEach { FlexChip(it, themeColorSecondaryContainer(), themeColorOnSecondaryContainer(), width = 48) }
        }
    }
}

private fun TileSchemaBuilderScope.WrapDemo(
    label: String,
    wrap: FlexBoxTileSchema.FlexWrapSchema,
) {
    Column(arrangement = arrangeVerticallySpacedBy(6)) {
        SimpleText(text = label, typography = typographyLabelMedium())
        Box(
            style = {
                size(width = fixedHorizontally(240), height = wrapVertically())
                clip(roundedCornerShape(all = 8))
            }
        ) {
            FlexBox(
                style = { size(width = fixedHorizontally(240), height = wrapVertically()) },
                direction = flexDirectionRow(),
                wrap = wrap,
                columnGap = 6,
                rowGap = 6
            ) {
                repeat(8) { i -> FlexChip("${i + 1}", themeColorTertiaryContainer(), themeColorOnTertiaryContainer(), width = 48) }
            }
        }
    }
}

private fun TileSchemaBuilderScope.FlexChip(
    label: String,
    containerColor: ColorSchema.Theme.Color,
    contentColor: ColorSchema.Theme.Color,
    width: Int? = null,
) {
    Box(
        id = randomId(),
        style = {
            size(
                width = width?.let { fixedHorizontally(it) } ?: wrapHorizontally(),
                height = fixedVertically(40)
            )
            clip(roundedCornerShape(all = 8))
            background(color(containerColor))
        }
    ) {
        SimpleText(text = label, style = { padding(horizontal = 12, vertical = 10) }, color = color(contentColor))
    }
}
