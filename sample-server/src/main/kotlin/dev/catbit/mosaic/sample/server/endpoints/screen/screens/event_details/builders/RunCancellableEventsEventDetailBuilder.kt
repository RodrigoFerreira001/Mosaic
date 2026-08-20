package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.event.CancelEvents
import dev.catbit.mosaic.server.builder.event.builders.event.RunCancellableEvents
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.mappedIncomingTileUpdateData
import dev.catbit.mosaic.server.builder.event.builders.time.StartCountdownTimer
import dev.catbit.mosaic.server.builder.event.builders.time.seconds
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.tile.builders.tooltip.Tooltip
import dev.catbit.mosaic.server.builder.tile.builders.tooltip.tooltipPositionBelow
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium

object RunCancellableEventsEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "RunCancellableEvents"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Runs its events block as a cancellable coroutine job, keyed by cancellableEventId " +
                    "— anything long-running or repeating (a countdown, a polling loop) can be stopped later " +
                    "with CancelEvents using the same id. Use it to wrap StartCountdownTimer/StartTimeLoop (the " +
                    "only two events that don't stop on their own), or any chain the user should be able to " +
                    "abort mid-flight. Starting a new RunCancellableEvents with an id already running replaces " +
                    "the previous job. events here is the job's own unconditional payload — not a trigger-" +
                    "matched chain — so its direct children use EventTriggers.inline()."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "A real, cancellable 10-second countdown") {
                SimpleText(id = "countdown_text", text = "TIMER", typography = typographyTitleMedium())
                Row(arrangement = arrangeHorizontallySpacedBy(8)) {
                    Tooltip(text = "Start timer", position = tooltipPositionBelow()) {
                        IconButton(
                            icon = icon("timer_play"),
                            events = {
                                RunCancellableEvents(
                                    trigger = EventTriggers.onClick(),
                                    cancellableEventId = "timer",
                                    events = {
                                        StartCountdownTimer(
                                            trigger = EventTriggers.inline(),
                                            timerData = seconds(initial = 10, step = 1),
                                            events = {
                                                UpdateTiles(
                                                    trigger = EventTriggers.onTimeTick(),
                                                    updates = {
                                                        update(
                                                            tileId = "countdown_text",
                                                            updateData = mappedIncomingTileUpdateData("text" to "<||>")
                                                        )
                                                    }
                                                )
                                                UpdateTiles(
                                                    trigger = EventTriggers.onTimeFinish(),
                                                    updates = {
                                                        update(tileId = "countdown_text", updateData = inlineTileUpdateData("text" to "FINISH"))
                                                    }
                                                )
                                            }
                                        )
                                    }
                                )
                            }
                        )
                    }
                    Tooltip(text = "Stop timer", position = tooltipPositionBelow()) {
                        IconButton(
                            icon = icon("timer_pause"),
                            events = {
                                CancelEvents(
                                    trigger = EventTriggers.onClick(),
                                    cancellableEventId = "timer"
                                )
                            }
                        )
                    }
                    Tooltip(text = "Restart timer", position = tooltipPositionBelow()) {
                        IconButton(
                            icon = icon("history"),
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onClick(),
                                    updates = {
                                        update(tileId = "countdown_text", updateData = inlineTileUpdateData("text" to "TIMER"))
                                    }
                                )
                            }
                        )
                    }
                }
                ShowroomNote(
                    "Stop doesn't reset the label back to \"TIMER\" by itself — CancelEvents just cancels the " +
                        "coroutine job, it doesn't chain any UpdateTiles here. The third button (history icon) " +
                        "resets the label directly, so you can start a fresh countdown after stopping one early."
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                RunCancellableEvents(
                    trigger = EventTriggers.onClick(),
                    cancellableEventId = "otp_countdown",
                    events = {
                        StartCountdownTimer(
                            trigger = EventTriggers.inline(),
                            timerData = seconds(initial = 60, step = 1),
                            events = {
                                UpdateTiles(trigger = EventTriggers.onTimeTick(), updates = { /* ... */ })
                            }
                        )
                    }
                )

                // Cancel it early, e.g. on a "Resend" button:
                CancelEvents(trigger = EventTriggers.onClick(), cancellableEventId = "otp_countdown")
                """
            )

            ShowroomRelated(
                names = listOf("CancelEvents", "StartCountdownTimer", "StartTimeLoop"),
                destination = "eventDetails"
            )
        }
    }
}
