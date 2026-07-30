package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.event.CancelEvents
import dev.catbit.mosaic.server.builder.event.builders.event.RunCancellableEvents
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.event.builders.time.StartTimeLoop
import dev.catbit.mosaic.server.builder.event.builders.time.seconds
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row

object StartTimeLoopEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "StartTimeLoop"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        Row {
            Button(
                text = "Start loop",
                events = {
                    RunCancellableEvents(
                        trigger = EventTriggers.onClick(),
                        cancellableEventId = "loop",
                        events = {
                            StartTimeLoop(
                                trigger = EventTriggers.inline(),
                                timeData = seconds(4),
                                events = {
                                    DisplaySnackbar(
                                        trigger = EventTriggers.onTimeLoop(),
                                        message = "Hello there!"
                                    )
                                }
                            )
                        }
                    )
                }
            )
            Button(
                text = "End loop",
                events = {
                    CancelEvents(
                        trigger = EventTriggers.onClick(),
                        cancellableEventId = "loop",
                    )
                }
            )
        }
    }
}
