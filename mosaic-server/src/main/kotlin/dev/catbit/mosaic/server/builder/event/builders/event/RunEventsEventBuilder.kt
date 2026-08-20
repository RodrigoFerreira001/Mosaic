package dev.catbit.mosaic.server.builder.event.builders.event

import dev.catbit.mosaic.core.data.schemas.event.events.event.RunEventsEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class RunEventsEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
) : EventSchemaBuilder<RunEventsEventSchema>() {

    override fun build() = RunEventsEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
    )
}

/**
 * Runs its own child [events] inline, in order, each receiving this event's `incomingData` — the
 * way to fan one trigger out into several events, or to group events for reuse, without changing
 * `incomingData` along the way. Unlike most events, [events] here is the payload executed rather
 * than a downstream chain; each entry is run guarded, so one failing event is logged and the rest
 * still run. Dispatches `onSuccess` (no data) when every event ran without throwing; `onFailure`
 * (no data), once at the end after all were attempted, when at least one threw (each failure
 * logged).
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Events run inline, in order, each receiving this event's `incomingData`.
 */
fun EventSchemaBuilderScope.RunEvents(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
) {
    addBuilder(
        RunEventsEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
        )
    )
}
