package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.extensions.randomId
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
import dev.catbit.mosaic.server.builder.event.builders.time.StartTimeLoop
import dev.catbit.mosaic.server.builder.event.builders.time.seconds
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium

object CancelEventsEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "CancelEvents"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        val cancellableId = randomId()

        ShowroomScaffold {
            ShowroomHero(
                description = "Stops a cancellable event chain, identified by the same cancellableEventId used " +
                    "when starting it with RunCancellableEvents. Use it to stop a long-running process before it " +
                    "finishes on its own — for example, a StartTimeLoop running inside a RunCancellableEvents, " +
                    "cancelled when the user leaves the screen or taps \"stop\". CancelEvents always fires " +
                    "onSuccess() immediately, whether or not a chain with that id is currently running."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Start a cancellable loop and interrupt it") {
                SimpleText(
                    id = "cancel_events_status",
                    text = "Stopped",
                    typography = typographyBodyMedium()
                )
                Row(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeHorizontallySpacedBy(8)
                ) {
                    Button(
                        text = "Start cancellable loop",
                        events = {
                            RunCancellableEvents(
                                trigger = EventTriggers.onClick(),
                                cancellableEventId = cancellableId,
                                events = {
                                    UpdateTiles(
                                        trigger = EventTriggers.inline(),
                                        updates = {
                                            update(
                                                "cancel_events_status",
                                                inlineTileUpdateData("text" to "Running...")
                                            )
                                        }
                                    )
                                    StartTimeLoop(
                                        trigger = EventTriggers.inline(),
                                        timeData = seconds(1),
                                        events = {
                                            UpdateTiles(
                                                trigger = EventTriggers.onTimeLoop(),
                                                updates = {
                                                    update(
                                                        "cancel_events_status",
                                                        inlineTileUpdateData("text" to "Tick received — still running")
                                                    )
                                                }
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    )
                    Button(
                        text = "Cancel with CancelEvents",
                        buttonType = outlinedButton(),
                        events = {
                            CancelEvents(
                                trigger = EventTriggers.onClick(),
                                cancellableEventId = cancellableId,
                                events = {
                                    UpdateTiles(
                                        trigger = EventTriggers.onSuccess(),
                                        updates = {
                                            update(
                                                "cancel_events_status",
                                                inlineTileUpdateData("text" to "Cancelled ✓")
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    )
                }
                ShowroomNote(
                    "After cancelling, the text stops changing even after a second passes — the StartTimeLoop " +
                        "inside the RunCancellableEvents was really interrupted, not just \"visually paused\"."
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                val loopId = randomId()

                RunCancellableEvents(
                    trigger = EventTriggers.onClick(),
                    cancellableEventId = loopId,
                    events = {
                        StartTimeLoop(trigger = EventTriggers.inline(), timeData = seconds(1), events = {
                            UpdateTiles(trigger = EventTriggers.onTimeLoop(), updates = { /* ... */ })
                        })
                    }
                )

                CancelEvents(trigger = EventTriggers.onClick(), cancellableEventId = loopId)
                """
            )

            ShowroomRelated(
                names = listOf("RunCancellableEvents", "StartTimeLoop", "TriggerEvent"),
                destination = "eventDetails"
            )
        }
    }
}
