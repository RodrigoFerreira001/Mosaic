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
 * Stores the event's incomingData as the URL in the client's `NetworkParametersHolder`, so a
 * later request in the chain picks it up instead of carrying the URL on its own schema.
 *
 * **incomingData consumed:** required, and must be a `String`.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — when the URL was stored. No data is passed downstream.
 * - `OnFailureEventTrigger` — when incomingData is missing or is not a `String`; no data is passed
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
@SerialName("SetIncomingDataToNetworkParamsHolderUrl")
data class SetIncomingDataToNetworkParamsHolderUrlEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?
) : EventSchema
