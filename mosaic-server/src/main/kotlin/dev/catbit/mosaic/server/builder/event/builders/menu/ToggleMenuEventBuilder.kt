package dev.catbit.mosaic.server.builder.event.builders.menu

import dev.catbit.mosaic.core.data.schemas.event.events.menu.ToggleMenuEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class ToggleMenuEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val menuId: String
) : EventSchemaBuilder<ToggleMenuEventSchema>() {

    override fun build() = ToggleMenuEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        menuId = menuId
    )
}

/**
 * Flips the open/closed state of the `Menu` tile identified by [menuId] — since the tile itself
 * only closes on dismissal, this is how a menu is opened from the server side, and, wired onto a
 * menu item's click, how it is closed after acting on the selection. Does not consume
 * `incomingData`. Dispatches `onSuccess` (no data) when the signal reached the tile; `onFailure`
 * (carrying the thrown exception, logged) when no tile with [menuId] is currently mounted.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param menuId Id of the `Menu` tile to toggle.
 */
fun EventSchemaBuilderScope.ToggleMenu(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    menuId: String
) {
    addBuilder(
        ToggleMenuEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            menuId = menuId
        )
    )
}
