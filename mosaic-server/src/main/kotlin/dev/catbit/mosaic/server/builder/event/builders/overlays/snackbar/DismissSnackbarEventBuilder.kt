package dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar

import dev.catbit.mosaic.core.data.schemas.event.events.overlays.snackbar.DismissSnackbarEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class DismissSnackbarEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {}
) : EventSchemaBuilder<DismissSnackbarEventSchema>() {

    override fun build() = DismissSnackbarEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build()
    )
}

/**
 * Hides the snackbar currently showing on the screen, by broadcasting a dismiss command on the
 * screen channel. Does not consume `incomingData`. Dispatches `onSuccess` (no data) always,
 * right after the command is broadcast — the broadcast is fire-and-forget, so this fires even
 * when no snackbar is showing.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its trigger (`onSuccess`).
 */
fun EventSchemaBuilderScope.DismissSnackbar(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {}
) {
    addBuilder(
        DismissSnackbarEventBuilder(
            id = id,
            trigger = trigger,
            events = events
        )
    )
}
