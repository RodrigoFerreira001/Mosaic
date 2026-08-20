package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.AdaptiveVisibilityTileSchema
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
import dev.catbit.mosaic.server.builder.color.themeColorPrimaryContainer
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.AdaptiveVisibility
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.grouping.heightVisibleFromCompact
import dev.catbit.mosaic.server.builder.tile.builders.grouping.heightVisibleFromExpanded
import dev.catbit.mosaic.server.builder.tile.builders.grouping.heightVisibleFromMedium
import dev.catbit.mosaic.server.builder.tile.builders.grouping.heightVisibleUntilCompact
import dev.catbit.mosaic.server.builder.tile.builders.grouping.heightVisibleUntilExpanded
import dev.catbit.mosaic.server.builder.tile.builders.grouping.heightVisibleUntilMedium
import dev.catbit.mosaic.server.builder.tile.builders.grouping.widthVisibleFromCompact
import dev.catbit.mosaic.server.builder.tile.builders.grouping.widthVisibleFromExpanded
import dev.catbit.mosaic.server.builder.tile.builders.grouping.widthVisibleFromExtraLarge
import dev.catbit.mosaic.server.builder.tile.builders.grouping.widthVisibleFromLarge
import dev.catbit.mosaic.server.builder.tile.builders.grouping.widthVisibleFromMedium
import dev.catbit.mosaic.server.builder.tile.builders.grouping.widthVisibleUntilCompact
import dev.catbit.mosaic.server.builder.tile.builders.grouping.widthVisibleUntilExpanded
import dev.catbit.mosaic.server.builder.tile.builders.grouping.widthVisibleUntilExtraLarge
import dev.catbit.mosaic.server.builder.tile.builders.grouping.widthVisibleUntilLarge
import dev.catbit.mosaic.server.builder.tile.builders.grouping.widthVisibleUntilMedium
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyLabelMedium

object AdaptiveVisibilityTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "AdaptiveVisibility"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Renders its children only when the window satisfies the configured width/height " +
                    "breakpoints — the foundation of Mosaic's responsive design. It's a transparent logical " +
                    "container: it has no layout of its own (style is not applied — the children participate " +
                    "directly in the parent's layout). Width and height rank on independent scales (width: " +
                    "Compact < Medium < Expanded < Large < ExtraLarge; height: Compact < Medium < Expanded) " +
                    "and both must hold — resize this window and watch each row below flip live."
            )

            ShowroomSectionTitle("width_visibility — every VisibleFrom(breakpoint) value")
            ShowroomDemoCard(title = "\"visible now\" only shows where the label says it should") {
                Column(arrangement = arrangeVerticallySpacedBy(10)) {
                    BreakpointRow("widthVisibleFromCompact() — always (Compact is the lowest rank)", widthVisibleFromCompact())
                    BreakpointRow("widthVisibleFromMedium() — Expanded and up", widthVisibleFromMedium())
                    BreakpointRow("widthVisibleFromExpanded() — Large and up", widthVisibleFromExpanded())
                    BreakpointRow("widthVisibleFromLarge() — ExtraLarge only", widthVisibleFromLarge())
                    BreakpointRow("widthVisibleFromExtraLarge() — never (ExtraLarge is the highest rank)", widthVisibleFromExtraLarge())
                }
            }

            ShowroomSectionTitle("width_visibility — every VisibleUntil(breakpoint) value")
            ShowroomDemoCard(title = "The mirror image — satisfied at or below the breakpoint") {
                Column(arrangement = arrangeVerticallySpacedBy(10)) {
                    BreakpointRow("widthVisibleUntilCompact() — Compact only", widthVisibleUntilCompact())
                    BreakpointRow("widthVisibleUntilMedium() — Compact and Medium", widthVisibleUntilMedium())
                    BreakpointRow("widthVisibleUntilExpanded() — up to Expanded", widthVisibleUntilExpanded())
                    BreakpointRow("widthVisibleUntilLarge() — up to Large", widthVisibleUntilLarge())
                    BreakpointRow("widthVisibleUntilExtraLarge() — always (ExtraLarge is the highest rank)", widthVisibleUntilExtraLarge())
                }
            }

            ShowroomSectionTitle("height_visibility — every value (only 3 ranks: Compact/Medium/Expanded)")
            ShowroomDemoCard(title = "Same idea, on the height axis — resize the window's height to test these") {
                Column(arrangement = arrangeVerticallySpacedBy(10)) {
                    BreakpointRow("heightVisibleFromCompact() — always", heightVisibility = heightVisibleFromCompact())
                    BreakpointRow("heightVisibleFromMedium() — Expanded only", heightVisibility = heightVisibleFromMedium())
                    BreakpointRow("heightVisibleFromExpanded() — never (Expanded is the highest rank)", heightVisibility = heightVisibleFromExpanded())
                    BreakpointRow("heightVisibleUntilCompact() — Compact only", heightVisibility = heightVisibleUntilCompact())
                    BreakpointRow("heightVisibleUntilMedium() — Compact and Medium", heightVisibility = heightVisibleUntilMedium())
                    BreakpointRow("heightVisibleUntilExpanded() — always", heightVisibility = heightVisibleUntilExpanded())
                }
            }

            ShowroomSectionTitle("Real usage: a responsive side panel")
            ShowroomDemoCard(title = "Only appears on Medium-width windows or larger — mirrors a real master/detail layout") {
                Row(style = { size(width = fillHorizontally(), height = fixedVertically(80)) }, arrangement = arrangeHorizontallySpacedBy(12)) {
                    Box(
                        style = {
                            size(width = fillHorizontally(), height = fillVertically())
                            clip(roundedCornerShape(all = 12))
                            background(color(themeColorPrimaryContainer()))
                        }
                    ) {
                        SimpleText(text = "Main content — always here", style = { padding(horizontal = 16, vertical = 16) })
                    }
                    AdaptiveVisibility(widthVisibility = widthVisibleFromMedium()) {
                        Box(
                            style = {
                                size(width = fixedHorizontally(180), height = fillVertically())
                                clip(roundedCornerShape(all = 12))
                                background(color(themeColorPrimaryContainer()))
                            }
                        ) {
                            SimpleText(text = "Side panel — Medium+ only", style = { padding(horizontal = 16, vertical = 16) })
                        }
                    }
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                AdaptiveVisibility(
                    id = "sidePanel",
                    widthVisibility = widthVisibleFromMedium(),
                    heightVisibility = heightVisibleUntilExpanded(),
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onWidthBreakpointSatisfied(),
                            updates = { /* e.g. flip a "layout: side-by-side" flag elsewhere */ }
                        )
                    }
                ) {
                    Column(id = "sidePanelContent") { /* detail panel */ }
                }
                """
            )

            ShowroomNote(
                text = "The AdaptiveVisibility tile itself always composes and dispatches its triggers " +
                    "(onDisplay, width/height satisfied/not-satisfied) — only its children are conditionally " +
                    "composed. Style/visibility apply to the wrapping Box around the children, not to the " +
                    "tile itself, which has no layout of its own."
            )

            ShowroomRelated(
                names = listOf("NavigationRail", "NavigationBar"),
                destination = "tileDetails"
            )
        }
    }
}

private fun TileSchemaBuilderScope.BreakpointRow(
    label: String,
    widthVisibility: AdaptiveVisibilityTileSchema.WidthVisibility? = null,
    heightVisibility: AdaptiveVisibilityTileSchema.HeightVisibility? = null,
) {
    Row(
        style = { size(width = fillHorizontally(), height = wrapVertically()) },
        arrangement = arrangeHorizontallySpacedBy(12),
        alignment = alignVerticallyToCenter()
    ) {
        SimpleText(
            text = label,
            typography = typographyLabelMedium(),
            style = { size(width = fixedHorizontally(280), height = wrapVertically()) }
        )
        if (widthVisibility != null) {
            AdaptiveVisibility(widthVisibility = widthVisibility) { VisibleNowChip() }
        } else if (heightVisibility != null) {
            AdaptiveVisibility(heightVisibility = heightVisibility) { VisibleNowChip() }
        }
    }
}

private fun TileSchemaBuilderScope.VisibleNowChip() {
    Box(
        style = {
            size(width = wrapHorizontally(), height = wrapVertically())
            clip(roundedCornerShape(all = 8))
            background(color(themeColorPrimaryContainer()))
            padding(horizontal = 12, vertical = 6)
        }
    ) {
        SimpleText(
            text = "visible now",
            typography = typographyLabelMedium(),
            color = color(themeColorOnPrimaryContainer())
        )
    }
}
