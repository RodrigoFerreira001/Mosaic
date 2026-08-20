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
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.mappedIncomingTileUpdateData
import dev.catbit.mosaic.server.builder.event.builders.time.StartCountdownTimer
import dev.catbit.mosaic.server.builder.event.builders.time.seconds
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object StartCountdownTimerEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "StartCountdownTimer"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Starts a countdown on the client, from timerData.initial down to timerData.step, " +
                    "in decrements of step. Meant for OTP-expiration countdowns, session-timeout warnings, or " +
                    "deadline-driven actions. onTimeTick fires once per step with the remaining count as " +
                    "incomingData (an Int); onTimeFinish fires once at the very end, with no data. step must " +
                    "be strictly less than initial, or the event fails to build."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Start — a real 8-second countdown, ticking once per second") {
                SimpleText(id = "countdown_demo_label", text = "Not started")
                Button(
                    text = "Start countdown",
                    events = {
                        StartCountdownTimer(
                            trigger = EventTriggers.onClick(),
                            timerData = seconds(initial = 8, step = 1),
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onTimeTick(),
                                    updates = {
                                        update(
                                            tileId = "countdown_demo_label",
                                            updateData = mappedIncomingTileUpdateData("text" to "Time left: <||>s")
                                        )
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onTimeFinish(),
                                    updates = {
                                        update(tileId = "countdown_demo_label", updateData = mappedIncomingTileUpdateData("text" to "Done!"))
                                    }
                                )
                                DisplaySnackbar(
                                    trigger = EventTriggers.onTimeFinish(),
                                    message = "Countdown finished"
                                )
                            }
                        )
                    }
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                StartCountdownTimer(
                    trigger = EventTriggers.onDisplay(),
                    timerData = seconds(initial = 120, step = 1),
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onTimeTick(),
                            updates = {
                                update(tileId = "timer_label", updateData = mappedIncomingTileUpdateData("text" to "<||>s left"))
                            }
                        )
                        DisplaySnackbar(trigger = EventTriggers.onTimeFinish(), message = "Session expired")
                    }
                )
                """
            )

            ShowroomNote(
                text = "There's no way to cancel a running countdown mid-flight — unlike StartTimeLoop, it " +
                    "isn't paired with a matching CancelEvents mechanism. Once started, it runs to completion."
            )

            ShowroomRelated(
                names = listOf("StartTimeLoop", "DisplaySnackbar", "UpdateTiles"),
                destination = "eventDetails"
            )
        }
    }
}
