package dev.catbit.mosaic.server.builder.event.builders.data

import dev.catbit.mosaic.core.data.schemas.event.events.data.CheckForReceivedDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class CheckForReceivedDataEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val dataKey: String
) : EventSchemaBuilder<CheckForReceivedDataEventSchema>() {

    override fun build() = CheckForReceivedDataEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        dataKey = dataKey
    )
}

/**
 * Reads a one-shot value from the client's `DataMailer` under [dataKey] and branches on whether
 * it was there — the receiving half of a `SendData`/`CheckForReceivedData` handoff between
 * screens. Does not consume `incomingData`. Dispatches `onDataReceived` and `onSuccess` (both
 * carrying the value) when a value exists for [dataKey]; `onFailure` (no data) when none does.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onDataReceived`, `onSuccess`, `onFailure`).
 * @param dataKey Mailer key to check, matching the [dataKey] a `SendData` posted under.
 */
fun EventSchemaBuilderScope.CheckForReceivedData(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    dataKey: String
) {
    addBuilder(
        CheckForReceivedDataEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            dataKey = dataKey
        )
    )
}
