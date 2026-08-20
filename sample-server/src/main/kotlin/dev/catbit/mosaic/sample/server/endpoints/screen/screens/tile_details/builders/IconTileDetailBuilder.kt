package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorError
import dev.catbit.mosaic.server.builder.color.themeColorPrimary
import dev.catbit.mosaic.server.builder.color.themeColorTertiary
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.icon.outlinedIcon
import dev.catbit.mosaic.server.builder.icon.roundedIcon
import dev.catbit.mosaic.server.builder.icon.sharpIcon
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyLabelMedium

object IconTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Icon"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Renders a single Material Symbols icon using Mosaic's Icon composable, " +
                    "resolved from an IconSchema — no interaction of its own. Use Icon for decorative or " +
                    "informative elements that don't respond to touch — a status indicator, a marker next to " +
                    "text, an icon inside a Row. For clickable icons, use IconButton or wrap the icon in a " +
                    "Button. The IconSchema is built with the icon(name, color, size, style) helper, where " +
                    "style controls the Material Symbol's visual variant: outlinedIcon() (default), " +
                    "roundedIcon(), or sharpIcon()."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "4 real icons, each with a different name, size, and color") {
                Row(arrangement = arrangeHorizontallySpacedBy(20), alignment = alignVerticallyToCenter()) {
                    Icon(
                        icon = icon("favorite", size = 24, color = color(themeColorError()))
                    )
                    Icon(
                        icon = icon("star", size = 32, color = color(themeColorTertiary()), style = roundedIcon())
                    )
                    Icon(
                        icon = icon("verified", size = 40, color = color(themeColorPrimary()))
                    )
                    Icon(
                        icon = icon("notifications", size = 28, style = outlinedIcon())
                    )
                }
            }

            ShowroomSectionTitle("style — all 3 Material Symbol variants, same glyph")
            ShowroomDemoCard(title = "outlinedIcon() / roundedIcon() / sharpIcon()") {
                Row(arrangement = arrangeHorizontallySpacedBy(24)) {
                    Column(arrangement = arrangeVerticallySpacedBy(6)) {
                        Icon(icon = icon("settings", size = 36, style = outlinedIcon()))
                        SimpleText(text = "outlinedIcon()", typography = typographyLabelMedium())
                    }
                    Column(arrangement = arrangeVerticallySpacedBy(6)) {
                        Icon(icon = icon("settings", size = 36, style = roundedIcon()))
                        SimpleText(text = "roundedIcon()", typography = typographyLabelMedium())
                    }
                    Column(arrangement = arrangeVerticallySpacedBy(6)) {
                        Icon(icon = icon("settings", size = 36, style = sharpIcon()))
                        SimpleText(text = "sharpIcon()", typography = typographyLabelMedium())
                    }
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                Icon(
                    id = "statusIcon",
                    icon = icon("check_circle"),
                    style = { size(width = fixedHorizontally(24), height = fixedVertically(24)) }
                )
                """
            )

            ShowroomRelated(
                names = listOf("IconButton", "Image", "AsyncImage"),
                destination = "tileDetails"
            )
        }
    }
}
