package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.color.ColorSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
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
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToBottom
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToTop
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallyToEnd
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallyToStart
import dev.catbit.mosaic.server.builder.placement.arrangeSpaceAround
import dev.catbit.mosaic.server.builder.placement.arrangeSpaceBetween
import dev.catbit.mosaic.server.builder.placement.arrangeSpaceEvenly
import dev.catbit.mosaic.server.builder.placement.arrangeToCenter
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyLabelMedium
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium

object RowTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Row"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Positions child tiles side by side — action bars, icon+label pairs, form rows. " +
                    "Shares Column's API, just on the horizontal axis: arrangement controls how children are " +
                    "spaced/positioned along the row, alignment controls their vertical (cross-axis) position, " +
                    "and scrollable turns overflow into horizontal scrolling."
            )

            ShowroomSectionTitle("Arrangement — how children are spaced")
            ShowroomDemoCard(title = "arrangeHorizontallyToStart()") {
                Row(arrangement = arrangeHorizontallyToStart(), style = { size(width = fillHorizontally(), height = wrapVertically()) }) {
                    RowChip("1", themeColorPrimaryContainer(), themeColorOnPrimaryContainer())
                    RowChip("2", themeColorPrimaryContainer(), themeColorOnPrimaryContainer())
                    RowChip("3", themeColorPrimaryContainer(), themeColorOnPrimaryContainer())
                }
            }
            ShowroomDemoCard(title = "arrangeHorizontallyToEnd()") {
                Row(arrangement = arrangeHorizontallyToEnd(), style = { size(width = fillHorizontally(), height = wrapVertically()) }) {
                    RowChip("1", themeColorPrimaryContainer(), themeColorOnPrimaryContainer())
                    RowChip("2", themeColorPrimaryContainer(), themeColorOnPrimaryContainer())
                    RowChip("3", themeColorPrimaryContainer(), themeColorOnPrimaryContainer())
                }
            }
            ShowroomDemoCard(title = "arrangeToCenter()") {
                Row(arrangement = arrangeToCenter(), style = { size(width = fillHorizontally(), height = wrapVertically()) }) {
                    RowChip("1", themeColorSecondaryContainer(), themeColorOnSecondaryContainer())
                    RowChip("2", themeColorSecondaryContainer(), themeColorOnSecondaryContainer())
                    RowChip("3", themeColorSecondaryContainer(), themeColorOnSecondaryContainer())
                }
            }
            ShowroomDemoCard(title = "arrangeSpaceBetween()") {
                Row(arrangement = arrangeSpaceBetween(), style = { size(width = fillHorizontally(), height = wrapVertically()) }) {
                    RowChip("1", themeColorSecondaryContainer(), themeColorOnSecondaryContainer())
                    RowChip("2", themeColorSecondaryContainer(), themeColorOnSecondaryContainer())
                    RowChip("3", themeColorSecondaryContainer(), themeColorOnSecondaryContainer())
                }
            }
            ShowroomDemoCard(title = "arrangeSpaceEvenly()") {
                Row(arrangement = arrangeSpaceEvenly(), style = { size(width = fillHorizontally(), height = wrapVertically()) }) {
                    RowChip("1", themeColorTertiaryContainer(), themeColorOnTertiaryContainer())
                    RowChip("2", themeColorTertiaryContainer(), themeColorOnTertiaryContainer())
                    RowChip("3", themeColorTertiaryContainer(), themeColorOnTertiaryContainer())
                }
            }
            ShowroomDemoCard(title = "arrangeSpaceAround()") {
                Row(arrangement = arrangeSpaceAround(), style = { size(width = fillHorizontally(), height = wrapVertically()) }) {
                    RowChip("1", themeColorTertiaryContainer(), themeColorOnTertiaryContainer())
                    RowChip("2", themeColorTertiaryContainer(), themeColorOnTertiaryContainer())
                    RowChip("3", themeColorTertiaryContainer(), themeColorOnTertiaryContainer())
                }
            }
            ShowroomDemoCard(title = "arrangeHorizontallySpacedBy(24) — fixed gap, regardless of container width") {
                Row(arrangement = arrangeHorizontallySpacedBy(24), style = { size(width = fillHorizontally(), height = wrapVertically()) }) {
                    RowChip("1", themeColorPrimaryContainer(), themeColorOnPrimaryContainer())
                    RowChip("2", themeColorPrimaryContainer(), themeColorOnPrimaryContainer())
                    RowChip("3", themeColorPrimaryContainer(), themeColorOnPrimaryContainer())
                }
            }

            ShowroomSectionTitle("Alignment — cross-axis (vertical) position")
            ShowroomDemoCard(title = "Mixed child heights, aligned top/center/bottom") {
                Row(
                    alignment = alignVerticallyToTop(),
                    arrangement = arrangeHorizontallySpacedBy(12),
                    style = { size(width = fillHorizontally(), height = fixedVertically(72)) }
                ) {
                    RowChip("top", themeColorPrimaryContainer(), themeColorOnPrimaryContainer(), heightDp = 32)
                    RowChip("top", themeColorPrimaryContainer(), themeColorOnPrimaryContainer(), heightDp = 56)
                }
                Row(
                    alignment = alignVerticallyToCenter(),
                    arrangement = arrangeHorizontallySpacedBy(12),
                    style = { size(width = fillHorizontally(), height = fixedVertically(72)) }
                ) {
                    RowChip("center", themeColorSecondaryContainer(), themeColorOnSecondaryContainer(), heightDp = 32)
                    RowChip("center", themeColorSecondaryContainer(), themeColorOnSecondaryContainer(), heightDp = 56)
                }
                Row(
                    alignment = alignVerticallyToBottom(),
                    arrangement = arrangeHorizontallySpacedBy(12),
                    style = { size(width = fillHorizontally(), height = fixedVertically(72)) }
                ) {
                    RowChip("bottom", themeColorTertiaryContainer(), themeColorOnTertiaryContainer(), heightDp = 32)
                    RowChip("bottom", themeColorTertiaryContainer(), themeColorOnTertiaryContainer(), heightDp = 56)
                }
            }

            ShowroomSectionTitle("Weighted children — weightHorizontally on size()")
            ShowroomDemoCard(title = "1 : 2 : 1 width split, only available inside a Row") {
                Row(arrangement = arrangeHorizontallySpacedBy(8), style = { size(width = fillHorizontally(), height = wrapVertically()) }) {
                    RowChip("1x", themeColorPrimaryContainer(), themeColorOnPrimaryContainer(), weight = 1f)
                    RowChip("2x", themeColorSecondaryContainer(), themeColorOnSecondaryContainer(), weight = 2f)
                    RowChip("1x", themeColorTertiaryContainer(), themeColorOnTertiaryContainer(), weight = 1f)
                }
            }

            ShowroomSectionTitle("scrollable — overflow becomes horizontal scroll")
            ShowroomDemoCard(title = "12 fixed-width chips in a row too narrow to fit them all") {
                Row(
                    scrollable = true,
                    arrangement = arrangeHorizontallySpacedBy(8),
                    style = { size(width = fillHorizontally(), height = wrapVertically()) }
                ) {
                    repeat(12) { i ->
                        RowChip("#$i", themeColorPrimaryContainer(), themeColorOnPrimaryContainer(), widthDp = 64)
                    }
                }
            }

            ShowroomSectionTitle("Real usage: an action bar")
            ShowroomDemoCard(title = "Title + trailing icon button, tap it") {
                Row(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeSpaceBetween(),
                    alignment = alignVerticallyToCenter()
                ) {
                    SimpleText(text = "Section title", typography = typographyTitleMedium())
                    IconButton(
                        icon = icon("search"),
                        events = {
                            DisplaySnackbar(trigger = EventTriggers.onClick(), message = "Search tapped")
                        }
                    )
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                Row(
                    id = "actionBar",
                    arrangement = arrangeSpaceBetween(),
                    alignment = alignVerticallyToCenter()
                ) {
                    SimpleText(text = "Title")
                    IconButton(
                        id = "searchBtn",
                        icon = icon("search"),
                        events = { DisplaySnackbar(trigger = EventTriggers.onClick(), message = "Search tapped") }
                    )
                }
                """
            )

            ShowroomRelated(
                names = listOf("Column", "LazyRow", "FlowRow"),
                destination = "tileDetails"
            )
        }
    }
}

private fun TileSchemaBuilderScope.RowChip(
    label: String,
    containerColor: ColorSchema.Theme.Color,
    contentColor: ColorSchema.Theme.Color,
    heightDp: Int? = null,
    widthDp: Int? = null,
    weight: Float? = null,
) {
    Row(
        arrangement = arrangeToCenter(),
        alignment = alignVerticallyToCenter(),
        style = {
            size(
                width = when {
                    weight != null -> weightHorizontally(weight)
                    widthDp != null -> fixedHorizontally(widthDp)
                    else -> wrapHorizontally()
                },
                height = heightDp?.let { fixedVertically(it) } ?: wrapVertically()
            )
            clip(roundedCornerShape(all = 12))
            background(color(containerColor))
            padding(horizontal = 16, vertical = 10)
        }
    ) {
        SimpleText(text = label, typography = typographyLabelMedium(), color = color(contentColor))
    }
}
