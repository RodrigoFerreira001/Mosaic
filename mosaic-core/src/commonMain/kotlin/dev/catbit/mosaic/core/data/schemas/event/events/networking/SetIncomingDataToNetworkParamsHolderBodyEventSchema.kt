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
 * Stores the event's incomingData as the request body in the client's `NetworkParametersHolder`,
 * so a later request in the chain picks it up instead of carrying the body on its own schema.
 *
 * **incomingData consumed:** required; any non-null value is accepted as-is.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — when the body was stored. No data is passed downstream.
 * - `OnFailureEventTrigger` — when incomingData is `null`; no data is passed downstream and the
 *   error is logged.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
    ]
)
@Serializable
@SerialName("SetIncomingDataToNetworkParamsHolderBody")
data class SetIncomingDataToNetworkParamsHolderBodyEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?
) : EventSchema