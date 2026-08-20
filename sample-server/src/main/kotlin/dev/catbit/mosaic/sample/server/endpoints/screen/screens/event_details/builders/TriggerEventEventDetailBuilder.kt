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
import dev.catbit.mosaic.server.builder.event.builders.event.TriggerEvent
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row

object TriggerEventEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "TriggerEvent"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Jumps to another event by eventId, anywhere in the same tile's event tree — " +
                    "lets you reuse a \"subroutine\" from multiple triggers. TriggerEvent carries no logic of " +
                    "its own: it just fires the event identified by eventId, as if that event's original " +
                    "trigger had fired now. It's useful for centralizing a sequence (e.g. \"save the form\") " +
                    "and calling it from several different buttons without duplicating the event tree. The " +
                    "target event (eventId) must be registered in the same tile's event tree — you can't point " +
                    "to an event on another tile or another screen."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Two buttons calling the same event by eventId") {
                Column(
                    id = "trigger_event_demo_root",
                    events = {
                        // "Subroutine" registered once, called by both buttons below
                        DisplaySnackbar(
                            id = "trigger_event_subroutine",
                            trigger = EventTriggers.inline(),
                            message = "Subroutine \"trigger_event_subroutine\" ran!"
                        )
                    }
                ) {
                    Row(arrangement = arrangeHorizontallySpacedBy(8)) {
                        Button(
                            text = "Button A → fires the subroutine",
                            events = {
                                TriggerEvent(
                                    trigger = EventTriggers.onClick(),
                                    eventId = "trigger_event_subroutine"
                                )
                            }
                        )
                        Button(
                            text = "Button B → fires the same subroutine",
                            buttonType = outlinedButton(),
                            events = {
                                TriggerEvent(
                                    trigger = EventTriggers.onClick(),
                                    eventId = "trigger_event_subroutine"
                                )
                            }
                        )
                    }
                }
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                Column(id = "root") {
                    events = {
                        // Reusable "subroutine"
                        DisplaySnackbar(
                            id = "notify_saved",
                            trigger = EventTriggers.inline(),
                            message = "Saved successfully!"
                        )
                    }
                }

                Button(
                    text = "Save",
                    events = {
                        TriggerEvent(
                            trigger = EventTriggers.onClick(),
                            eventId = "notify_saved"
                        )
                    }
                )
                """
            )

            ShowroomNote(
                "The target event (eventId) must be registered in the same tile's event tree — it's not " +
                    "possible to point to an event on another tile or another screen."
            )

            ShowroomRelated(
                names = listOf("RunEvents", "RunCancellableEvents", "CancelEvents"),
                destination = "eventDetails"
            )
        }
    }
}
