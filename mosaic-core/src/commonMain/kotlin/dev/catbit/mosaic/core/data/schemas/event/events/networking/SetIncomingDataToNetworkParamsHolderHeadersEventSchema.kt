package dev.catbit.mosaic.core.data.schemas.event.events.networking

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Stores the event's incomingData as the request headers in the client's
 * `NetworkParametersHolder`, so a later request in the chain picks them up instead of carrying the
 * headers on its own schema.
 *
 * **incomingData consumed:** required, and must be a map holding at least one `String` value.
 * Entries whose value is not a `String` are dropped.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — when the headers were stored. No data is passed downstream.
 * - `OnFailureEventTrigger` — when incomingData is missing, is not a map, or holds no `String`
 *   value at all; no data is passed downstream and the error is logged.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
    ]
)
@Serializable
@SerialName("SetIncomingDataToNetworkParamsHolderHeaders")
data class SetIncomingDataToNetworkParamsHolderHeadersEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?
) : EventSchema