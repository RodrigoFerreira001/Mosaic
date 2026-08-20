package dev.catbit.mosaic.server.builder.event.builders.networking

import dev.catbit.mosaic.core.data.schemas.event.events.networking.SetIncomingDataToNetworkParamsHolderBodyEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class SetIncomingDataToNetworkParamsHolderBodyEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {}
) : EventSchemaBuilder<SetIncomingDataToNetworkParamsHolderBodyEventSchema>() {

    override fun build() = SetIncomingDataToNetworkParamsHolderBodyEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build()
    )
}

/**
 * Stores `incomingData` as the request body in the client's `NetworkParametersHolder`, so a
 * later request in the chain picks it up instead of carrying the body on its own schema.
 * `incomingData` is required; any non-null value is accepted as-is. Dispatches `onSuccess` (no
 * data) when the body was stored; `onFailure` (no data, logged) when `incomingData` is `null`.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 */
fun EventSchemaBuilderScope.SetIncomingDataToNetworkParamsHolderBody(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {}
) {
    addBuilder(
        SetIncomingDataToNetworkParamsHolderBodyEventBuilder(
            id = id,
            trigger = trigger,
            events = events
        )
    )
}
