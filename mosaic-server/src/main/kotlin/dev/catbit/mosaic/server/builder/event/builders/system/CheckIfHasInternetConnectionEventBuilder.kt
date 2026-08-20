package dev.catbit.mosaic.server.builder.event.builders.system

import dev.catbit.mosaic.core.data.schemas.event.events.system.CheckIfHasInternetConnectionEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class CheckIfHasInternetConnectionEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {}
) : EventSchemaBuilder<CheckIfHasInternetConnectionEventSchema>() {

    override fun build() = CheckIfHasInternetConnectionEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build()
    )
}

/**
 * Asks the client's network layer whether the device currently has an internet connection, and
 * branches on the answer, on the IO dispatcher. Does not consume `incomingData`. Dispatches
 * `onStart` before the check runs; `onSuccess` (no data) when there is a connection; `onFailure`
 * (no data) when there is none.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onStart`, `onSuccess`, `onFailure`).
 */
fun EventSchemaBuilderScope.CheckIfHasInternetConnection(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {}
) {
    addBuilder(
        CheckIfHasInternetConnectionEventBuilder(
            id = id,
            trigger = trigger,
            events = events
        )
    )
}
