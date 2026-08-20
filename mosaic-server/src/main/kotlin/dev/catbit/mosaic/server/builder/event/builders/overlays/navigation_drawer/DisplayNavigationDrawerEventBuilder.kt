package dev.catbit.mosaic.server.builder.event.builders.overlays.navigation_drawer

import dev.catbit.mosaic.core.data.schemas.event.events.overlays.navigation_drawer.DisplayNavigationDrawerEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class DisplayNavigationDrawerEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {}
) : EventSchemaBuilder<DisplayNavigationDrawerEventSchema>() {

    override fun build() = DisplayNavigationDrawerEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build()
    )
}

/**
 * Opens the screen's navigation drawer, by broadcasting an open command on the screen channel.
 * The drawer content is declared on the screen itself, so this event carries no parameters and
 * there is only ever one drawer per screen. Does not consume `incomingData`. Dispatches
 * `onSuccess` (no data) always, right after the command is broadcast — the broadcast is
 * fire-and-forget, so this fires even when the screen declares no drawer.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its trigger (`onSuccess`).
 */
fun EventSchemaBuilderScope.DisplayNavigationDrawer(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {}
) {
    addBuilder(
        DisplayNavigationDrawerEventBuilder(
            id = id,
            trigger = trigger,
            events = events
        )
    )
}
