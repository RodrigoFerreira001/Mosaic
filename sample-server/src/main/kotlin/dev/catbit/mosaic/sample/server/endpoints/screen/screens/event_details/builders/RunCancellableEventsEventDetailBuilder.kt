package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.event.CancelEvents
import dev.catbit.mosaic.server.builder.event.builders.event.RunCancellableEvents
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.mappedIncomingTileUpdateData
import dev.catbit.mosaic.server.builder.event.builders.time.StartCountdownTimer
import dev.catbit.mosaic.server.builder.event.builders.time.seconds
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.tile.builders.tooltip.Tooltip
import dev.catbit.mosaic.server.builder.tile.builders.tooltip.tooltipPositionBelow

object RunCancellableEventsEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "RunCancellableEvents"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        Column(
            style = {
                size(
                    width = fillHorizontally(),
                    height = fillVertically()
                )
            }
        ) {
            SimpleText(
                id = "countdown_text",
                text = "TIMER"
            )
            Row {
                Tooltip(
                    text = "Start timer",
                    position = tooltipPositionBelow()
                ) {
                    IconButton(
                        icon = icon("timer_play"),
                        events = {
                            RunCancellableEvents(
                                trigger = EventTriggers.onClick(),
                                cancellableEventId = "timer",
                                events = {
                                    StartCountdownTimer(
                                        trigger = EventTriggers.inline(),
                                        timerData = seconds(
                                            initial = 10,
                                            step = 1
                                        ),
                                        events = {
                                            UpdateTiles(
                                                trigger = EventTriggers.onTimeTick(),
                                                updates = {
                                                    update(
                                                        tileId = "countdown_text",
                                                        updateData = mappedIncomingTileUpdateData(
                                                            "text" to "<//>"
                                                        )
                                                    )
                                                }
                                            )
                                            UpdateTiles(
                                                trigger = EventTriggers.onTimeFinish(),
                                                updates = {
                                                    update(
                                                        tileId = "countdown_text",
                                                        updateData = inlineTileUpdateData(
                                                            "text" to "FINISH"
                                                        )
                                                    )
                                                }
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    )
                }

                Tooltip(
                    text = "Stop timer",
                    position = tooltipPositionBelow()
                ) {
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

                Tooltip(
                    text = "Restart timer",
                    position = tooltipPositionBelow()
                ) {
                    IconButton(
                        icon = icon("history"),
                        events = {
                            UpdateTiles(
                                trigger = EventTriggers.onClick(),
                                updates = {
                                    update(
                                        tileId = "countdown_text",
                                        updateData = inlineTileUpdateData(
                                            "text" to "TIMER"
                                        )
                                    )
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}
