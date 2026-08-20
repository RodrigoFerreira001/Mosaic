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
import dev.catbit.mosaic.server.builder.event.builders.event.RunEvents
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledButton
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium

object RunEventsEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "RunEvents"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Runs all child events unconditionally, grouping a chain under a single " +
                    "trigger — without the semantic onSuccess/onFailure chaining between them. Use it when " +
                    "several independent events need to fire from the same trigger and chaining them via " +
                    "onSuccess/onFailure would be misleading — for example, updating a tile, writing data, " +
                    "and showing a snackbar, all as a direct reaction to a click, with none depending on the " +
                    "others' result. RunEvents doesn't consume or transform the incomingData: it passes it " +
                    "along, intact, to every child event."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Click and watch both events fire together") {
                SimpleText(
                    id = "run_events_status",
                    text = "Waiting for click...",
                    typography = typographyBodyMedium()
                )
                Button(
                    text = "Fire events with RunEvents",
                    buttonType = filledButton(),
                    events = {
                        RunEvents(
                            trigger = EventTriggers.onClick(),
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.inline(),
                                    updates = {
                                        update(
                                            tileId = "run_events_status",
                                            updateData = inlineTileUpdateData("text" to "Tile updated ✓")
                                        )
                                    }
                                )
                                DisplaySnackbar(
                                    trigger = EventTriggers.inline(),
                                    message = "Snackbar fired alongside — without waiting for the UpdateTiles above"
                                )
                            }
                        )
                    }
                )
                ShowroomNote(
                    "Notice that UpdateTiles and DisplaySnackbar both use EventTriggers.inline() — neither " +
                        "one depends on the other's success. That's what sets RunEvents apart from chaining " +
                        "events via onSuccess/onFailure."
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                RunEvents(
                    trigger = EventTriggers.onClick(),
                    events = {
                        UpdateTiles(trigger = EventTriggers.inline(), updates = { /* ... */ })
                        UpdateData(trigger = EventTriggers.inline(), updates = { /* ... */ })
                        DisplaySnackbar(trigger = EventTriggers.inline(), message = "Saved")
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("TriggerEvent", "UpdateEvents", "AddTiles"),
                destination = "eventDetails"
            )
        }
    }
}
