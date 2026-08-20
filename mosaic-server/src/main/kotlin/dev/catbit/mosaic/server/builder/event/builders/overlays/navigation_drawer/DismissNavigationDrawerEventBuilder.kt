package dev.catbit.mosaic.server.builder.event.builders.overlays.navigation_drawer

import dev.catbit.mosaic.core.data.schemas.event.events.overlays.navigation_drawer.DismissNavigationDrawerEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class DismissNavigationDrawerEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {}
) : EventSchemaBuilder<DismissNavigationDrawerEventSchema>() {

    override fun build() = DismissNavigationDrawerEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build()
    )
}

/**
 * Closes the screen's navigation drawer, by broadcasting a close command on the screen channel.
 * Does not consume `incomingData`. Dispatches `onSuccess` (no data) always, right after the
 * command is broadcast — the broadcast is fire-and-forget, so this fires even when no drawer is
 * open.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its trigger (`onSuccess`).
 */
fun EventSchemaBuilderScope.DismissNavigationDrawer(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {}
) {
    addBuilder(
        DismissNavigationDrawerEventBuilder(
            id = id,
            trigger = trigger,
            events = events
        )
    )
}
