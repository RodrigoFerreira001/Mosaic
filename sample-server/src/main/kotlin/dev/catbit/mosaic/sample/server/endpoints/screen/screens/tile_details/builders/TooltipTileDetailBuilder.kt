package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.tooltip.Tooltip
import dev.catbit.mosaic.server.builder.tile.builders.tooltip.tooltipPositionAbove
import dev.catbit.mosaic.server.builder.tile.builders.tooltip.tooltipPositionBelow
import dev.catbit.mosaic.server.builder.tile.builders.tooltip.tooltipPositionEnd
import dev.catbit.mosaic.server.builder.tile.builders.tooltip.tooltipPositionStart

object TooltipTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Tooltip"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Wraps an anchor tile and shows text on long-press/hover — the gesture is handled " +
                    "entirely by the client, with no server-side state. Unlike Menu/Popup, Tooltip has no " +
                    "server-controlled expanded — it's purely client-side. text, position, and appearance " +
                    "(shape/colors) are fixed in the schema, not reactive to events."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Long-press/hover each icon — 4 different positions") {
                Row(arrangement = arrangeHorizontallySpacedBy(16)) {
                    Tooltip(text = "Above", position = tooltipPositionAbove(), showCaret = true) {
                        IconButton(icon = icon("arrow_upward"))
                    }
                    Tooltip(text = "Below", position = tooltipPositionBelow(), showCaret = true) {
                        IconButton(icon = icon("arrow_downward"))
                    }
                    Tooltip(text = "Start", position = tooltipPositionStart(), showCaret = true) {
                        IconButton(icon = icon("arrow_back"))
                    }
                    Tooltip(text = "End", position = tooltipPositionEnd(), showCaret = true) {
                        IconButton(icon = icon("arrow_forward"))
                    }
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                Tooltip(
                    text = "Start timer",
                    position = tooltipPositionBelow(),
                    showCaret = true
                ) {
                    IconButton(icon = icon("timer_play"), events = { RunCancellableEvents(trigger = EventTriggers.onClick(), cancellableEventId = "timer") })
                }
                """
            )

            ShowroomRelated(
                names = listOf("IconButton", "Popup", "Menu"),
                destination = "tileDetails"
            )
        }
    }
}
