package dev.catbit.mosaic.server.builder.event.builders.networking

import dev.catbit.mosaic.core.data.schemas.event.events.networking.SetIncomingDataToNetworkParamsHolderQueryParametersEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class SetIncomingDataToNetworkParamsHolderQueryParametersEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {}
) : EventSchemaBuilder<SetIncomingDataToNetworkParamsHolderQueryParametersEventSchema>() {

    override fun build() = SetIncomingDataToNetworkParamsHolderQueryParametersEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build()
    )
}

/**
 * Stores `incomingData` as the query parameters in the client's `NetworkParametersHolder`, so a
 * later request in the chain picks them up instead of carrying the parameters on its own schema.
 * `incomingData` is required and must be a map keyed by `String`. Dispatches `onSuccess` (no
 * data) when the parameters were stored; `onFailure` (no data, logged) when `incomingData` is
 * missing or isn't a map.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 */
fun EventSchemaBuilderScope.SetIncomingDataToNetworkParamsHolderQueryParameters(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {}
) {
    addBuilder(
        SetIncomingDataToNetworkParamsHolderQueryParametersEventBuilder(
            id = id,
            trigger = trigger,
            events = events
        )
    )
}
