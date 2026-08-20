package dev.catbit.mosaic.server.builder.event.builders.event

import dev.catbit.mosaic.core.data.schemas.event.events.event.RunCancellableEventsEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class RunCancellableEventsEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val cancellableEventId: String,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
) : EventSchemaBuilder<RunCancellableEventsEventSchema>() {

    override fun build() = RunCancellableEventsEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        cancellableEventId = cancellableEventId,
    )
}

/**
 * Runs its own child [events] inline, in order, inside a cancellable job registered under
 * [cancellableEventId] — a later `CancelEvents` carrying the same id stops the job mid-flight.
 * Unlike most events, [events] here is the payload executed rather than a downstream chain, and
 * it receives this event's `incomingData`. The job runs in its own coroutine scope, so this event
 * returns as soon as the job is registered, without waiting for [events] to finish. Dispatches
 * `onSuccess` (no data) as soon as the job is registered — not when it finishes; `onFailure` when
 * [events] is empty, since there is nothing to run.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param cancellableEventId Id this cancellable job is registered under, matched by a later `CancelEvents`.
 * @param events Events run inline as the cancellable job's payload, receiving this event's `incomingData`.
 */
fun EventSchemaBuilderScope.RunCancellableEvents(
    id: String = randomId(),
    trigger: EventTrigger,
    cancellableEventId: String,
    events: EventSchemaBuilderScope.() -> Unit = {},
) {
    addBuilder(
        RunCancellableEventsEventBuilder(
            id = id,
            trigger = trigger,
            cancellableEventId = cancellableEventId,
            events = events,
        )
    )
}
