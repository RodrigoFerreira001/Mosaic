package dev.catbit.mosaic.server.builder.event.builders.networking

import dev.catbit.mosaic.core.data.schemas.event.events.networking.SetIncomingDataToNetworkParamsHolderHeadersEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class SetIncomingDataToNetworkParamsHolderHeadersEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {}
) : EventSchemaBuilder<SetIncomingDataToNetworkParamsHolderHeadersEventSchema>() {

    override fun build() = SetIncomingDataToNetworkParamsHolderHeadersEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build()
    )
}

/**
 * Stores `incomingData` as the request headers in the client's `NetworkParametersHolder`, so a
 * later request in the chain picks them up instead of carrying the headers on its own schema.
 * `incomingData` is required and must be a map holding at least one `String` value; entries whose
 * value isn't a `String` are dropped. Dispatches `onSuccess` (no data) when the headers were
 * stored; `onFailure` (no data, logged) when `incomingData` is missing, isn't a map, or holds no
 * `String` value at all.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 */
fun EventSchemaBuilderScope.SetIncomingDataToNetworkParamsHolderHeaders(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {}
) {
    addBuilder(
        SetIncomingDataToNetworkParamsHolderHeadersEventBuilder(
            id = id,
            trigger = trigger,
            events = events
        )
    )
}
