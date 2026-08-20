package dev.catbit.mosaic.server.builder.event.builders.popup

import dev.catbit.mosaic.core.data.schemas.event.events.popup.TogglePopupEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class TogglePopupEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val popupId: String
) : EventSchemaBuilder<TogglePopupEventSchema>() {

    override fun build() = TogglePopupEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        popupId = popupId
    )
}

/**
 * Flips the open/closed state of the `Popup` tile identified by [popupId] — since the tile
 * itself only closes on dismissal, this is how a popup is opened from the server side. Does not
 * consume `incomingData`. Dispatches `onSuccess` (no data) when the signal reached the tile;
 * `onFailure` (carrying the thrown exception, logged) when no tile with [popupId] is currently
 * mounted.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param popupId Id of the `Popup` tile to toggle.
 */
fun EventSchemaBuilderScope.TogglePopup(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    popupId: String
) {
    addBuilder(
        TogglePopupEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            popupId = popupId
        )
    )
}
