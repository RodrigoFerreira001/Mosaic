package dev.catbit.mosaic.server.builder.event.builders.event

import dev.catbit.mosaic.core.data.schemas.event.events.event.CancelEventsEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class CancelEventsEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val cancellableEventId: String,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
) : EventSchemaBuilder<CancelEventsEventSchema>() {

    override fun build() = CancelEventsEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        cancellableEventId = cancellableEventId,
    )
}

/**
 * Cancels the coroutine job registered under [cancellableEventId], stopping whatever
 * `RunCancellableEvents` started with that id. Does not consume `incomingData`. Dispatches
 * `onSuccess` (no data) when a running job was found and cancelled; `onFailure` (carrying a
 * `NoSuchElementException`, logged) when nothing is registered under [cancellableEventId] —
 * either it never ran or its job already finished.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param cancellableEventId Id matching the `cancellableEventId` a `RunCancellableEvents` started with.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 */
fun EventSchemaBuilderScope.CancelEvents(
    id: String = randomId(),
    trigger: EventTrigger,
    cancellableEventId: String,
    events: EventSchemaBuilderScope.() -> Unit = {},
) {
    addBuilder(
        CancelEventsEventBuilder(
            id = id,
            trigger = trigger,
            cancellableEventId = cancellableEventId,
            events = events,
        )
    )
}
