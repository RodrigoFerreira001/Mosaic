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
import dev.catbit.mosaic.server.builder.event.builders.event.TriggerEvent
import dev.catbit.mosaic.server.builder.event.builders.event.UpdateEvents
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.incomingTileUpdateData
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium

object UpdateEventsEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "UpdateEvents"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        val targetEventId = randomId()

        ShowroomScaffold {
            ShowroomHero(
                description = "Patches the incomingData of an already-registered event without a network " +
                    "round-trip — useful for pre-filling an event with contextual data before firing it. Use " +
                    "it when you need to inject contextual data into an event at runtime — for example, " +
                    "storing the selected item's id in a delete event's holder when the user taps a list item, " +
                    "before actually firing that delete event. An unknown eventId is silently ignored; updates " +
                    "are applied in list order, with no rollback on partial failure."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Pre-fill an event's incomingData, then fire it") {
                SimpleText(
                    id = "update_events_target_text",
                    text = "(no value set yet)",
                    typography = typographyTitleMedium(),
                    // "Target" event attached to this very tile: trigger = inline() never fires on
                    // its own; it only runs when TriggerEvent invokes it below, using whatever
                    // incomingData UpdateEvents has written into it.
                    events = {
                        UpdateTiles(
                            id = targetEventId,
                            trigger = EventTriggers.inline(),
                            updates = {
                                update(
                                    tileId = "update_events_target_text",
                                    updateData = incomingTileUpdateData()
                                )
                            }
                        )
                    }
                )
                Row(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeHorizontallySpacedBy(8)
                ) {
                    Button(
                        text = "Set \"Value A\"",
                        buttonType = outlinedButton(),
                        events = {
                            UpdateEvents(
                                trigger = EventTriggers.onClick(),
                                updates = {
                                    update(
                                        eventId = targetEventId,
                                        data = mapOf("text" to "Value A ✓")
                                    )
                                },
                                events = {
                                    TriggerEvent(trigger = EventTriggers.onSuccess(), eventId = targetEventId)
                                }
                            )
                        }
                    )
                    Button(
                        text = "Set \"Value B\"",
                        buttonType = outlinedButton(),
                        events = {
                            UpdateEvents(
                                trigger = EventTriggers.onClick(),
                                updates = {
                                    update(
                                        eventId = targetEventId,
                                        data = mapOf("text" to "Value B ✓")
                                    )
                                },
                                events = {
                                    TriggerEvent(trigger = EventTriggers.onSuccess(), eventId = targetEventId)
                                }
                            )
                        }
                    )
                }
                ShowroomNote(
                    "The \"target\" UpdateTiles above has trigger = EventTriggers.inline() — it never fires " +
                        "on its own. UpdateEvents writes a new incomingData into it, and only then does " +
                        "TriggerEvent actually run it, using that freshly written value to update the text."
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                UpdateEvents(
                    trigger = EventTriggers.onClick(),
                    updates = {
                        update(
                            eventId = deleteEventId,
                            data = mapOf("itemId" to item.id)
                        )
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("TriggerEvent", "RunEvents", "UpdateTiles"),
                destination = "eventDetails"
            )
        }
    }
}
