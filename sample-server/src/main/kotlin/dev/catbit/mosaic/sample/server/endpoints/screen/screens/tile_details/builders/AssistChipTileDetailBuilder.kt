package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.builders.chips.AssistChip
import dev.catbit.mosaic.server.builder.tile.builders.chips.defaultAssistChip
import dev.catbit.mosaic.server.builder.tile.builders.chips.elevatedAssistChip
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row

object AssistChipTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "AssistChip"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A Material 3 chip for smart/automated actions that cross apps — like " +
                    "\"Add to Calendar\" or \"Share\". Use it for actions triggered by context, not selection: " +
                    "unlike FilterChip/InputChip, AssistChip has no selected state — it's always an action " +
                    "button wearing a chip's appearance."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Tap to fire a real action") {
                AssistChip(
                    id = "assist_chip_demo",
                    text = "Add to calendar",
                    leadingIcon = icon("calendar_today"),
                    variant = defaultAssistChip(),
                    events = {
                        DisplaySnackbar(
                            trigger = EventTriggers.onClick(),
                            message = "AssistChip fired a real event"
                        )
                    }
                )
            }

            ShowroomSectionTitle("variant — defaultAssistChip() vs elevatedAssistChip()")
            ShowroomDemoCard(title = "DEFAULT is outlined, ELEVATED adds a shadow. Also: trailingIcon and enabled = false") {
                Row(arrangement = arrangeHorizontallySpacedBy(8)) {
                    AssistChip(text = "Default", leadingIcon = icon("calendar_today"), variant = defaultAssistChip())
                    AssistChip(text = "Elevated", leadingIcon = icon("calendar_today"), variant = elevatedAssistChip())
                    AssistChip(text = "Trailing icon", trailingIcon = icon("open_in_new"))
                    AssistChip(text = "Disabled", leadingIcon = icon("calendar_today"), enabled = false)
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                AssistChip(
                    id = "calendarChip",
                    text = "Add to Calendar",
                    leadingIcon = icon("calendar_today"),
                    variant = defaultAssistChip(),
                    events = {
                        Navigate(trigger = EventTriggers.onClick(), screenId = "calendar")
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("FilterChip", "InputChip", "SuggestionChip"),
                destination = "tileDetails"
            )
        }
    }
}
