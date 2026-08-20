package dev.catbit.mosaic.server.builder.event.builders.event

import dev.catbit.mosaic.core.data.schemas.event.events.event.TriggerEventEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class TriggerEventEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val eventId: String
) : EventSchemaBuilder<TriggerEventEventSchema>() {

    override fun build() = TriggerEventEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        eventId = eventId
    )
}

/**
 * Looks up the event registered on the screen under [eventId] and runs it inline, letting one
 * event chain reuse another that lives on a different tile. Forwards `incomingData` unchanged to
 * the event being run. Dispatches `onSuccess` (no data) after the target event ran; `onFailure`
 * when no event is registered under [eventId] (no data, logged) or when running it throws
 * (carrying the `Throwable`, logged).
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param eventId Id of the screen-registered event to run.
 */
fun EventSchemaBuilderScope.TriggerEvent(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    eventId: String
) {
    addBuilder(
        TriggerEventEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            eventId = eventId
        )
    )
}
