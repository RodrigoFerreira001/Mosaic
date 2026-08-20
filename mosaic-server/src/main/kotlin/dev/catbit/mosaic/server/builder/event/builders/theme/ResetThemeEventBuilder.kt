package dev.catbit.mosaic.server.builder.event.builders.theme

import dev.catbit.mosaic.core.data.schemas.event.events.theme.ResetThemeEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class ResetThemeEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {}
) : EventSchemaBuilder<ResetThemeEventSchema>() {

    override fun build() = ResetThemeEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build()
    )
}

/**
 * Drops any color scheme previously installed by `SetTheme`, putting the app back on the color
 * scheme it was built with. Does not consume `incomingData`. Dispatches `onSuccess` (no data)
 * always, after the reset.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its trigger (`onSuccess`).
 */
fun EventSchemaBuilderScope.ResetTheme(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {}
) {
    addBuilder(
        ResetThemeEventBuilder(
            id = id,
            trigger = trigger,
            events = events
        )
    )
}
