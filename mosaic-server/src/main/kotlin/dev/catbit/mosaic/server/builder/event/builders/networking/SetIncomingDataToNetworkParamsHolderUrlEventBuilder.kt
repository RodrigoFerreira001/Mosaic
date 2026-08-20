package dev.catbit.mosaic.server.builder.event.builders.networking

import dev.catbit.mosaic.core.data.schemas.event.events.networking.SetIncomingDataToNetworkParamsHolderUrlEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class SetIncomingDataToNetworkParamsHolderUrlEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {}
) : EventSchemaBuilder<SetIncomingDataToNetworkParamsHolderUrlEventSchema>() {

    override fun build() = SetIncomingDataToNetworkParamsHolderUrlEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build()
    )
}

/**
 * Stores `incomingData` as the URL in the client's `NetworkParametersHolder`, so a later request
 * in the chain picks it up instead of carrying the URL on its own schema — the pattern behind
 * `SendFile`-style flows where the URL comes from a prior response. `incomingData` is required
 * and must be a `String`. Dispatches `onSuccess` (no data) when the URL was stored; `onFailure`
 * (no data, logged) when `incomingData` is missing or isn't a `String`.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 */
fun EventSchemaBuilderScope.SetIncomingDataToNetworkParamsHolderUrl(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {}
) {
    addBuilder(
        SetIncomingDataToNetworkParamsHolderUrlEventBuilder(
            id = id,
            trigger = trigger,
            events = events
        )
    )
}
