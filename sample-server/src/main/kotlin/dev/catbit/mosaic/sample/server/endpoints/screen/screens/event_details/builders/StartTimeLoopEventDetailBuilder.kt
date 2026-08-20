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
import dev.catbit.mosaic.server.builder.event.builders.time.StartTimeLoop
import dev.catbit.mosaic.server.builder.event.builders.time.seconds
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object StartTimeLoopEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "StartTimeLoop"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Starts an endless loop that fires onTimeLoop once per period, forever — the " +
                    "first fire happens after the first delay, not immediately. The event returns as soon as " +
                    "the loop is launched, so the rest of the chain keeps going while it ticks in the " +
                    "background. Since it never stops on its own, wrap it in RunCancellableEvents and cancel " +
                    "that id with CancelEvents when you actually need to stop it — there's no other way."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Start — every 2 seconds the label below really flips, driven by a live loop") {
                SimpleText(id = "time_loop_demo_status", text = "Not running")
                Row(arrangement = arrangeHorizontallySpacedBy(8)) {
                    Button(
                        text = "Start loop",
                        events = {
                            RunCancellableEvents(
                                trigger = EventTriggers.onClick(),
                                cancellableEventId = "showroom_time_loop",
                                events = {
                                    // events here is the cancellable job's own payload, run unconditionally —
                                    // not a downstream chain matched against a real onSuccess/onFailure, hence inline().
                                    StartTimeLoop(
                                        trigger = EventTriggers.inline(),
                                        timeData = seconds(2),
                                        events = {
                                            UpdateTiles(
                                                trigger = EventTriggers.onTimeLoop(),
                                                updates = {
                                                    update(tileId = "time_loop_demo_status", updateData = inlineTileUpdateData("text" to "Tick! (fires every 2s)"))
                                                }
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    )
                    Button(
                        text = "Stop loop",
                        events = {
                            CancelEvents(
                                trigger = EventTriggers.onClick(),
                                cancellableEventId = "showroom_time_loop",
                                events = {
                                    UpdateTiles(
                                        trigger = EventTriggers.onSuccess(),
                                        updates = {
                                            update(tileId = "time_loop_demo_status", updateData = inlineTileUpdateData("text" to "Stopped"))
                                        }
                                    )
                                }
                            )
                        }
                    )
                }
            }
            ShowroomNote(
                text = "onTimeLoop doesn't carry any incomingData — the label above just gets overwritten with " +
                    "a fixed string on every tick, not an incrementing count. A real tick counter needs " +
                    "server-side state (e.g. GetData/UpdateData) bumped on each onTimeLoop."
            )

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                RunCancellableEvents(
                    trigger = EventTriggers.onDisplay(),
                    cancellableEventId = "pollingLoop",
                    events = {
                        StartTimeLoop(
                            trigger = EventTriggers.inline(),
                            timeData = seconds(30),
                            events = {
                                SendNetworkRequest(
                                    trigger = EventTriggers.onTimeLoop(),
                                    url = "https://api.example.com/status"
                                )
                            }
                        )
                    }
                )

                // Stop it later, e.g. when the screen is torn down:
                CancelEvents(trigger = EventTriggers.onDisplayLeave(), cancellableEventId = "pollingLoop")
                """
            )

            ShowroomRelated(
                names = listOf("StartCountdownTimer", "RunCancellableEvents", "CancelEvents"),
                destination = "eventDetails"
            )
        }
    }
}
