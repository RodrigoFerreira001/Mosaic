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
 * Stores the event's incomingData as the query parameters in the client's
 * `NetworkParametersHolder`, so a later request in the chain picks them up instead of carrying the
 * parameters on its own schema.
 *
 * **incomingData consumed:** required, and must be a map keyed by `String`.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — when the parameters were stored. No data is passed downstream.
 * - `OnFailureEventTrigger` — when incomingData is missing or is not a map; no data is passed
 *   downstream and the error is logged.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
    ]
)
@Serializable
@SerialName("SetIncomingDataToNetworkParamsHolderQueryParameters")
data class SetIncomingDataToNetworkParamsHolderQueryParametersEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?
) : EventSchema
