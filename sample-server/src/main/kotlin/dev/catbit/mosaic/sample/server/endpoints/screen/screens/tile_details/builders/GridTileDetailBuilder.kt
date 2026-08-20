package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.color.ColorSchema
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
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
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignToCenter
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Grid
import dev.catbit.mosaic.server.builder.tile.builders.grouping.gridColumnFixed
import dev.catbit.mosaic.server.builder.tile.builders.grouping.gridColumnFraction
import dev.catbit.mosaic.server.builder.tile.builders.grouping.gridRowAuto
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium

object GridTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Grid"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A CSS-grid-like two-dimensional layout — columns and rows declared as lists of " +
                    "typed tracks (fixed, fraction, flexible, auto, max-content or min-content), for real 2D " +
                    "layouts like dashboards, image grids or complex forms. A cell's own size() can span " +
                    "multiple tracks via spanHorizontally/spanVertically in its style block — those only take " +
                    "effect inside a Grid ancestor."
            )

            ShowroomSectionTitle("Real usage: a stats dashboard, mixed track types")
            ShowroomDemoCard(title = "gridColumnFixed(56) icon rail + gridColumnFraction(1f) content, 3 rows of gridRowAuto()") {
                Grid(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    columns = listOf(gridColumnFixed(56), gridColumnFraction(1f)),
                    rows = listOf(gridRowAuto(), gridRowAuto(), gridRowAuto()),
                    columnGap = 12,
                    rowGap = 12
                ) {
                    listOf(
                        Triple("hiking", "1,204", "Trails logged"),
                        Triple("group", "38", "Active members"),
                        Triple("eco", "92%", "Trails rated Easy+")
                    ).forEach { (iconName, value, label) ->
                        StatIconCell(iconName)
                        StatValueCell(value, label)
                    }
                }
            }

            ShowroomSectionTitle("span — a cell occupying multiple tracks")
            ShowroomDemoCard(title = "6-column grid: one hero cell spans all 6, the rest span 2 each") {
                Grid(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    columns = List(6) { gridColumnFraction(1f / 6) },
                    rows = listOf(gridRowAuto(), gridRowAuto()),
                    columnGap = 8,
                    rowGap = 8
                ) {
                    GridCell("Hero — spanHorizontally(6)", themeColorPrimaryContainer(), themeColorOnPrimaryContainer(), spanColumns = 6)
                    GridCell("spanHorizontally(2)", themeColorSecondaryContainer(), themeColorOnSecondaryContainer(), spanColumns = 2)
                    GridCell("spanHorizontally(2)", themeColorSecondaryContainer(), themeColorOnSecondaryContainer(), spanColumns = 2)
                    GridCell("spanHorizontally(2)", themeColorTertiaryContainer(), themeColorOnTertiaryContainer(), spanColumns = 2)
                }
            }
            ShowroomNote(
                text = "spanHorizontally(n)/spanVertically(n) are Style DSL size() behaviors — see the Style " +
                    "tile detail page for the full span vs. FlexBox's flexHorizontally comparison."
            )

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                Grid(
                    id = "dashboardGrid",
                    columns = listOf(gridColumnFraction(0.5f), gridColumnFraction(0.5f)),
                    rows = listOf(gridRowFraction(1f)),
                    columnGap = 8,
                    rowGap = 8
                ) {
                    metrics.forEach { m -> Card(id = "metric_${'$'}{m.id}") { SimpleText(text = m.label) } }
                }
                """
            )

            ShowroomRelated(
                names = listOf("FlexBox", "FlowRow", "Column"),
                destination = "tileDetails"
            )
        }
    }
}

private fun TileSchemaBuilderScope.StatIconCell(iconName: String) {
    Box(
        id = randomId(),
        style = {
            size(width = fillHorizontally(), height = fillVertically())
            clip(roundedCornerShape(all = 12))
            background(color(themeColorSecondaryContainer()))
        },
        alignment = alignToCenter()
    ) {
        Icon(icon = icon(iconName, color = color(themeColorOnSecondaryContainer())))
    }
}

private fun TileSchemaBuilderScope.StatValueCell(value: String, label: String) {
    Column(
        id = randomId(),
        style = {
            size(width = fillHorizontally(), height = fillVertically())
            clip(roundedCornerShape(all = 12))
            background(color(themeColorPrimaryContainer()))
            padding(horizontal = 16, vertical = 12)
        }
    ) {
        SimpleText(text = value, typography = typographyTitleMedium(), color = color(themeColorOnPrimaryContainer()))
        SimpleText(text = label, color = color(themeColorOnPrimaryContainer()))
    }
}

private fun TileSchemaBuilderScope.GridCell(
    label: String,
    containerColor: ColorSchema.Theme.Color,
    contentColor: ColorSchema.Theme.Color,
    spanColumns: Int
) {
    Box(
        id = randomId(),
        style = {
            size(width = spanHorizontally(spanColumns), height = fixedVertically(56))
            clip(roundedCornerShape(all = 12))
            background(color(containerColor))
        },
        alignment = alignToCenter()
    ) {
        SimpleText(text = label, color = color(contentColor))
    }
}
